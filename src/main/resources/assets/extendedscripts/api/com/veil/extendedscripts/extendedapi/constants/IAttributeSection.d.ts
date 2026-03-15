

/**
 * This object stores attribute sections available to all scripting handlers through the "AttributeSection" keyword.
 */
declare namespace AttributeSection {
	var BASE: number;
	var MODIFIER: number;
	var STATS: number;
	var INFO: number;
	var EXTRA: number;
	/**
	 * Gets the corresponding ordinal given a String representation.
	 * 
	 * @param value The name of the section as a string (e.g. "Base") not case sensitive.
	 * @return The corresponding ordinal, or -1 if not found.
	 */
	function getValue(value: string): number;

}

