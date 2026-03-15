import { IObject } from "./../../../../../IObject";


/**
 * This object stores armor types available to all scripting handlers through the "ArmorSlot" keyword.
 */
export interface IArmorType extends IObject {
	NONE: number;
	ALL: number;
	HEAD: number;
	CHEST: number;
	LEGS: number;
	FEET: number;
}

