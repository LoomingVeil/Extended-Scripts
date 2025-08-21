package com.veil.extendedscripts.extendedapi.constants;

/**
 * This object stores attribute value types available to all scripting handlers through the "AttributeValueType" keyword.
 */
public interface IAttributeValueType {
    int FLAT = 0;
    int PERCENT = 1;
    int MAGIC = 2;

    int getValue(String value);
}
