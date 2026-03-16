/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.constants
 */

/**
 * This object stores colors available to all scripting handlers through the "Color" keyword.
 * The values stored here are the characters for each color without the formatting character.
  * @javaFqn com.veil.extendedscripts.extendedapi.constants.IColorCodes
*/
export interface IColorCodes {
    /**
     * Gets the color code character for a given color name.
     * @param name The name of the color as a string (e.g., "Red") not case sensitive.
     * @return The corresponding color code character, or 'x' if not found.
     */
    getValue(name: String): import('./char').char;
}
