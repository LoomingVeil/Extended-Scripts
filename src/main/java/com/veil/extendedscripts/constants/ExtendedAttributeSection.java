package com.veil.extendedscripts.constants;


import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;

public class ExtendedAttributeSection {
    public final static ExtendedAttributeSection Instance = new ExtendedAttributeSection();
    public final int BASE = 0;
    public final int MODIFIER = 1;
    public final int STATS = 2;
    public final int INFO = 3;
    public final int EXTRA = 4;

    public static AttributeDefinition.AttributeSection toKamSection(int extendedAttribute) {
        return AttributeDefinition.AttributeSection.values()[extendedAttribute];
    }

    public static int toExtendedSection(AttributeDefinition.AttributeSection kamAttribute) {
        return kamAttribute.ordinal();
    }

    /**
     * Gets the corresponding ordinal given a String representation.
     * @param value The name of the section as a string (e.g., "Base") not case sensitive.
     * @return The corresponding ordinal, or -1 if not found.
     */
    public int getValue(String value) {
        switch (value.toUpperCase()) {
            case "BASE":
                return this.BASE;
            case "MODIFIER":
                return this.MODIFIER;
            case "STATS":
                return this.STATS;
            case "INFO":
                return this.INFO;
            case "EXTRA":
                return this.EXTRA;
            default:
                return -1;
        }
    }
}
