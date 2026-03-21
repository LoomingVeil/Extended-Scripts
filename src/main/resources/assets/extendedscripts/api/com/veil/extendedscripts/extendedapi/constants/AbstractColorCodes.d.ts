/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.constants
 */

/**
 * This object stores colors available to all scripting handlers through the "Color" keyword.
 * The values stored here are the characters for each color without the formatting character.
  * @javaFqn com.veil.extendedscripts.extendedapi.constants.AbstractColorCodes
*/
export interface AbstractColorCodes {
    /**
     * Gets the color code character for a given color name.
     * @param name The name of the color as a string (e.g., "Red") not case sensitive.
     * @return The corresponding color code character, or 'x' if not found.
     */
    getValue(name: String): import('./char').char;
	/**
	 * Value is '0'
	 */
	BLACK: string;
	/**
	 * Value is '1'
	 */
	DARK_BLUE: string;
	/**
	 * Value is '2'
	 */
	DARK_GREEN: string;
	/**
	 * Value is '3'
	 */
	DARK_AQUA: string;
	/**
	 * Value is '4'
	 */
	DARK_RED: string;
	/**
	 * Value is '5'
	 */
	DARK_PURPLE: string;
	/**
	 * Value is '6'
	 */
	GOLD: string;
	/**
	 * Value is '7'
	 */
	GRAY: string;
	/**
	 * Value is '8'
	 */
	DARK_GRAY: string;
	/**
	 * Value is '9'
	 */
	BLUE: string;
	/**
	 * Value is 'a'
	 */
	GREEN: string;
	/**
	 * Value is 'b'
	 */
	AQUA: string;
	/**
	 * Value is 'c'
	 */
	RED: string;
	/**
	 * Value is 'd'
	 */
	LIGHT_PURPLE: string;
	/**
	 * Value is 'e'
	 */
	YELLOW: string;
	/**
	 * Value is 'f'
	 */
	WHITE: string;
}
