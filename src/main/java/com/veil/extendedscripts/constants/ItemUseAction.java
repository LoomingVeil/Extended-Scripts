package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.IItemUseAction;

public class ItemUseAction implements IItemUseAction {
    public static final ItemUseAction Instance = new ItemUseAction();
    public final int NONE = 0;
    public final int BLOCK = 1;
    public final int BOW = 2;
    public final int EAT = 3;
    public final int DRINK = 4;
}
