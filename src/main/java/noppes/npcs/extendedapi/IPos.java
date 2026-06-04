package noppes.npcs.extendedapi;

public interface IPos {
    /**
     * Gets the magnitude (for vectors)
     */
    double getMagnitude();

    /**
     * Offsets the position in some direction some distance
     * @param direction the direction of travel
     * @param distance
     */
    noppes.npcs.api.IPos offset(noppes.npcs.api.IPos direction, double distance);

    noppes.npcs.api.IPos multiply(double scalar);

    double dotProduct(noppes.npcs.api.IPos other);

    /**
     * Rotates the vector horizontally around the world's Y-axis.
     */
    noppes.npcs.api.IPos rotateYaw(double angleDeg);

    /**
     * Tilts the vector up or down exclusively along the world's horizontal X-axis grid line.
     */
    noppes.npcs.api.IPos rotatePitch(double angleDeg);

    /**
     * Rotates the vector horizontally left or right relative to its own current viewpoint plane.
     */
    noppes.npcs.api.IPos rotateRelativeYaw(double angleDeg);

    /**
     * Tilts the vector up or down relative to its own current heading plane.
     */
    noppes.npcs.api.IPos rotateRelativePitch(double angleDeg);
}
