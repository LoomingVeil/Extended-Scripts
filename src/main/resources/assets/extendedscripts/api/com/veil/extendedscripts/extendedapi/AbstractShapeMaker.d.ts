/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi
 */

/**
 * Provides a variant of methods for creating shapes.
 * TAKE NOTE: This class is a work in progress. Pyramid and cone shapes are known to have issues.
  * @javaFqn com.veil.extendedscripts.extendedapi.AbstractShapeMaker
*/
export interface AbstractShapeMaker {
    getBox(center: IPos, width: import('./int').int, length: import('./int').int, height: import('./int').int): IPos[];
    getBox(pos1: IPos, pos2: IPos): IPos[];
    getHollowBox(center: IPos, width: import('./int').int, length: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    getHollowBox(center: IPos, width: import('./int').int, length: import('./int').int, height: import('./int').int): IPos[];
    getHollowBox(pos1: IPos, pos2: IPos, thickness: import('./double').double): IPos[];
    getHollowBox(pos1: IPos, pos2: IPos): IPos[];
    getEllipsoid(center: IPos, sizeX: import('./int').int, sizeY: import('./int').int, sizeZ: import('./int').int): IPos[];
    getHollowEllipsoid(center: IPos, sizeX: import('./int').int, sizeY: import('./int').int, sizeZ: import('./int').int, thickness: import('./double').double): IPos[];
    getHollowEllipsoid(center: IPos, sizeX: import('./int').int, sizeY: import('./int').int, sizeZ: import('./int').int): IPos[];
    getSphere(center: IPos, radius: import('./int').int): IPos[];
    getHollowSphere(center: IPos, radius: import('./int').int): IPos[];
    getHollowSphere(center: IPos, radius: import('./int').int, thickness: import('./double').double): IPos[];
    getCylinder(center: IPos, radius: import('./int').int, height: import('./int').int): IPos[];
    getCylinder(pos1: IPos, pos2: IPos): IPos[];
    getHollowCylinder(center: IPos, radius: import('./int').int, height: import('./int').int): IPos[];
    getHollowCylinder(center: IPos, radius: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    getHollowCylinder(pos1: IPos, pos2: IPos): IPos[];
    getHollowCylinder(pos1: IPos, pos2: IPos, thickness: import('./double').double): IPos[];
    getPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int): IPos[];
    getPyramid(pos1: IPos, pos2: IPos): IPos[];
    getHollowPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    getHollowPyramid(center: IPos, baseWidth: import('./int').int, baseLength: import('./int').int, height: import('./int').int): IPos[];
    getCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int): IPos[];
    getCone(pos1: IPos, pos2: IPos): IPos[];
    getHollowCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int, thickness: import('./double').double): IPos[];
    getHollowCone(center: IPos, baseRadius: import('./int').int, height: import('./int').int): IPos[];
}
