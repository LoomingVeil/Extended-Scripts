/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi
 */

/**
 * @javaFqn noppes.npcs.extendedapi.IPos
 */
export interface IPos {
    /**
     * Offsets the position in some direction some distance
     * @param direction the direction of travel
     * @param distance
     */
    offset(direction: noppes.npcs.api.IPos, distance: import('./double').double): noppes.npcs.api.IPos;
    multiply(scalar: import('./double').double): noppes.npcs.api.IPos;
    dotProduct(other: noppes.npcs.api.IPos): import('./double').double;
    /**
     * Rotates the vector horizontally around the world's Y-axis.
     */
    rotateYaw(angleDeg: import('./double').double): noppes.npcs.api.IPos;
    /**
     * Tilts the vector up or down exclusively along the world's horizontal X-axis grid line.
     */
    rotatePitch(angleDeg: import('./double').double): noppes.npcs.api.IPos;
    /**
     * Rotates the vector horizontally left or right relative to its own current viewpoint plane.
     */
    rotateRelativeYaw(angleDeg: import('./double').double): noppes.npcs.api.IPos;
    /**
     * Tilts the vector up or down relative to its own current heading plane.
     */
    rotateRelativePitch(angleDeg: import('./double').double): noppes.npcs.api.IPos;
}
