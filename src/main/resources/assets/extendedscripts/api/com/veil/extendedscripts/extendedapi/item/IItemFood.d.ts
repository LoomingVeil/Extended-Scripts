import { IObject } from "./../../../../../IObject";


export interface IItemFood extends IObject {
	getHungerRestored(): number;

	getSaturation(): number;

}

