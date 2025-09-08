package com.veil.extendedscripts.extendedapi.constants;

/**
 * All {@link noppes.npcs.api.item.IItemStack} implement getType(). You can compare that result with this class's fields
 * to determine what kind of item it is.
 * This object is available to all scripting handlers through the "ItemType" keyword.
 */
public interface IItemType {
    public final int DEFAULT = 0;
    public final int BOOK = 1;
    public final int BLOCK = 2;
    public final int SWORD = 4;
    public final int ARMOR = 5;
    public final int PLANTABLE = 5;
    public final int SCRIPTED_ITEM = 6;
}
