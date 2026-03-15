
/**
 * All {@link noppes.npcs.api.item.IItemStack} implement getType(). You can compare that result with this class's fields
 * to determine what kind of item it is.
 * This object is available to all scripting handlers through the "ItemType" keyword.
 */
declare namespace ItemType {
	var DEFAULT: number;
	var BOOK: number;
	var BLOCK: number;
	var SWORD: number;
	var ARMOR: number;
	var PLANTABLE: number;
	var SCRIPTED_ITEM: number;
	var FOOD: number;
	var POTION: number;
	var TOOL: number;
}

