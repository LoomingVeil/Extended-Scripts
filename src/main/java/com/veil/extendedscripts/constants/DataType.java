package com.veil.extendedscripts.constants;

public class DataType {
    public static final DataType Instance = new DataType();
    public final int BYTE = 1;
    public final int SHORT = 2;
    public final int INT = 3;
    public final int LONG = 4;
    public final int FLOAT = 5;
    public final int DOUBLE = 6;
    public final int BYTE_ARRAY = 7;
    public final int STRING = 8;
    public final int TAG_LIST = 9;
    public final int COMPOUND = 10;
    public final int INTEGER_ARRAY = 11;

    public int valueOf(String type) {
        type = type.toUpperCase();
        switch(type) {
            case "BYTE":
                return 1;
            case "SHORT":
                return 2;
            case "INT":
                return 3;
            case "LONG":
                return 4;
            case "FLOAT":
                return 5;
            case "DOUBLE":
                return 6;
            case "BYTE_ARRAY":
                return 7;
            case "STRING":
                return 8;
            case "TAG_LIST":
                return 9;
            case "COMPOUND":
                return 10;
            case "INTEGER_ARRAY":
                return 11;
            default:
                return -1;
        }
    }
}
