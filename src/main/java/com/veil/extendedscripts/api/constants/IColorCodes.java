package com.veil.extendedscripts.api.constants;

public interface IColorCodes {
    public final char BLACK = '0';
    public final char DARK_BLUE = '1';
    public final char DARK_GREEN = '2';
    public final char DARK_AQUA = '3';
    public final char DARK_RED = '4';
    public final char DARK_PURPLE = '5';
    public final char GOLD = '6';
    public final char GRAY = '7';
    public final char DARK_GRAY = '8';
    public final char BLUE = '9';
    public final char GREEN = 'a';
    public final char AQUA = 'b';
    public final char RED = 'c';
    public final char LIGHT_PURPLE = 'd';
    public final char YELLOW = 'e';
    public final char WHITE = 'f';

    /**
     * Gets the color code character for a given color name.
     * @param name The name of the color as a string (e.g., "Red") not case sensitive.
     * @return The corresponding color code character, or 'x' if not found.
     */
    public char getValue(String name);
}
