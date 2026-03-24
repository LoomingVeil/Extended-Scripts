/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi
 */

/**
 * Interface for generating sets of block positions that form 3D shapes.
  * @javaFqn com.veil.extendedscripts.extendedapi.AbstractShapeMaker
*/
export interface AbstractShapeMaker {
    /** Solid box centred on {@code center} with the given full width, height, and length. */
    getBox(center: IPos, width: import('./int').int, height: import('./int').int, length: import('./int').int): IPos[];
    /** Solid box from corner {@code pos1} to corner {@code pos2}. */
    getBox(pos1: IPos, pos2: IPos): IPos[];
    /** Hollow box with configurable shell thickness. */
    getHollowBox(center: IPos, width: import('./int').int, height: import('./int').int, length: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow box with shell thickness 1. */
    getHollowBox(center: IPos, width: import('./int').int, height: import('./int').int, length: import('./int').int): IPos[];
    /** Hollow box from corner to corner with configurable shell thickness. */
    getHollowBox(pos1: IPos, pos2: IPos, thickness: import('./double').double): IPos[];
    /** Hollow box from corner to corner with shell thickness 1. */
    getHollowBox(pos1: IPos, pos2: IPos): IPos[];
    /** Solid ellipsoid centred on {@code center} with per-axis radii. */
    getEllipsoid(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int): IPos[];
    /** Hollow ellipsoid with configurable shell thickness. */
    getHollowEllipsoid(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow ellipsoid with shell thickness 1. */
    getHollowEllipsoid(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int): IPos[];
    /** Solid sphere centred on {@code center}. */
    getSphere(center: IPos, radius: import('./int').int): IPos[];
    /** Hollow sphere with configurable shell thickness. */
    getHollowSphere(center: IPos, radius: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow sphere with shell thickness 1. */
    getHollowSphere(center: IPos, radius: import('./int').int): IPos[];
    /**
     * Solid hemisphere centred on the flat base.
     *
     * @param center  Centre of the flat base.
     * @param radiusX Radius along X.
     * @param radiusY Height of the dome above (or below) the base.
     * @param radiusZ Radius along Z.
     * @param faceUp  {@code true} ? dome rises upward; {@code false} ? dome hangs downward.
     */
    getHemisphere(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int, faceUp: import('./boolean').boolean): IPos[];
    /** Solid hemisphere with equal XZ radius and configurable direction. */
    getHemisphere(center: IPos, radius: import('./int').int, height: import('./int').int, faceUp: import('./boolean').boolean): IPos[];
    /** Solid upward-facing hemisphere with equal XZ radius. */
    getHemisphere(center: IPos, radius: import('./int').int, height: import('./int').int): IPos[];
    /** Hollow hemisphere with configurable direction and shell thickness. */
    getHollowHemisphere(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int, faceUp: import('./boolean').boolean, thickness: import('./double').double): IPos[];
    /** Hollow upward-facing hemisphere with configurable shell thickness. */
    getHollowHemisphere(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow upward-facing hemisphere with shell thickness 1. */
    getHollowHemisphere(center: IPos, radiusX: import('./int').int, radiusY: import('./int').int, radiusZ: import('./int').int): IPos[];
    /**
     * Solid cylinder centred on {@code center} (mid-height).
     *
     * @param center Centre of the cylinder.
     * @param radius Radius of the circular cross-section (XZ plane).
     * @param height Full height.
     */
    getCylinder(center: IPos, radius: import('./int').int, height: import('./int').int): IPos[];
    /**
     * Solid elliptic cylinder from corner {@code pos1} to corner {@code pos2}.
     * The XZ cross-section is scaled to fill the bounding box.
     */
    getCylinder(pos1: IPos, pos2: IPos): IPos[];
    /** Hollow cylinder (pipe) with configurable wall thickness. */
    getHollowCylinder(center: IPos, radius: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow cylinder with wall thickness 1. */
    getHollowCylinder(center: IPos, radius: import('./int').int, height: import('./int').int): IPos[];
    /**
     * Solid pyramid centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseWidth  Full width of the base (X axis).
     * @param baseLength Full length of the base (Z axis).
     * @param height     Full height.
     */
    getPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int): IPos[];
    /** Solid pyramid from corner {@code pos1} to corner {@code pos2}. */
    getPyramid(pos1: IPos, pos2: IPos): IPos[];
    /** Hollow pyramid with configurable shell thickness. */
    getHollowPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow pyramid with shell thickness 1. */
    getHollowPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int): IPos[];
    /** Hollow pyramid from corner to corner with configurable shell thickness. */
    getHollowPyramid(pos1: IPos, pos2: IPos, thickness: import('./double').double): IPos[];
    /** Hollow pyramid from corner to corner with shell thickness 1. */
    getHollowPyramid(pos1: IPos, pos2: IPos): IPos[];
    /**
     * Solid cone centred on its geometric centre (mid-height).
     *
     * @param center     Centre of the bounding box.
     * @param baseRadius Radius of the circular base (XZ plane).
     * @param height     Full height.
     */
    getCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int): IPos[];
    /** Hollow cone with configurable shell thickness. */
    getHollowCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow cone with shell thickness 1. */
    getHollowCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int): IPos[];
    /**
     * Solid capsule (cylinder with hemispherical end-caps) centred on the middle
     * of the cylindrical body.
     *
     * @param center         Centre of the cylindrical section.
     * @param radius         Radius of both the cylinder and the end-caps.
     * @param cylinderHeight Height of the straight cylindrical section only.
     *                       Total height = {@code cylinderHeight + 2 * radius}.
     */
    getCapsule(center: IPos, radius: import('./int').int, cylinderHeight: import('./int').int): IPos[];
    /** Hollow capsule with configurable shell thickness. */
    getHollowCapsule(center: IPos, radius: import('./int').int, cylinderHeight: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow capsule with shell thickness 1. */
    getHollowCapsule(center: IPos, radius: import('./int').int, cylinderHeight: import('./int').int): IPos[];
    /**
     * Solid torus lying flat in the XZ plane, centred on {@code center}.
     *
     * @param center      Centre of the torus.
     * @param majorRadius Distance from the torus centre to the centre of the tube.
     * @param minorRadius Radius of the tube itself.
     */
    getTorus(center: IPos, majorRadius: import('./int').int, minorRadius: import('./int').int): IPos[];
    /** Hollow torus with configurable tube-wall thickness. */
    getHollowTorus(center: IPos, majorRadius: import('./int').int, minorRadius: import('./int').int, thickness: import('./double').double): IPos[];
    /** Hollow torus with tube-wall thickness 1. */
    getHollowTorus(center: IPos, majorRadius: import('./int').int, minorRadius: import('./int').int): IPos[];
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
    drawArc(referencePos: IPos, lookDir: IPos, radius: import('./double').double, heightOffset: import('./double').double, forwardOffset: import('./double').double, angleDeg: import('./double').double, pitchDeg: import('./double').double, yawDeg: import('./double').double, segments: import('./int').int): IPos[];
    /**
     * Draws a simple arc centered at {@code center} in the XZ plane.
     *
     * @param center   Arc center.
     * @param radius   Arc radius.
     * @param angleDeg Total sweep angle in degrees.
     * @param segments Number of line segments used to approximate the arc.
     */
    drawArc(center: IPos, radius: import('./double').double, angleDeg: import('./double').double, segments: import('./int').int): IPos[];
    /**
     * Draws a simple arc and rotates it around its center.
     *
     * <p>Base arc lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    drawArc(center: IPos, radius: import('./double').double, angleDeg: import('./double').double, segments: import('./int').int, pitchDeg: import('./double').double, yawDeg: import('./double').double, rollDeg: import('./double').double): IPos[];
    /**
     * Draws a ring (full circle) centered at {@code center} in the XZ plane.
     *
     * @param center   Ring center.
     * @param radius   Ring radius.
     * @param segments Number of segments used to approximate the ring.
     */
    drawRing(center: IPos, radius: import('./double').double, segments: import('./int').int): IPos[];
    /**
     * Draws a ring and rotates it around its center.
     *
     * <p>Base ring lies in the XZ plane, then is rotated by X (pitch), Y (yaw), Z (roll).
     */
    drawRing(center: IPos, radius: import('./double').double, segments: import('./int').int, pitchDeg: import('./double').double, yawDeg: import('./double').double, rollDeg: import('./double').double): IPos[];
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
    drawLissajous(center: IPos, amplitudeX: import('./double').double, amplitudeY: import('./double').double, amplitudeZ: import('./double').double, freqX: import('./double').double, freqY: import('./double').double, freqZ: import('./double').double, phaseDeg: import('./double').double, points: import('./int').int): IPos[];
    /**
     * Draws a straight line between two points.
     *
     * @param start  Start point.
     * @param end    End point.
     * @param points Number of sampled points including endpoints.
     */
    drawLine(start: IPos, end: IPos, points: import('./int').int): IPos[];
    /**
     * Draws a Bezier curve of arbitrary degree.
     *
     * @param controlPoints Bezier control points (2+).
     * @param points        Number of sampled points including endpoints.
     */
    drawBezier(controlPoints: IPos[], points: import('./int').int): IPos[];
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
    drawSpiral(center: IPos, radiusStart: import('./double').double, radiusEnd: import('./double').double, height: import('./double').double, turns: import('./double').double, angleOffset: import('./double').double, points: import('./int').int): IPos[];
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
    drawHelix(referencePos: IPos, radius: import('./double').double, heightOffset: import('./double').double, height: import('./double').double, compression: import('./double').double, angleOffset: import('./double').double, numPoints: import('./int').int): IPos[];
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
    drawMultiHelix(referencePos: IPos, radius: import('./double').double, heightOffset: import('./double').double, height: import('./double').double, compression: import('./double').double, angleOffset: import('./double').double, pointsPerHelix: import('./int').int, numHelices: import('./int').int): IPos[];
    /**
     * Rotates a set of points around an explicit pivot.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    rotatePoints(points: IPos[], pivot: IPos, pitchDeg: import('./double').double, yawDeg: import('./double').double, rollDeg: import('./double').double): IPos[];
    /**
     * Rotates a set of points around their centroid.
     *
     * <p>Rotation order is X (pitch), then Y (yaw), then Z (roll), with all angles in degrees.
     */
    rotatePoints(points: IPos[], pitchDeg: import('./double').double, yawDeg: import('./double').double, rollDeg: import('./double').double): IPos[];
}
