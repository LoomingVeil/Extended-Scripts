package com.veil.extendedscripts.constants;

public class ColorCodes {
    public final static ColorCodes Instance = new ColorCodes();
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
    public char getValue(String name) {
        switch (name.toUpperCase()) {
            case "BLACK":
                return this.BLACK;
            case "DARK_BLUE":
                return this.DARK_BLUE;
            case "DARK_GREEN":
                return this.DARK_GREEN;
            case "DARK_AQUA":
                return this.DARK_AQUA;
            case "DARK_RED":
                return this.DARK_RED;
            case "DARK_PURPLE":
                return this.DARK_PURPLE;
            case "GOLD":
                return this.GOLD;
            case "GRAY":
                return this.GRAY;
            case "DARK_GRAY":
                return this.DARK_GRAY;
            case "BLUE":
                return this.BLUE;
            case "GREEN":
                return this.GREEN;
            case "AQUA":
                return this.AQUA;
            case "RED":
                return this.RED;
            case "LIGHT_PURPLE":
                return this.LIGHT_PURPLE;
            case "YELLOW":
                return this.YELLOW;
            case "WHITE":
                return this.WHITE;
            default:
                return 'x';
        }
    }
}
