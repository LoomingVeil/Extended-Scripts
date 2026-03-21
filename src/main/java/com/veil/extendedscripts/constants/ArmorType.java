package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractArmorType;

/**
 * This object stores armor types available to all scripting handlers through the "ArmorSlot" keyword.
 */
public class ArmorType implements AbstractArmorType {
    public static final ArmorType Instance = new ArmorType();
    public final int NONE = -2;
    public final int ALL = -1;
    public final int HEAD = 0;
    public final int CHEST = 1;
    public final int LEGS = 2;
    public final int FEET = 3;
}
