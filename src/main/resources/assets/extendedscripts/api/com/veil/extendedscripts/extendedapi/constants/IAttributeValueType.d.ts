

/**
 * This object stores attribute value types available to all scripting handlers through the "AttributeValueType" keyword.
 */
declare namespace AttributeValueType {
	var FLAT: number;
	var PERCENT: number;
	var MAGIC: number;
	function getValue(value: string): number;

}

