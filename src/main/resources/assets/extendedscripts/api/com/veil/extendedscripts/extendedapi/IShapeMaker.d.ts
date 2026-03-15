import { IPos } from "./../../../../noppes/npcs/api/IPos";


/**
 * Provides a variant of methods for creating shapes.
 * TAKE NOTE: This class is a work in progress. Pyramid and cone shapes are known to have issues.
 */
declare namespace ShapeMaker {
	function getBox(center: IPos, width: number, length: number, height: number): IPos[];

	function getBox(pos1: IPos, pos2: IPos): IPos[];

	function getHollowBox(center: IPos, width: number, length: number, height: number, thickness: number): IPos[];

	function getHollowBox(center: IPos, width: number, length: number, height: number): IPos[];

	function getHollowBox(pos1: IPos, pos2: IPos, thickness: number): IPos[];

	function getHollowBox(pos1: IPos, pos2: IPos): IPos[];

	function getEllipsoid(center: IPos, sizeX: number, sizeY: number, sizeZ: number): IPos[];

	function getHollowEllipsoid(center: IPos, sizeX: number, sizeY: number, sizeZ: number, thickness: number): IPos[];

	function getHollowEllipsoid(center: IPos, sizeX: number, sizeY: number, sizeZ: number): IPos[];

	function getSphere(center: IPos, radius: number): IPos[];

	function getHollowSphere(center: IPos, radius: number): IPos[];

	function getHollowSphere(center: IPos, radius: number, thickness: number): IPos[];

	function getCylinder(center: IPos, radius: number, height: number): IPos[];

	function getCylinder(pos1: IPos, pos2: IPos): IPos[];

	function getHollowCylinder(center: IPos, radius: number, height: number): IPos[];

	function getHollowCylinder(center: IPos, radius: number, height: number, thickness: number): IPos[];

	function getHollowCylinder(pos1: IPos, pos2: IPos): IPos[];

	function getHollowCylinder(pos1: IPos, pos2: IPos, thickness: number): IPos[];

	function getPyramid(center: IPos, baseWidth: number, baseLength: number, height: number): IPos[];

	function getPyramid(pos1: IPos, pos2: IPos): IPos[];

	function getHollowPyramid(center: IPos, baseWidth: number, baseLength: number, height: number, thickness: number): IPos[];

	function getHollowPyramid(center: IPos, baseWidth: number, baseLength: number, height: number): IPos[];

	function getCone(center: IPos, baseRadius: number, height: number): IPos[];

	function getCone(pos1: IPos, pos2: IPos): IPos[];

	function getHollowCone(center: IPos, baseRadius: number, height: number, thickness: number): IPos[];

	function getHollowCone(center: IPos, baseRadius: number, height: number): IPos[];

}

