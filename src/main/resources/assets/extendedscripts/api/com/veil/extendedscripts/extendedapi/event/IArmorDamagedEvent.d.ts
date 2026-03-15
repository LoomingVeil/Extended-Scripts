import { IEntity } from "./../../../../../noppes/npcs/api/entity/IEntity";
import { IItemEvent } from "./../../../../../noppes/npcs/api/event/IItemEvent";
import { IDamageSource } from "./../../../../../noppes/npcs/api/IDamageSource";


/**
 * Called when an equipped scripted item that has the armor_value attribute and is equipped in the armor or hand.
 */
export interface IArmorDamagedEvent extends IItemEvent {
	/**
	 * Gets the amount of durability damage this attack would do to a normal item.
	 */
	getItemDamage(): number;

	getDamageSource(): IDamageSource;

	/**
	 * Get what entity is wearing this item.
	 */
	getEquippedOn(): IEntity;

	/**
	 * 0-3 helmet -> boots. -1 for hand.
	 */
	getSlot(): number;

}

