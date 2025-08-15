package com.veil.extendedscripts.extendedapi.constants;

public interface IColorCodes {
    char BLACK = '0';
    char DARK_BLUE = '1';
    char DARK_GREEN = '2';
    char DARK_AQUA = '3';
    char DARK_RED = '4';
    char DARK_PURPLE = '5';
    char GOLD = '6';
    char GRAY = '7';
    char DARK_GRAY = '8';
    char BLUE = '9';
    char GREEN = 'a';
    char AQUA = 'b';
    char RED = 'c';
    char LIGHT_PURPLE = 'd';
    char YELLOW = 'e';
    char WHITE = 'f';

    /**
     * Gets the color code character for a given color name.
     * @param name The name of the color as a string (e.g., "Red") not case sensitive.
     * @return The corresponding color code character, or 'x' if not found.
     */
    char getValue(String name);
}
