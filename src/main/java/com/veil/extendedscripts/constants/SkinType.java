package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.ISkinType;

public class SkinType implements ISkinType {
    public static SkinType Instance = new SkinType();
    public final int TEXTURE = 0;
    public final int PLAYER = 1;
    public final int URL = 2;
    public final int URL64 = 3;
}
