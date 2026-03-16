package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractMouseButton;

public class MouseButton implements AbstractMouseButton {
    public static final MouseButton Instance = new MouseButton();
    public static final int SCROLL = -1;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int MIDDLE = 2;
    public static final int MB4 = 3;
    public static final int MB5 = 4;
}
