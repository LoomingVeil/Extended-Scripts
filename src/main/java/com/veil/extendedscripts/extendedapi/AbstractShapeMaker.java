package com.veil.extendedscripts.extendedapi;

import noppes.npcs.api.IPos;

/**
 * Interface for generating sets of block positions that form 3D shapes.
 */
public interface AbstractShapeMaker {

    // =========================================================================
    // BOX
    // =========================================================================

    /** Solid box centred on {@code center} with the given full width, height, and length. */
    IPos[] getBox(IPos center, int width, int height, int length);

    /** Solid box from corner {@code pos1} to corner {@code pos2}. */
    IPos[] getBox(IPos pos1, IPos pos2);

    /** Hollow box with configurable shell thickness. */
    IPos[] getHollowBox(IPos center, int width, int height, int length, double thickness);

    /** Hollow box with shell thickness 1. */
    IPos[] getHollowBox(IPos center, int width, int height, int length);

    /** Hollow box from corner to corner with configurable shell thickness. */
    IPos[] getHollowBox(IPos pos1, IPos pos2, double thickness);

    /** Hollow box from corner to corner with shell thickness 1. */
    IPos[] getHollowBox(IPos pos1, IPos pos2);

    // =========================================================================
    // ELLIPSOID
    // =========================================================================

    /** Solid ellipsoid centred on {@code center} with per-axis radii. */
    IPos[] getEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ);

    /** Hollow ellipsoid with configurable shell thickness. */
    IPos[] getHollowEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ, double thickness);

    /** Hollow ellipsoid with shell thickness 1. */
    IPos[] getHollowEllipsoid(IPos center, int radiusX, int radiusY, int radiusZ);

    // =========================================================================
    // SPHERE
    // =========================================================================

    /** Solid sphere centred on {@code center}. */
    IPos[] getSphere(IPos center, int radius);

    /** Hollow sphere with configurable shell thickness. */
    IPos[] getHollowSphere(IPos center, int radius, double thickness);

    /** Hollow sphere with shell thickness 1. */
    IPos[] getHollowSphere(IPos center, int radius);

    // =========================================================================
    // HEMISPHERE
    // =========================================================================

    /**
     * Solid hemisphere centred on the flat base.
     *
     * @param center  Centre of the flat base.
     * @param radiusX Radius along X.
     * @param radiusY Height of the dome above (or below) the base.
     * @param radiusZ Radius along Z.
     * @param faceUp  {@code true} → dome rises upward; {@code false} → dome hangs downward.
     */
    IPos[] getHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, boolean faceUp);

    /** Solid hemisphere with equal XZ radius and configurable direction. */
    IPos[] getHemisphere(IPos center, int radius, int height, boolean faceUp);

    /** Solid upward-facing hemisphere with equal XZ radius. */
    IPos[] getHemisphere(IPos center, int radius, int height);

    /** Hollow hemisphere with configurable direction and shell thickness. */
    IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, boolean faceUp, double thickness);

    /** Hollow upward-facing hemisphere with configurable shell thickness. */
    IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ, double thickness);

    /** Hollow upward-facing hemisphere with shell thickness 1. */
    IPos[] getHollowHemisphere(IPos center, int radiusX, int radiusY, int radiusZ);

    // =========================================================================
    // CYLINDER
    // =========================================================================

    /**
     * Solid cylinder centred on {@code center} (mid-height).
     *
     * @param center Centre of the cylinder.
     * @param radius Radius of the circular cross-section (XZ plane).
     * @param height Full height.
     */
    IPos[] getCylinder(IPos center, int radius, int height);

    /**
     * Solid elliptic cylinder from corner {@code pos1} to corner {@code pos2}.
     * The XZ cross-section is scaled to fill the bounding box.
     */
    IPos[] getCylinder(IPos pos1, IPos pos2);

    /** Hollow cylinder (pipe) with configurable wall thickness. */
    IPos[] getHollowCylinder(IPos center, int radius, int height, double thickness);

    /** Hollow cylinder with wall thickness 1. */
    IPos[] getHollowCylinder(IPos center, int radius, int height);

    // =========================================================================
    // PYRAMID
    // =========================================================================

    /**
     * Solid pyramid centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseWidth  Full width of the base (X axis).
     * @param baseLength Full length of the base (Z axis).
     * @param height     Full height.
     */
    IPos[] getPyramid(IPos center, int baseWidth, int baseLength, int height);

    /** Solid pyramid from corner {@code pos1} to corner {@code pos2}. */
    IPos[] getPyramid(IPos pos1, IPos pos2);

    /** Hollow pyramid with configurable shell thickness. */
    IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height, double thickness);

    /** Hollow pyramid with shell thickness 1. */
    IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height);

    /** Hollow pyramid from corner to corner with configurable shell thickness. */
    IPos[] getHollowPyramid(IPos pos1, IPos pos2, double thickness);

    /** Hollow pyramid from corner to corner with shell thickness 1. */
    IPos[] getHollowPyramid(IPos pos1, IPos pos2);

    // =========================================================================
    // CONE
    // =========================================================================

    /**
     * Solid cone centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseRadius Radius of the circular base (XZ plane).
     * @param height     Full height.
     */
    IPos[] getCone(IPos center, int baseRadius, int height);

    /** Hollow cone with configurable shell thickness. */
    IPos[] getHollowCone(IPos center, int baseRadius, int height, double thickness);

    /** Hollow cone with shell thickness 1. */
    IPos[] getHollowCone(IPos center, int baseRadius, int height);

    // =========================================================================
    // CAPSULE
    // =========================================================================

    /**
     * Solid capsule (cylinder with hemispherical end-caps) centred on the middle
     * of the cylindrical body.
     *
     * @param center         Centre of the cylindrical section.
     * @param radius         Radius of both the cylinder and the end-caps.
     * @param cylinderHeight Height of the straight cylindrical section only.
     *                       Total height = {@code cylinderHeight + 2 * radius}.
     */
    IPos[] getCapsule(IPos center, int radius, int cylinderHeight);

    /** Hollow capsule with configurable shell thickness. */
    IPos[] getHollowCapsule(IPos center, int radius, int cylinderHeight, double thickness);

    /** Hollow capsule with shell thickness 1. */
    IPos[] getHollowCapsule(IPos center, int radius, int cylinderHeight);

    // =========================================================================
    // TORUS
    // =========================================================================

    /**
     * Solid torus lying flat in the XZ plane, centred on {@code center}.
     *
     * @param center      Centre of the torus.
     * @param majorRadius Distance from the torus centre to the centre of the tube.
     * @param minorRadius Radius of the tube itself.
     */
    IPos[] getTorus(IPos center, int majorRadius, int minorRadius);

    /** Hollow torus with configurable tube-wall thickness. */
    IPos[] getHollowTorus(IPos center, int majorRadius, int minorRadius, double thickness);

    /** Hollow torus with tube-wall thickness 1. */
    IPos[] getHollowTorus(IPos center, int majorRadius, int minorRadius);

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
    IPos[] drawArc(IPos referencePos, IPos lookDir, double radius, double heightOffset, double forwardOffset,
                   double angleDeg, double pitchDeg, double yawDeg, int segments);

    /**
     * Draws a simple arc centered at {@code center} in the XZ plane.
     *
     * @param center   Arc center.
     * @param radius   Arc radius.
     * @param angleDeg Total sweep angle in degrees.
     * @param segments Number of line segments used to approximate the arc.
     */
    IPos[] drawArc(IPos center, double radius, double angleDeg, int segments);

    /**
     * Draws a simple arc and rotates it around its center.
     *
     * <p>Base arc lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    IPos[] drawArc(IPos center, double radius, double angleDeg, int segments,
                   double pitchDeg, double yawDeg, double rollDeg);

    /**
     * Draws a ring (full circle) centered at {@code center} in the XZ plane.
     *
     * @param center   Ring center.
     * @param radius   Ring radius.
     * @param segments Number of segments used to approximate the ring.
     */
    IPos[] drawRing(IPos center, double radius, int segments);

    /**
     * Draws a ring and rotates it around its center.
     *
     * <p>Base ring lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    IPos[] drawRing(IPos center, double radius, int segments, double pitchDeg, double yawDeg, double rollDeg);

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
    IPos[] drawLissajous(IPos center, double amplitudeX, double amplitudeY, double amplitudeZ,
                         double freqX, double freqY, double freqZ, double phaseDeg, int points);

    /**
     * Draws a straight line between two points.
     *
     * @param start  Start point.
     * @param end    End point.
     * @param points Number of sampled points including endpoints.
     */
    IPos[] drawLine(IPos start, IPos end, int points);

    /**
     * Draws a Bezier curve of arbitrary degree.
     *
     * @param controlPoints Bezier control points (2+).
     * @param points        Number of sampled points including endpoints.
     */
    IPos[] drawBezier(IPos[] controlPoints, int points);

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
    IPos[] drawSpiral(IPos center, double radiusStart, double radiusEnd, double height,
                      double turns, double angleOffset, int points);

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
    IPos[] drawHelix(IPos referencePos, double radius, double heightOffset, double height,
                     double compression, double angleOffset, int numPoints);

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
    IPos[] drawMultiHelix(IPos referencePos, double radius, double heightOffset, double height,
                          double compression, double angleOffset, int pointsPerHelix, int numHelices);

    // =========================================================================
    // TRANSFORMS
    // =========================================================================

    /**
     * Rotates a set of points around an explicit pivot.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    IPos[] rotatePoints(IPos[] points, IPos pivot, double pitchDeg, double yawDeg, double rollDeg);

    /**
     * Rotates a set of points around their centroid.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    IPos[] rotatePoints(IPos[] points, double pitchDeg, double yawDeg, double rollDeg);

}
