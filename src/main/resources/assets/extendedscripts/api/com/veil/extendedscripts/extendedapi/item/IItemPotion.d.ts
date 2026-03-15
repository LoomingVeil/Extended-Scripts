import { IPotionEffect } from "./../IPotionEffect";
import { IObject } from "./../../../../../IObject";


/**
 * Use this class to make custom potions.
 * To make an IPotionEffect see {@link com.veil.extendedscripts.extendedapi.AbstractExtendedAPI#getIPotionEffect(int, int, int)}
 */
export interface IItemPotion extends IObject {
	getEffects(): IPotionEffect[];

	hasEffect(id: number): boolean;

	/**
	 * Adds an effect to the potion if it does not already exist.
	 */
	addEffect(effect: IPotionEffect): void;

	/**
	 * Adds an effect to the potion and overrides it if it already exists.
	 */
	setEffect(effect: IPotionEffect): void;

	removeEffect(id: number): void;

}

