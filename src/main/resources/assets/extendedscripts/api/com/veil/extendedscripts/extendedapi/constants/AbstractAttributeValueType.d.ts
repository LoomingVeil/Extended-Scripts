/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.constants
 */

/**
 * This object stores attribute value types available to all scripting handlers through the "AttributeValueType" keyword.
  * @javaFqn com.veil.extendedscripts.extendedapi.constants.AbstractAttributeValueType
*/
export interface AbstractAttributeValueType {
    getValue(value: String): import('./int').int;
	/**
	 * Value is 0
	 */
	FLAT: number;
	/**
	 * Value is 1
	 */
	PERCENT: number;
	/**
	 * Value is 2
	 */
	MAGIC: number;
}
