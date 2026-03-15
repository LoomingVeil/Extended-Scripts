import { IItemStack } from "./../../../../../noppes/npcs/api/item/IItemStack";
import { IPlayerEvent } from "./../../../../../noppes/npcs/api/event/IPlayerEvent";


export interface IArmorChangedEvent extends IPlayerEvent {
	getOldArmor(): IItemStack[];

	getNewArmor(): IItemStack[];

	wasChanged(slot: number): boolean;

	wasEquipped(slot: number): boolean;

	wasUnequipped(slot: number): boolean;

}

