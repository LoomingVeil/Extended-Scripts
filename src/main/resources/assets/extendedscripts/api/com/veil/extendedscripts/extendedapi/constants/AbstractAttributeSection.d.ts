/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.constants
 */

/**
 * This object stores attribute sections available to all scripting handlers through the "AttributeSection" keyword.
  * @javaFqn com.veil.extendedscripts.extendedapi.constants.AbstractAttributeSection
*/
export interface AbstractAttributeSection {
    /**
     * Gets the corresponding ordinal given a String representation.
     * @param value The name of the section as a string (e.g. "Base") not case sensitive.
     * @return The corresponding ordinal, or -1 if not found.
     */
    getValue(value: String): import('./int').int;
}
