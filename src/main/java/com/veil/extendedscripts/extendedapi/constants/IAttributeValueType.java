package com.veil.extendedscripts.extendedapi.constants;

public interface IAttributeValueType {
    int FLAT = 0;
    int PERCENT = 1;
    int MAGIC = 2;

    int getValue(String value);
}
