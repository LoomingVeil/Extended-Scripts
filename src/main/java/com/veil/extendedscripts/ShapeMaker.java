package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.AbstractShapeMaker;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates sets of block positions that form 3D shapes.
 */
public class ShapeMaker implements AbstractShapeMaker {

    public static final ShapeMaker Instance = new ShapeMaker();

    // -------------------------------------------------------------------------
    // Internal plumbing
    // -------------------------------------------------------------------------

    @FunctionalInterface
    private interface ShapeTest {
        boolean accept(int dx, int dy, int dz);
    }

    /** Creates a new IPos via the NPC API. */
    private static IPos pos(int x, int y, int z) {
        return AbstractNpcAPI.Instance().getIPos(x, y, z);
    }

    /** Creates a new IPos via the NPC API using double coordinates. */
    private static IPos pos(double x, double y, double z) {
        return AbstractNpcAPI.Instance().getIPos(x, y, z);
    }

    /** Midpoint between two corner positions. */
    private static IPos midpoint(IPos a, IPos b) {
        return pos((a.getX() + b.getX()) / 2,
            (a.getY() + b.getY()) / 2,
            (a.getZ() + b.getZ()) / 2);
    }

    /**
     * Iterates every integer point in the axis-aligned bounding box
     * [cx±rx, cy±ry, cz±rz] and keeps those accepted by {@code test}.
     */
    private static IPos[] collect(IPos center, int rx, int ry, int rz, ShapeTest test) {
        int cx = center.getX(), cy = center.getY(), cz = center.getZ();
        List<IPos> pts = new ArrayList<>();
        for (int x = cx - rx; x <= cx + rx; x++)
            for (int y = cy - ry; y <= cy + ry; y++)
                for (int z = cz - rz; z <= cz + rz; z++)
                    if (test.accept(x - cx, y - cy, z - cz))
                        pts.add(pos(x, y, z));
        return pts.toArray(new IPos[0]);
    }

    /** Accepts a point that is inside {@code outer} but NOT inside {@code inner}. */
    private static ShapeTest shell(ShapeTest outer, ShapeTest inner) {
        return (dx, dy, dz) -> outer.accept(dx, dy, dz) && !inner.accept(dx, dy, dz);
    }

    /**
     * 1-voxel shell of {@code outer} using 6-neighbour adjacency.
     * A point is on the shell if it is in {@code outer} and at least one axial neighbour is not.
     */
    private static ShapeTest boundaryShell(ShapeTest outer) {
        return (dx, dy, dz) -> outer.accept(dx, dy, dz)
            && (!outer.accept(dx + 1, dy, dz)
            || !outer.accept(dx - 1, dy, dz)
            || !outer.accept(dx, dy + 1, dz)
            || !outer.accept(dx, dy - 1, dz)
            || !outer.accept(dx, dy, dz + 1)
            || !outer.accept(dx, dy, dz - 1));
    }

    /**
     * Returns one normalised squared-axis term for implicit quadric tests.
     * If {@code radius <= 0}, only the axis origin is considered valid.
     */
    private static double axisTerm(int d, double radius) {
        if (radius <= 0) return d == 0 ? 0.0 : Double.POSITIVE_INFINITY;
        return (d * (double) d) / (radius * radius);
    }

    /** True when (dx,dy,dz) is inside/on an axis-aligned ellipsoid. */
    private static boolean insideEllipsoid(int dx, int dy, int dz, double rx, double ry, double rz) {
        return axisTerm(dx, rx) + axisTerm(dy, ry) + axisTerm(dz, rz) <= 1.0;
    }

    /** True when (dx,dz) is inside/on an axis-aligned ellipse in XZ. */
    private static boolean insideEllipseXZ(int dx, int dz, double rx, double rz) {
        return axisTerm(dx, rx) + axisTerm(dz, rz) <= 1.0;
    }

    // =========================================================================
    // BOX
    // =========================================================================

    // --- Solid ---

    /** Solid box centred on {@code center} with the given full width, height, and length. */
    public IPos[] getBox(IPos center, int width, int height, int length) {
        int rx = width / 2, ry = height / 2, rz = length / 2;
        return collect(center, rx, ry, rz,
            (dx, dy, dz) -> Math.abs(dx) <= rx && Math.abs(dy) <= ry && Math.abs(dz) <= rz);
    }

    /** Solid box from corner {@code pos1} to corner {@code pos2}. */
    public IPos[] getBox(IPos pos1, IPos pos2) {
        int minX = Math.min(pos1.getX(), pos2.getX()), maxX = Math.max(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY()), maxY = Math.max(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ()), maxZ = Math.max(pos1.getZ(), pos2.getZ());
        List<IPos> pts = new ArrayList<>();
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    pts.add(pos(x, y, z));
        return pts.toArray(new IPos[0]);
    }

    // --- Hollow ---

    /** Hollow box with configurable shell thickness. */
    public IPos[] getHollowBox(IPos center, int width, int height, int length, double thickness) {
        int rx = width / 2, ry = height / 2, rz = length / 2;
        double irx = rx - thickness, iry = ry - thickness, irz = rz - thickness;
        return collect(center, rx, ry, rz, shell(
            (dx, dy, dz) -> Math.abs(dx) <= rx  && Math.abs(dy) <= ry  && Math.abs(dz) <= rz,
            (dx, dy, dz) -> Math.abs(dx) <= irx && Math.abs(dy) <= iry && Math.abs(dz) <= irz
        ));
    }

    /** Hollow box with shell thickness 1. */
    public IPos[] getHollowBox(IPos center, int width, int height, int length) {
        return getHollowBox(center, width, height, length, 1);
    }

    /** Hollow box from corner to corner with configurable shell thickness. */
    public IPos[] getHollowBox(IPos pos1, IPos pos2, double thickness) {
        double t = Math.max(0, thickness);
        int minX = Math.min(pos1.getX(), pos2.getX()), maxX = Math.max(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY()), maxY = Math.max(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ()), maxZ = Math.max(pos1.getZ(), pos2.getZ());
        List<IPos> pts = new ArrayList<>();
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++) {
                    boolean inner = x >= minX + t && x <= maxX - t
                        && y >= minY + t && y <= maxY - t
                        && z >= minZ + t && z <= maxZ - t;
                    if (!inner) pts.add(pos(x, y, z));
                }
        return pts.toArray(new IPos[0]);
    }

    /** Hollow box from corner to corner with shell thickness 1. */
    public IPos[] getHollowBox(IPos pos1, IPos pos2) {
        return getHollowBox(pos1, pos2, 1);
    }

    // =========================================================================
    // ELLIPSOID  (sphere is the equal-radii special case)
    // =========================================================================

    // --- Solid ---

    /** Solid ellipsoid centred on {@code center} with per-axis radii. */
    public IPos[] getEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ) {
        return collect(center, radiusX, radiusY, radiusZ,
            (dx, dy, dz) -> insideEllipsoid(dx, dy, dz, radiusX, radiusY, radiusZ));
    }

    /** Solid sphere centred on {@code center}. */
    public IPos[] getSphere(IPos center, int radius) {
        return getEllipsoid(center, radius, radius, radius);
    }

    // --- Hollow ---

    /** Hollow ellipsoid with configurable shell thickness. */
    public IPos[] getHollowEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ, double thickness) {
        double irx = Math.max(0, radiusX - thickness);
        double iry = Math.max(0, radiusY - thickness);
        double irz = Math.max(0, radiusZ - thickness);
        return collect(center, radiusX, radiusY, radiusZ, shell(
            (dx, dy, dz) -> insideEllipsoid(dx, dy, dz, radiusX, radiusY, radiusZ),
            (dx, dy, dz) -> irx > 0 && iry > 0 && irz > 0
                && insideEllipsoid(dx, dy, dz, irx, iry, irz)
        ));
    }

    /** Hollow ellipsoid with shell thickness 1. */
    public IPos[] getHollowEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ) {
        return getHollowEllipsoid(center, radiusX, radiusY, radiusZ, 1);
    }

    /** Hollow sphere with configurable shell thickness. */
    public IPos[] getHollowSphere(IPos center, int radius, double thickness) {
        return getHollowEllipsoid(center, radius, radius, radius, thickness);
    }

    /** Hollow sphere with shell thickness 1. */
    public IPos[] getHollowSphere(IPos center, int radius) {
        return getHollowSphere(center, radius, 1);
    }

    // =========================================================================
    // HEMISPHERE
    // =========================================================================

    // --- Solid ---

    /**
     * Solid hemisphere centred on the flat base.
     *
     * @param center   Centre of the flat base.
     * @param radiusX  Radius along X.
     * @param radiusY  Height of the dome above (or below) the base.
     * @param radiusZ  Radius along Z.
     * @param faceUp   {@code true} → dome rises upward; {@code false} → dome hangs downward.
     */
    public IPos[] getHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, boolean faceUp) {
        return collect(center, radiusX, radiusY, radiusZ, (dx, dy, dz) -> {
            if (faceUp  && dy < 0) return false;
            if (!faceUp && dy > 0) return false;
            return insideEllipsoid(dx, dy, dz, radiusX, radiusY, radiusZ);
        });
    }

    /** Solid upward-facing dome with equal XZ radius. */
    public IPos[] getHemisphere(IPos center, int radius, int height, boolean faceUp) {
        return getHemisphere(center, radius, height, radius, faceUp);
    }

    /** Solid upward-facing dome with equal XZ radius (defaults {@code faceUp = true}). */
    public IPos[] getHemisphere(IPos center, int radius, int height) {
        return getHemisphere(center, radius, height, radius, true);
    }

    // --- Hollow ---

    /** Hollow hemisphere with configurable shell thickness. */
    public IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, boolean faceUp, double thickness) {
        double irx = Math.max(0, radiusX - thickness);
        double iry = Math.max(0, radiusY - thickness);
        double irz = Math.max(0, radiusZ - thickness);
        return collect(center, radiusX, radiusY, radiusZ, (dx, dy, dz) -> {
            if (faceUp  && dy < 0) return false;
            if (!faceUp && dy > 0) return false;
            boolean outer = insideEllipsoid(dx, dy, dz, radiusX, radiusY, radiusZ);
            boolean inner = irx > 0 && iry > 0 && irz > 0
                && insideEllipsoid(dx, dy, dz, irx, iry, irz);
            return outer && !inner;
        });
    }

    /** Hollow upward-facing hemisphere with configurable shell thickness. */
    public IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, double thickness) {
        return getHollowHemisphere(center, radiusX, radiusY, radiusZ, true, thickness);
    }

    /** Hollow upward-facing hemisphere with shell thickness 1. */
    public IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ) {
        return getHollowHemisphere(center, radiusX, radiusY, radiusZ, true, 1);
    }

    // =========================================================================
    // CYLINDER
    // =========================================================================

    // --- Solid ---

    /**
     * Solid cylinder centred on {@code center} (mid-height).
     *
     * @param center Centre of the cylinder.
     * @param radius Radius of the circular cross-section (XZ plane).
     * @param height Full height.
     */
    public IPos[] getCylinder(IPos center, int radius, int height) {
        int ry = height / 2;
        double r2 = (double) radius * radius;
        return collect(center, radius, ry, radius,
            (dx, dy, dz) -> Math.abs(dy) <= ry && dx*dx + dz*dz <= r2);
    }

    /**
     * Solid elliptic cylinder from corner {@code pos1} to corner {@code pos2}.
     * The XZ cross-section is scaled to the bounding box; Y defines the height.
     */
    public IPos[] getCylinder(IPos pos1, IPos pos2) {
        IPos center = midpoint(pos1, pos2);
        int rx = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int ry = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int rz = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        return collect(center, rx, ry, rz,
            (dx, dy, dz) -> Math.abs(dy) <= ry && insideEllipseXZ(dx, dz, rx, rz));
    }

    // --- Hollow ---

    /** Hollow cylinder (pipe) with configurable wall thickness. */
    public IPos[] getHollowCylinder(IPos center, int radius, int height, double thickness) {
        int ry = height / 2;
        double or2 = (double) radius * radius;
        double ir  = Math.max(0, radius - thickness), ir2 = ir * ir;
        return collect(center, radius, ry, radius, shell(
            (dx, dy, dz) -> Math.abs(dy) <= ry && dx*dx + dz*dz <= or2,
            (dx, dy, dz) -> ir > 0 && Math.abs(dy) <= ry && dx*dx + dz*dz <= ir2
        ));
    }

    /** Hollow cylinder with wall thickness 1. */
    public IPos[] getHollowCylinder(IPos center, int radius, int height) {
        return getHollowCylinder(center, radius, height, 1);
    }

    // =========================================================================
    // PYRAMID
    // =========================================================================

    /**
     * Chebyshev (square cross-section) normalised distance from the pyramid axis.
     * Returns a value in [0, 1] where 0 = on axis and 1 = at the base edge.
     */
    private static double pyramidNorm(double dx, double dz, double rx, double rz) {
        return Math.max(Math.abs(dx) / rx, Math.abs(dz) / rz);
    }

    private static ShapeTest pyramidTest(double rx, double ry, double rz) {
        return (dx, dy, dz) -> {
            if (rx <= 0 || ry <= 0 || rz <= 0) return false;
            if (dy < -ry || dy > ry) return false;
            double norm = pyramidNorm(dx, dz, rx, rz);
            double y01 = (dy + ry) / (2.0 * ry); // 0 at base, 1 at apex
            return norm <= 1.0 - y01;
        };
    }

    // --- Solid ---

    /**
     * Solid pyramid centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseWidth  Full width of the base (X axis).
     * @param baseLength Full length of the base (Z axis).
     * @param height     Full height.
     */
    public IPos[] getPyramid(IPos center, int baseWidth, int baseLength, int height) {
        int rx = baseWidth / 2, ry = height / 2, rz = baseLength / 2;
        return collect(center, rx, ry, rz, pyramidTest(rx, ry, rz));
    }

    /** Solid pyramid from corner {@code pos1} to corner {@code pos2}. */
    public IPos[] getPyramid(IPos pos1, IPos pos2) {
        IPos center = midpoint(pos1, pos2);
        int rx = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int ry = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int rz = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        return collect(center, rx, ry, rz, pyramidTest(rx, ry, rz));
    }

    // --- Hollow ---

    /** Hollow pyramid with configurable shell thickness. */
    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height, double thickness) {
        int rx = baseWidth / 2, ry = height / 2, rz = baseLength / 2;
        if (thickness <= 1.0) {
            return collect(center, rx, ry, rz, boundaryShell(pyramidTest(rx, ry, rz)));
        }
        double irx = Math.max(0, rx - thickness);
        double iry = Math.max(0, ry - thickness);
        double irz = Math.max(0, rz - thickness);
        return collect(center, rx, ry, rz, shell(pyramidTest(rx, ry, rz), pyramidTest(irx, iry, irz)));
    }

    /** Hollow pyramid with shell thickness 1. */
    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height) {
        return getHollowPyramid(center, baseWidth, baseLength, height, 1);
    }

    /** Hollow pyramid from corner to corner with configurable shell thickness. */
    public IPos[] getHollowPyramid(IPos pos1, IPos pos2, double thickness) {
        IPos center = midpoint(pos1, pos2);
        int rx = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int ry = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int rz = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        if (thickness <= 1.0) {
            return collect(center, rx, ry, rz, boundaryShell(pyramidTest(rx, ry, rz)));
        }
        double irx = Math.max(0, rx - thickness);
        double iry = Math.max(0, ry - thickness);
        double irz = Math.max(0, rz - thickness);
        return collect(center, rx, ry, rz, shell(pyramidTest(rx, ry, rz), pyramidTest(irx, iry, irz)));
    }

    /** Hollow pyramid from corner to corner with shell thickness 1. */
    public IPos[] getHollowPyramid(IPos pos1, IPos pos2) {
        return getHollowPyramid(pos1, pos2, 1);
    }

    // =========================================================================
    // CONE
    // =========================================================================

    /** Euclidean (circular cross-section) normalised distance from the cone axis. */
    private static double coneNorm(double dx, double dz, double rx, double rz) {
        return Math.sqrt((dx*dx)/(rx*rx) + (dz*dz)/(rz*rz));
    }

    private static ShapeTest coneTest(double rx, double ry, double rz) {
        return (dx, dy, dz) -> {
            if (rx <= 0 || ry <= 0 || rz <= 0) return false;
            if (dy < -ry || dy > ry) return false;
            double norm = coneNorm(dx, dz, rx, rz);
            double y01 = (dy + ry) / (2.0 * ry); // 0 at base, 1 at apex
            return norm <= 1.0 - y01;
        };
    }

    // --- Solid ---

    /**
     * Solid cone centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseRadius Radius of the circular base (XZ plane).
     * @param height     Full height.
     */
    public IPos[] getCone(IPos center, int baseRadius, int height) {
        int ry = height / 2;
        return collect(center, baseRadius, ry, baseRadius, coneTest(baseRadius, ry, baseRadius));
    }

    // --- Hollow ---

    /** Hollow cone with configurable shell thickness. */
    public IPos[] getHollowCone(IPos center, int baseRadius, int height, double thickness) {
        int ry = height / 2;
        if (baseRadius <= 0 || ry <= 0) return new IPos[0];
        ShapeTest outer = coneTest(baseRadius, ry, baseRadius);
        if (thickness <= 1.0) {
            return collect(center, baseRadius, ry, baseRadius, boundaryShell(outer));
        }
        double t = Math.max(0, thickness);
        return collect(center, baseRadius, ry, baseRadius, (dx, dy, dz) -> {
            if (!outer.accept(dx, dy, dz)) return false;

            // Slice-wise radial carving keeps the hollow core continuous and avoids
            // artifacts caused by subtracting a shorter inner cone.
            double y01 = (dy + ry) / (2.0 * ry); // 0 at base, 1 at apex
            double outerRadiusAtY = baseRadius * (1.0 - y01);
            double innerRadiusAtY = outerRadiusAtY - t;
            if (innerRadiusAtY <= 0) return true;

            double distXZ = Math.sqrt(dx*dx + dz*dz);
            return distXZ > innerRadiusAtY;
        });
    }

    /** Hollow cone with shell thickness 1. */
    public IPos[] getHollowCone(IPos center, int baseRadius, int height) {
        return getHollowCone(center, baseRadius, height, 1);
    }

    // =========================================================================
    // CAPSULE  (cylinder + two hemispherical end-caps)
    // =========================================================================

    private static ShapeTest capsuleTest(double r2, int halfCyl) {
        return (dx, dy, dz) -> {
            double xz2 = dx*dx + dz*dz;
            if (Math.abs(dy) <= halfCyl) return xz2 <= r2;
            double capDy = Math.abs(dy) - halfCyl;
            return xz2 + capDy * capDy <= r2;
        };
    }

    // --- Solid ---

    /**
     * Solid capsule centred on the middle of the cylindrical body.
     *
     * @param center         Centre of the cylindrical section.
     * @param radius         Radius of the cylinder and both hemispherical caps.
     * @param cylinderHeight Height of the straight cylindrical section only
     *                       (total height = cylinderHeight + 2 * radius).
     */
    public IPos[] getCapsule(IPos center, int radius, int cylinderHeight) {
        int halfCyl = cylinderHeight / 2;
        int totalRy = halfCyl + radius;
        double r2 = (double) radius * radius;
        return collect(center, radius, totalRy, radius, capsuleTest(r2, halfCyl));
    }

    // --- Hollow ---

    /** Hollow capsule with configurable shell thickness. */
    public IPos[] getHollowCapsule(IPos center, int radius, int cylinderHeight, double thickness) {
        int halfCyl  = cylinderHeight / 2;
        int totalRy  = halfCyl + radius;
        double or2   = (double) radius * radius;
        double ir    = Math.max(0, radius - thickness);
        double ir2   = ir * ir;
        int iHalfCyl = (int) Math.max(0, halfCyl - thickness);
        ShapeTest outer = capsuleTest(or2, halfCyl);
        ShapeTest inner = capsuleTest(ir2, iHalfCyl);
        return collect(center, radius, totalRy, radius,
            shell(outer, (dx, dy, dz) -> ir > 0 && inner.accept(dx, dy, dz)));
    }

    /** Hollow capsule with shell thickness 1. */
    public IPos[] getHollowCapsule(IPos center, int radius, int cylinderHeight) {
        return getHollowCapsule(center, radius, cylinderHeight, 1);
    }

    // =========================================================================
    // TORUS
    // =========================================================================

    private static ShapeTest torusTest(double majorRadius, double mr2) {
        return (dx, dy, dz) -> {
            if (majorRadius <= 0 || mr2 <= 0) return false;
            double distXZ = Math.sqrt(dx*dx + dz*dz);
            double toRing = distXZ - majorRadius;
            return toRing * toRing + dy * dy <= mr2;
        };
    }

    // --- Solid ---

    /**
     * Solid torus lying flat in the XZ plane, centred on {@code center}.
     *
     * @param center      Centre of the torus.
     * @param majorRadius Distance from the torus centre to the centre of the tube.
     * @param minorRadius Radius of the tube.
     */
    public IPos[] getTorus(IPos center, int majorRadius, int minorRadius) {
        if (majorRadius <= minorRadius || minorRadius <= 0) return new IPos[0];
        int bound = majorRadius + minorRadius;
        double mr2 = (double) minorRadius * minorRadius;
        return collect(center, bound, minorRadius, bound, torusTest(majorRadius, mr2));
    }

    // --- Hollow ---

    /** Hollow torus with configurable tube-wall thickness. */
    public IPos[] getHollowTorus(IPos center, int majorRadius, int minorRadius, double thickness) {
        if (majorRadius <= minorRadius || minorRadius <= 0) return new IPos[0];
        int bound  = majorRadius + minorRadius;
        double omr2 = (double) minorRadius * minorRadius;
        double imr  = Math.max(0, minorRadius - thickness);
        double imr2 = imr * imr;
        ShapeTest outer = torusTest(majorRadius, omr2);
        ShapeTest inner = torusTest(majorRadius, imr2);
        return collect(center, bound, minorRadius, bound,
            shell(outer, (dx, dy, dz) -> imr > 0 && inner.accept(dx, dy, dz)));
    }

    /** Hollow torus with tube-wall thickness 1. */
    public IPos[] getHollowTorus(IPos center, int majorRadius, int minorRadius) {
        return getHollowTorus(center, majorRadius, minorRadius, 1);
    }

    // =========================================================================
    // CURVES
    // =========================================================================

    /**
     * Draws an arc through 3D space.
     *
     * @param referencePos  Anchor position used for offsets.
     * @param lookDir       Normalized forward direction.
     * @param radius        Arc radius.
     * @param heightOffset  Shift up/down along world Y from {@code referencePos}.
     * @param forwardOffset Shift forward along {@code lookDir} from {@code referencePos}.
     * @param angleDeg      Total sweep angle in degrees.
     * @param pitchDeg      Rotation of the arc plane around look direction, in degrees.
     * @param yawDeg        Rotation around world Y, in degrees.
     * @param segments      Number of line segments used to approximate the arc.
     */
    public IPos[] drawArc(IPos referencePos, IPos lookDir, double radius, double heightOffset, double forwardOffset, double angleDeg, double pitchDeg, double yawDeg, int segments) {
        int seg = Math.max(1, segments);
        double angle = Math.toRadians(angleDeg);
        double pitch = Math.toRadians(pitchDeg);
        double yaw = Math.toRadians(yawDeg);

        double fwdX = lookDir.getXD();
        double fwdY = lookDir.getYD();
        double fwdZ = lookDir.getZD();

        // Apply yaw by rotating around world Y axis.
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        double yawFwdX = fwdX * cosYaw + fwdZ * sinYaw;
        double yawFwdY = fwdY;
        double yawFwdZ = -fwdX * sinYaw + fwdZ * cosYaw;

        // right = cross(fwd, upWorld=(0,1,0)).
        double rightX = -yawFwdZ;
        double rightY = 0;
        double rightZ = yawFwdX;

        double rightLen = Math.sqrt(rightX * rightX + rightZ * rightZ);
        if (rightLen < 0.0001) {
            rightX = 1;
            rightY = 0;
            rightZ = 0;
            rightLen = 1;
        }
        rightX /= rightLen;
        rightY /= rightLen;
        rightZ /= rightLen;

        // up = cross(fwd, right).
        double upX = yawFwdY * rightZ - yawFwdZ * rightY;
        double upY = yawFwdZ * rightX - yawFwdX * rightZ;
        double upZ = yawFwdX * rightY - yawFwdY * rightX;

        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);
        double rightPX = rightX * cosPitch + upX * sinPitch;
        double rightPY = rightY * cosPitch + upY * sinPitch;
        double rightPZ = rightZ * cosPitch + upZ * sinPitch;

        double centerX = referencePos.getXD() + yawFwdX * forwardOffset;
        double centerY = referencePos.getYD() + heightOffset;
        double centerZ = referencePos.getZD() + yawFwdZ * forwardOffset;

        double halfAngle = angle / 2.0;
        List<IPos> points = new ArrayList<>(seg + 1);
        for (int i = 0; i <= seg; i++) {
            double t = i / (double) seg;
            double theta = -halfAngle + t * angle;
            double cosT = Math.cos(theta);
            double sinT = Math.sin(theta);

            points.add(pos(
                centerX + (yawFwdX * cosT + rightPX * sinT) * radius,
                centerY + (yawFwdY * cosT + rightPY * sinT) * radius,
                centerZ + (yawFwdZ * cosT + rightPZ * sinT) * radius
            ));
        }

        return points.toArray(new IPos[0]);
    }

    /**
     * Draws a simple arc centered at {@code center} in the XZ plane.
     *
     * @param center   Arc center.
     * @param radius   Arc radius.
     * @param angleDeg Total sweep angle in degrees.
     * @param segments Number of line segments used to approximate the arc.
     */
    public IPos[] drawArc(IPos center, double radius, double angleDeg, int segments) {
        int seg = Math.max(1, segments);
        double halfAngle = Math.toRadians(angleDeg) / 2.0;
        List<IPos> points = new ArrayList<>(seg + 1);
        for (int i = 0; i <= seg; i++) {
            double t = i / (double) seg;
            double theta = -halfAngle + t * (2.0 * halfAngle);
            points.add(pos(
                center.getXD() + radius * Math.cos(theta),
                center.getYD(),
                center.getZD() + radius * Math.sin(theta)
            ));
        }
        return points.toArray(new IPos[0]);
    }

    /**
     * Draws a simple arc and rotates it around its center.
     *
     * <p>Base arc lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    public IPos[] drawArc(IPos center, double radius, double angleDeg, int segments,
                          double pitchDeg, double yawDeg, double rollDeg) {
        IPos[] arc = drawArc(center, radius, angleDeg, segments);
        return rotatePoints(arc, center, pitchDeg, yawDeg, rollDeg);
    }

    /**
     * Draws a ring (full circle) centered at {@code center} in the XZ plane.
     *
     * @param center   Ring center.
     * @param radius   Ring radius.
     * @param segments Number of segments used to approximate the ring.
     */
    public IPos[] drawRing(IPos center, double radius, int segments) {
        int seg = Math.max(3, segments);
        List<IPos> points = new ArrayList<>(seg + 1);
        for (int i = 0; i <= seg; i++) {
            double t = i / (double) seg;
            double theta = 2.0 * Math.PI * t;
            points.add(pos(
                center.getXD() + radius * Math.cos(theta),
                center.getYD(),
                center.getZD() + radius * Math.sin(theta)
            ));
        }
        return points.toArray(new IPos[0]);
    }

    /**
     * Draws a ring and rotates it around its center.
     *
     * <p>Base ring lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    public IPos[] drawRing(IPos center, double radius, int segments, double pitchDeg, double yawDeg, double rollDeg) {
        IPos[] ring = drawRing(center, radius, segments);
        return rotatePoints(ring, center, pitchDeg, yawDeg, rollDeg);
    }

    /**
     * Draws a 3D Lissajous curve centered at {@code center}.
     *
     * @param center     Curve center.
     * @param amplitudeX Amplitude on X.
     * @param amplitudeY Amplitude on Y.
     * @param amplitudeZ Amplitude on Z.
     * @param freqX      Frequency multiplier on X.
     * @param freqY      Frequency multiplier on Y.
     * @param freqZ      Frequency multiplier on Z.
     * @param phaseDeg   Phase offset in degrees (applied to X component).
     * @param points     Number of sampled points.
     */
    public IPos[] drawLissajous(IPos center, double amplitudeX, double amplitudeY, double amplitudeZ,
                                double freqX, double freqY, double freqZ, double phaseDeg, int points) {
        int count = Math.max(0, points);
        if (count == 0) return new IPos[0];
        double phase = Math.toRadians(phaseDeg);

        List<IPos> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double t = (2.0 * Math.PI * i) / count;
            double x = center.getXD() + amplitudeX * Math.sin(freqX * t + phase);
            double y = center.getYD() + amplitudeY * Math.sin(freqY * t);
            double z = center.getZD() + amplitudeZ * Math.sin(freqZ * t);
            out.add(pos(x, y, z));
        }
        return out.toArray(new IPos[0]);
    }

    /**
     * Draws a straight line between two points.
     *
     * @param start  Start point.
     * @param end    End point.
     * @param points Number of sampled points including endpoints.
     */
    public IPos[] drawLine(IPos start, IPos end, int points) {
        if (start == null || end == null) return new IPos[0];
        int count = Math.max(2, points);

        List<IPos> out = new ArrayList<>(count);
        double sx = start.getXD(), sy = start.getYD(), sz = start.getZD();
        double ex = end.getXD(), ey = end.getYD(), ez = end.getZD();
        for (int i = 0; i < count; i++) {
            double t = i / (double) (count - 1);
            out.add(pos(
                sx + (ex - sx) * t,
                sy + (ey - sy) * t,
                sz + (ez - sz) * t
            ));
        }
        return out.toArray(new IPos[0]);
    }

    /**
     * Draws a Bezier curve of arbitrary degree.
     *
     * @param controlPoints Bezier control points (2+).
     * @param points        Number of sampled points including endpoints.
     */
    public IPos[] drawBezier(IPos[] controlPoints, int points) {
        if (controlPoints == null || controlPoints.length < 2) return new IPos[0];
        int count = Math.max(2, points);
        int n = controlPoints.length;

        List<IPos> out = new ArrayList<>(count);
        double[] workX = new double[n];
        double[] workY = new double[n];
        double[] workZ = new double[n];
        for (int i = 0; i < n; i++) {
            IPos p = controlPoints[i];
            if (p == null) return new IPos[0];
            workX[i] = p.getXD();
            workY[i] = p.getYD();
            workZ[i] = p.getZD();
        }

        for (int sample = 0; sample < count; sample++) {
            double t = sample / (double) (count - 1);
            double[] bx = workX.clone();
            double[] by = workY.clone();
            double[] bz = workZ.clone();

            for (int level = n - 1; level > 0; level--) {
                for (int i = 0; i < level; i++) {
                    bx[i] = bx[i] + (bx[i + 1] - bx[i]) * t;
                    by[i] = by[i] + (by[i + 1] - by[i]) * t;
                    bz[i] = bz[i] + (bz[i + 1] - bz[i]) * t;
                }
            }
            out.add(pos(bx[0], by[0], bz[0]));
        }

        return out.toArray(new IPos[0]);
    }

    /**
     * Draws a spiral in the XZ plane around {@code center}, with optional vertical rise.
     *
     * @param center      Spiral center.
     * @param radiusStart Radius at t=0.
     * @param radiusEnd   Radius at t=1.
     * @param height      Total Y displacement from start to end.
     * @param turns       Number of full rotations.
     * @param angleOffset Initial phase offset in degrees.
     * @param points      Number of sampled points.
     */
    public IPos[] drawSpiral(IPos center, double radiusStart, double radiusEnd, double height,
                             double turns, double angleOffset, int points) {
        if (center == null) return new IPos[0];
        int count = Math.max(1, points);
        double offset = Math.toRadians(angleOffset);

        List<IPos> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double t = count == 1 ? 0.0 : i / (double) (count - 1);
            double radius = radiusStart + (radiusEnd - radiusStart) * t;
            double theta = offset + (2.0 * Math.PI * turns * t);
            out.add(pos(
                center.getXD() + radius * Math.cos(theta),
                center.getYD() + height * t,
                center.getZD() + radius * Math.sin(theta)
            ));
        }
        return out.toArray(new IPos[0]);
    }

    /**
     * Draws a helix around world Y.
     *
     * @param referencePos Anchor position.
     * @param radius       Helix radius.
     * @param heightOffset Vertical offset from {@code referencePos}.
     * @param height       Vertical extent of the helix.
     * @param compression  Number of turns (1 = one full revolution).
     * @param angleOffset  Initial phase offset in degrees.
     * @param numPoints    Number of sampled points.
     */
    public IPos[] drawHelix(IPos referencePos, double radius, double heightOffset, double height,
                            double compression, double angleOffset, int numPoints) {
        int count = Math.max(0, numPoints);
        if (count == 0) return new IPos[0];
        double offset = Math.toRadians(angleOffset);
        List<IPos> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double t = i / (double) count;
            double angle = offset + 2.0 * Math.PI * compression * t;
            points.add(pos(
                referencePos.getXD() + radius * Math.cos(angle),
                referencePos.getYD() + heightOffset + height * t,
                referencePos.getZD() + radius * Math.sin(angle)
            ));
        }
        return points.toArray(new IPos[0]);
    }

    /**
     * Draws multiple helices phase-shifted around the same axis.
     *
     * @param referencePos   Anchor position.
     * @param radius         Helix radius.
     * @param heightOffset   Vertical offset from {@code referencePos}.
     * @param height         Vertical extent of each helix.
     * @param compression    Number of turns per helix.
     * @param angleOffset    Initial phase offset in degrees.
     * @param pointsPerHelix Number of sampled points per helix.
     * @param numHelices     Number of helices to generate.
     */
    public IPos[] drawMultiHelix(IPos referencePos, double radius, double heightOffset, double height, double compression, double angleOffset, int pointsPerHelix, int numHelices) {
        int helixCount = Math.max(0, numHelices);
        int perHelix = Math.max(0, pointsPerHelix);
        if (helixCount == 0 || perHelix == 0) return new IPos[0];

        List<IPos> points = new ArrayList<>(helixCount * perHelix);
        double phase = angleOffset;
        for (int i = 0; i < helixCount; i++) {
            IPos[] helix = drawHelix(referencePos, radius, heightOffset, height, compression, phase, perHelix);
            for (IPos p : helix) points.add(p);
            phase += 360.0 / helixCount;
        }
        return points.toArray(new IPos[0]);
    }

    // =========================================================================
    // TRANSFORMS
    // =========================================================================

    /**
     * Rotates a set of points around an explicit pivot.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    public IPos[] rotatePoints(IPos[] points, IPos pivot, double pitchDeg, double yawDeg, double rollDeg) {
        if (points == null || points.length == 0) return new IPos[0];
        if (pivot == null) return new IPos[0];

        double px = pivot.getXD();
        double py = pivot.getYD();
        double pz = pivot.getZD();

        double pitch = Math.toRadians(pitchDeg);
        double yaw = Math.toRadians(yawDeg);
        double roll = Math.toRadians(rollDeg);

        double cosX = Math.cos(pitch), sinX = Math.sin(pitch);
        double cosY = Math.cos(yaw), sinY = Math.sin(yaw);
        double cosZ = Math.cos(roll), sinZ = Math.sin(roll);

        List<IPos> out = new ArrayList<>(points.length);
        for (IPos point : points) {
            if (point == null) continue;

            // Translate to pivot-local coordinates.
            double x = point.getXD() - px;
            double y = point.getYD() - py;
            double z = point.getZD() - pz;

            // X rotation (pitch).
            double y1 = y * cosX - z * sinX;
            double z1 = y * sinX + z * cosX;
            double x1 = x;

            // Y rotation (yaw).
            double x2 = x1 * cosY + z1 * sinY;
            double z2 = -x1 * sinY + z1 * cosY;
            double y2 = y1;

            // Z rotation (roll).
            double x3 = x2 * cosZ - y2 * sinZ;
            double y3 = x2 * sinZ + y2 * cosZ;
            double z3 = z2;

            // Translate back to world coordinates.
            out.add(pos(x3 + px, y3 + py, z3 + pz));
        }
        return out.toArray(new IPos[0]);
    }

    /**
     * Rotates a set of points around their centroid.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    public IPos[] rotatePoints(IPos[] points, double pitchDeg, double yawDeg, double rollDeg) {
        if (points == null || points.length == 0) return new IPos[0];

        double sx = 0, sy = 0, sz = 0;
        int count = 0;
        for (IPos point : points) {
            if (point == null) continue;
            sx += point.getXD();
            sy += point.getYD();
            sz += point.getZD();
            count++;
        }
        if (count == 0) return new IPos[0];

        IPos centroid = pos(sx / count, sy / count, sz / count);
        return rotatePoints(points, centroid, pitchDeg, yawDeg, rollDeg);
    }
}
