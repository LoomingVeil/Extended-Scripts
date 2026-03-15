import { IObject } from "./../../../../IObject";


export interface IScreenResolution extends IObject {
	getWidth(): number;

	getHeight(): number;

	getScale(): number;

	getWidthPercent(percent: number): number;

	getHeightPercent(percent: number): number;

}

