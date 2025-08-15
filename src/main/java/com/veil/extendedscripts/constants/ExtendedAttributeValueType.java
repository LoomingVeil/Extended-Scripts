package com.veil.extendedscripts.constants;

import kamkeel.npcs.controllers.data.attribute.AttributeValueType;

public class ExtendedAttributeValueType {
    public final static ExtendedAttributeValueType Instance = new ExtendedAttributeValueType();
    public final int FLAT = 0;
    public final int PERCENT = 1;
    public final int MAGIC = 2;

    public static AttributeValueType toKamAttribute(int extendedAttribute) {
        return AttributeValueType.values()[extendedAttribute];
    }

    public static int toExtendedAttribute(AttributeValueType kamAttribute) {
        return kamAttribute.ordinal();
    }

    /**
     * Gets the corresponding ordinal given a String representation.
     * @param value The name of the value type as a string (e.g., "Flat") not case sensitive.
     * @return The corresponding ordinal, or -1 if not found.
     */
    public int getValue(String value) {
        switch (value.toUpperCase()) {
            case "FLAT":
                return this.FLAT;
            case "PERCENT":
                return this.PERCENT;
            case "MAGIC":
                return this.MAGIC;
            default:
                return -1;
        }
    }

}
