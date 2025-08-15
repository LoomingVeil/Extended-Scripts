package com.veil.extendedscripts;

import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IPos;

import java.util.ArrayList;
import java.util.List;

enum ShapeType {
    CUBE,
    ELLIPSOID,
    CYLINDER,
    PYRAMID,
    CONE
}

/**
 * Provides a variant of methods for creating shapes
 */
public class ShapeMaker {
    public static ShapeMaker Instance = new ShapeMaker();

    /**
     * The core method to generate a list of points for a shape within a bounding box.
     * All public wrapper functions ultimately call this.
     *
     * @param type      The type of shape to create.
     * @param center    The center point of the shape's bounding box.
     * @param sizeX     The radius or half-width of the shape on the X-axis.
     * @param sizeY     The radius or half-height of the shape on the Y-axis.
     * @param sizeZ     The radius or half-length of the shape on the Z-axis.
     * @param hollow    If true, the shape will be hollow.
     * @param thickness The thickness of the shell if the shape is hollow.
     * @return An array of IPos objects representing the shape.
     */
    private static IPos[] getShape(
        ShapeType type,
        IPos center,
        int sizeX, int sizeY, int sizeZ,
        boolean hollow,
        double thickness
    ) {
        List<IPos> points = new ArrayList<>();

        // Define the bounding box for iteration
        int minX = center.getX() - sizeX;
        int maxX = center.getX() + sizeX;
        int minY = center.getY() - sizeY;
        int maxY = center.getY() + sizeY;
        int minZ = center.getZ() - sizeZ;
        int maxZ = center.getZ() + sizeZ;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (isInsideShape(type, x, y, z, center, sizeX, sizeY, sizeZ, hollow, thickness)) {
                        // In a real environment, you'd use your API instance here.
                        points.add(AbstractNpcAPI.Instance().getIPos(x, y, z));
                    }
                }
            }
        }
        // Convert the List to an array before returning.
        return points.toArray(new IPos[0]);
    }

    /**
     * Checks if a specific point (x, y, z) is part of the desired shape.
     */
    private static boolean isInsideShape(
        ShapeType type,
        int x, int y, int z,
        IPos center,
        int sizeX, int sizeY, int sizeZ,
        boolean hollow,
        double thickness
    ) {
        // Calculate the distance from the center for the current point.
        double dx = x - center.getX();
        double dy = y - center.getY();
        double dz = z - center.getZ();

        switch (type) {
            case CUBE: {
                boolean isOuter = Math.abs(dx) <= sizeX && Math.abs(dy) <= sizeY && Math.abs(dz) <= sizeZ;
                if (!hollow) return isOuter;

                // For hollow shapes, we "carve out" an inner shape.
                // A point is in the shell if it's in the outer shape but NOT the inner one.
                boolean isInner = Math.abs(dx) <= sizeX - thickness && Math.abs(dy) <= sizeY - thickness && Math.abs(dz) <= sizeZ - thickness;
                return isOuter && !isInner;
            }

            case ELLIPSOID: { // This logic correctly handles spheroids/ellipsoids.
                // Avoid division by zero if a size is zero.
                if (sizeX == 0 || sizeY == 0 || sizeZ == 0) return false;

                // Normalize coordinates to check against a unit sphere equation.
                double distSq = (dx * dx) / (sizeX * sizeX) + (dy * dy) / (sizeY * sizeY) + (dz * dz) / (sizeZ * sizeZ);
                boolean isOuter = distSq <= 1;
                if (!hollow) return isOuter;

                // For a hollow spheroid, the inner surface must also be a spheroid.
                double innerSizeX = Math.max(0, sizeX - thickness);
                double innerSizeY = Math.max(0, sizeY - thickness);
                double innerSizeZ = Math.max(0, sizeZ - thickness);
                if (innerSizeX == 0 || innerSizeY == 0 || innerSizeZ == 0) return isOuter; // No inner shape to carve

                double innerDistSq = (dx * dx) / (innerSizeX * innerSizeX) + (dy * dy) / (innerSizeY * innerSizeY) + (dz * dz) / (innerSizeZ * innerSizeZ);
                boolean isInner = innerDistSq <= 1;
                return isOuter && !isInner;
            }

            case CYLINDER: {
                // Check distance in the XZ plane for the circular cross-section.
                double distXZ = Math.sqrt(dx * dx + dz * dz);
                boolean isOuter = distXZ <= sizeX && Math.abs(dy) <= sizeY;
                if (!hollow) return isOuter;

                // Hollow cylinder is a pipe. The thickness applies to the circular wall.
                boolean isInner = distXZ <= sizeX - thickness && Math.abs(dy) <= sizeY;
                return isOuter && !isInner;
            }

            case PYRAMID: {
                // This vertical calculation method prevents holes in wide, short pyramids.
                // It calculates the max height for any given (x, z) column.
                if (sizeX <= 0 || sizeY <= 0 || sizeZ <= 0) return false;

                // 1. Calculate properties of the outer pyramid
                double norm_dist_outer = Math.max(Math.abs(dx) / sizeX, Math.abs(dz) / sizeZ);
                if (norm_dist_outer > 1) return false; // Outside the base
                double max_dy_outer = sizeY * (1.0 - norm_dist_outer);
                boolean isOuter = Math.abs(dy) <= max_dy_outer;

                if (!hollow) return isOuter;

                // 2. Calculate properties of the inner "carved out" pyramid
                double innerSizeX = Math.max(0, sizeX - thickness);
                double innerSizeY = Math.max(0, sizeY - thickness);
                double innerSizeZ = Math.max(0, sizeZ - thickness);
                if (innerSizeX <= 0 || innerSizeY <= 0 || innerSizeZ <= 0) return isOuter;

                double norm_dist_inner = Math.max(Math.abs(dx) / innerSizeX, Math.abs(dz) / innerSizeZ);
                if (norm_dist_inner > 1) { // This point is outside the inner pyramid's base
                    return isOuter; // so it must be part of the shell.
                }
                double max_dy_inner = innerSizeY * (1.0 - norm_dist_inner);
                boolean isInner = Math.abs(dy) <= max_dy_inner;

                return isOuter && !isInner;
            }

            case CONE: {
                // This uses the same vertical calculation method as the pyramid.
                if (sizeX <= 0 || sizeY <= 0 || sizeZ <= 0) return false;

                // 1. Calculate properties of the outer cone
                double norm_dist_outer = Math.sqrt((dx * dx) / (sizeX * sizeX) + (dz * dz) / (sizeZ * sizeZ));
                if (norm_dist_outer > 1) return false; // Outside the base
                double max_dy_outer = sizeY * (1.0 - norm_dist_outer);
                boolean isOuter = Math.abs(dy) <= max_dy_outer;

                if (!hollow) return isOuter;

                // 2. Calculate properties of the inner "carved out" cone
                double innerSizeX = Math.max(0, sizeX - thickness);
                double innerSizeY = Math.max(0, sizeY - thickness);
                double innerSizeZ = Math.max(0, sizeZ - thickness);
                if (innerSizeX <= 0 || innerSizeY <= 0 || innerSizeZ <= 0) return isOuter;

                double norm_dist_inner = Math.sqrt((dx * dx) / (innerSizeX * innerSizeX) + (dz * dz) / (innerSizeZ * innerSizeZ));
                if (norm_dist_inner > 1) { // This point is outside the inner cone's base
                    return isOuter; // so it must be part of the shell.
                }
                double max_dy_inner = innerSizeY * (1.0 - norm_dist_inner);
                boolean isInner = Math.abs(dy) <= max_dy_inner;

                return isOuter && !isInner;
            }
        }
        return false;
    }

    // --- Box / Cube Wrappers ---

    public IPos[] getBox(IPos center, int width, int length, int height) {
        return getShape(ShapeType.CUBE, center, width / 2, height / 2, length / 2, false, 0);
    }

    public IPos[] getBox(IPos pos1, IPos pos2) {
        List<IPos> points = new ArrayList<>();
        int minX = Math.min(pos1.getX(), pos2.getX());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    points.add(AbstractNpcAPI.Instance().getIPos(x, y, z));
                }
            }
        }
        return points.toArray(new IPos[0]);
    }

    public IPos[] getHollowBox(IPos center, int width, int length, int height, double thickness) {
        return getShape(ShapeType.CUBE, center, width / 2, height / 2, length / 2, true, thickness);
    }

    public IPos[] getHollowBox(IPos center, int width, int length, int height) {
        return getHollowBox(center, width, length, height, 1);
    }

    public IPos[] getHollowBox(IPos pos1, IPos pos2, double thickness) {
        List<IPos> points = new ArrayList<>();
        int minX = Math.min(pos1.getX(), pos2.getX());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        // Define the inner "carved out" area
        int innerMinX = minX + (int)thickness;
        int innerMaxX = maxX - (int)thickness;
        int innerMinY = minY + (int)thickness;
        int innerMaxY = maxY - (int)thickness;
        int innerMinZ = minZ + (int)thickness;
        int innerMaxZ = maxZ - (int)thickness;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Check if the point is inside the inner, carved-out box.
                    boolean isInner = (x >= innerMinX && x <= innerMaxX) &&
                        (y >= innerMinY && y <= innerMaxY) &&
                        (z >= innerMinZ && z <= innerMaxZ);
                    if (!isInner) {
                        points.add(AbstractNpcAPI.Instance().getIPos(x, y, z));
                    }
                }
            }
        }
        return points.toArray(new IPos[0]);
    }

    public IPos[] getHollowBox(IPos pos1, IPos pos2) {
        return getHollowBox(pos1, pos2, 1);
    }

    // --- Sphere / Spheroid Wrappers ---

    public IPos[] getEllipsoid(IPos center, int radius) {
        return getShape(ShapeType.ELLIPSOID, center, radius, radius, radius, false, 0);
    }

    public IPos[] getEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ) {
        return getShape(ShapeType.ELLIPSOID, center, sizeX, sizeY, sizeZ, false, 0);
    }

    public IPos[] getHollowEllipsoid(IPos center, int radius, double thickness) {
        return getShape(ShapeType.ELLIPSOID, center, radius, radius, radius, true, thickness);
    }

    public IPos[] getHollowEllipsoid(IPos center, int radius) {
        return getHollowEllipsoid(center, radius, 1);
    }

    public IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ, double thickness) {
        return getShape(ShapeType.ELLIPSOID, center, sizeX, sizeY, sizeZ, true, thickness);
    }

    public IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ) {
        return getHollowEllipsoid(center, sizeX, sizeY, sizeZ, 1);
    }

    // --- Cylinder Wrappers ---

    public IPos[] getCylinder(IPos center, int radius, int height) {
        return getShape(ShapeType.CYLINDER, center, radius, height/2, radius, false, 0);
    }

    public IPos[] getCylinder(IPos pos1, IPos pos2) {
        // We calculate the center and sizes from the bounding box.
        IPos center = getCenter(pos1, pos2);
        int sizeX = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int sizeY = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int sizeZ = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        // To ensure a circular base, we use the smaller of the X/Z dimensions for the radius.
        int radius = Math.min(sizeX, sizeZ);
        return getShape(ShapeType.CYLINDER, center, radius, sizeY, radius, false, 0);
    }

    public IPos[] getHollowCylinder(IPos center, int radius, int height, double thickness) {
        return getShape(ShapeType.CYLINDER, center, radius, height/2, radius, true, thickness);
    }

    public IPos[] getHollowCylinder(IPos center, int radius, int height) {
        return getHollowCylinder(center, radius, height, 1);
    }

    public IPos[] getHollowCylinder(IPos pos1, IPos pos2) {
        return getHollowCylinder(pos1, pos2, 1);
    }

    public IPos[] getHollowCylinder(IPos pos1, IPos pos2, double thickness) {
        IPos center = getCenter(pos1, pos2);
        int sizeX = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int sizeY = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int sizeZ = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        int radius = Math.min(sizeX, sizeZ);
        return getShape(ShapeType.CYLINDER, center, radius, sizeY, radius, true, thickness);
    }

    // --- Pyramid Wrappers ---

    public IPos[] getPyramid(IPos center, int baseWidth, int baseLength, int height) {
        return getShape(ShapeType.PYRAMID, center, baseWidth/2, height/2, baseLength/2, false, 0);
    }

    public IPos[] getPyramid(IPos pos1, IPos pos2) {
        IPos center = getCenter(pos1, pos2);
        // A pyramid base can be rectangular, so we use the X and Z sizes directly.
        int sizeX = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int sizeY = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int sizeZ = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        return getShape(ShapeType.PYRAMID, center, sizeX, sizeY, sizeZ, false, 0);
    }

    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height, double thickness) {
        return getShape(ShapeType.PYRAMID, center, baseWidth/2, height, baseLength/2, true, thickness);
    }

    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height) {
        return getHollowPyramid(center, baseWidth, baseLength, height, 1);
    }

    // --- Cone Wrappers ---

    public IPos[] getCone(IPos center, int baseRadius, int height) {
        return getShape(ShapeType.CONE, center, baseRadius, height/2, baseRadius, false, 0);
    }

    public IPos[] getCone(IPos pos1, IPos pos2) {
        IPos center = getCenter(pos1, pos2);
        int sizeX = Math.abs(pos1.getX() - pos2.getX()) / 2;
        int sizeY = Math.abs(pos1.getY() - pos2.getY()) / 2;
        int sizeZ = Math.abs(pos1.getZ() - pos2.getZ()) / 2;
        // Use the smaller dimension for a circular base.
        int radius = Math.min(sizeX, sizeZ);
        return getShape(ShapeType.CONE, center, radius, sizeY, radius, false, 0);
    }

    public IPos[] getHollowCone(IPos center, int baseRadius, int height, double thickness) {
        return getShape(ShapeType.CONE, center, baseRadius, height/2, baseRadius, true, thickness);
    }

    public IPos[] getHollowCone(IPos center, int baseRadius, int height) {
        return getHollowCone(center, baseRadius, height, 1);
    }

    // --- Helper function to calculate center from two points ---
    private static IPos getCenter(IPos pos1, IPos pos2) {
        int cx = (pos1.getX() + pos2.getX()) / 2;
        int cy = (pos1.getY() + pos2.getY()) / 2;
        int cz = (pos1.getZ() + pos2.getZ()) / 2;
        return AbstractNpcAPI.Instance().getIPos(cx, cy, cz);
    }
}
