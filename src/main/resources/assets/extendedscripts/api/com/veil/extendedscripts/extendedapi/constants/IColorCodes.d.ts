

/**
 * This object stores colors available to all scripting handlers through the "Color" keyword.
 * The values stored here are the characters for each color without the formatting character.
 */
declare namespace Color {
	var BLACK: string;
	var DARK_BLUE: string;
	var DARK_GREEN: string;
	var DARK_AQUA: string;
	var DARK_RED: string;
	var DARK_PURPLE: string;
	var GOLD: string;
	var GRAY: string;
	var DARK_GRAY: string;
	var BLUE: string;
	var GREEN: string;
	var AQUA: string;
	var RED: string;
	var LIGHT_PURPLE: string;
	var YELLOW: string;
	var WHITE: string;
	/**
	 * Gets the color code character for a given color name.
	 * 
	 * @param name The name of the color as a string (e.g., "Red") not case sensitive.
	 * @return The corresponding color code character, or 'x' if not found.
	 */
	function getValue(name: string): string;

}

