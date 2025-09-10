package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.IItemType;

public class ItemType implements IItemType {
    public static final ItemType Instance = new ItemType();
    public final int DEFAULT = 0;
    public final int BOOK = 1;
    public final int BLOCK = 2;
    public final int SWORD = 4;
    public final int ARMOR = 5;
    public final int PLANTABLE = 5;
    public final int SCRIPTED_ITEM = 6;
    public final int FOOD = 7;
    public final int POTION = 8;
    public final int TOOL = 9;
}
