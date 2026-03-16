package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractTacticalVariant;

public class TacticalVariant implements AbstractTacticalVariant {
    public static final TacticalVariant Instance = new TacticalVariant();
    public final int RUSH = 0;
    public final int DODGE = 1;
    public final int SURROUND = 2;
    public final int HITNRUN = 3;
    public final int AMBUSH = 4;
    public final int STALK = 5;
    public final int NONE = 6;
}
