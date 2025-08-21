package com.veil.extendedscripts.extendedapi.constants;

/**
 * This object stores attribute sections available to all scripting handlers through the "AttributeSection" keyword.
 */
public interface IAttributeSection {
    int BASE = 0;
    int MODIFIER = 1;
    int STATS = 2;
    int INFO = 3;
    int EXTRA = 4;

    /**
     * Gets the corresponding ordinal given a String representation.
     * @param value The name of the section as a string (e.g. "Base") not case sensitive.
     * @return The corresponding ordinal, or -1 if not found.
     */
    int getValue(String value);
}
