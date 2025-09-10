package com.veil.extendedscripts.extendedapi.constants;

/**
 * All {@link noppes.npcs.api.item.IItemStack} implement getType(). You can compare that result with this class's fields
 * to determine what kind of item it is.
 * This object is available to all scripting handlers through the "ItemType" keyword.
 */
public interface IItemType {
    int DEFAULT = 0;
    int BOOK = 1;
    int BLOCK = 2;
    int SWORD = 4;
    int ARMOR = 5;
    int PLANTABLE = 5;
    int SCRIPTED_ITEM = 6;
    int FOOD = 7;
    int POTION = 8;
    int TOOL = 9;
}
