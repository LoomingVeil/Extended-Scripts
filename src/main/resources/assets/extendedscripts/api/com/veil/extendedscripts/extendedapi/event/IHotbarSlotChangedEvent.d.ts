import { IItemStack } from "./../../../../../noppes/npcs/api/item/IItemStack";
import { IPlayerEvent } from "./../../../../../noppes/npcs/api/event/IPlayerEvent";


export interface IHotbarSlotChangedEvent extends IPlayerEvent {
	oldSlot: number;
	newSlot: number;
	oldStack: IItemStack;
	newStack: IItemStack;
	getOldSlot(): number;

	getNewSlot(): number;

	getOldStack(): IItemStack;

	getNewStack(): IItemStack;

}

