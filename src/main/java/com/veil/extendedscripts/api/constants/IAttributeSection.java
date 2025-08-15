package com.veil.extendedscripts.api.constants;
public interface IAttributeSection {
    public final int BASE = 0;
    public final int MODIFIER = 1;
    public final int STATS = 2;
    public final int INFO = 3;
    public final int EXTRA = 4;

    /**
     * Gets the corresponding ordinal given a String representation.
     * @param value The name of the section as a string (e.g. "Base") not case sensitive.
     * @return The corresponding ordinal, or -1 if not found.
     */
    public int getValue(String value);
}
