package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityAnchorPoint;

public class AbilityAnchorPoint implements AbstractAbilityAnchorPoint {
    public static final AbilityAnchorPoint Instance = new AbilityAnchorPoint();
    public final int FRONT = 0;
    public final int CENTER = 1;
    public final int RIGHT_HAND = 2;
    public final int LEFT_HAND = 3;
    public final int ABOVE_HEAD = 4;
    public final int CHEST = 5;
    public final int EYE = 6;
}
