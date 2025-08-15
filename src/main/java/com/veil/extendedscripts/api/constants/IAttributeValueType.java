package com.veil.extendedscripts.api.constants;

public interface IAttributeValueType {
    public final int FLAT = 0;
    public final int PERCENT = 1;
    public final int MAGIC = 2;

    public int getValue(String value);
}
