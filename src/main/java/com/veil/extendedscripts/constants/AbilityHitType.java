package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityAnchorPoint;

public class AbilityHitType implements AbstractAbilityAnchorPoint {
    public static final AbilityHitType Instance = new AbilityHitType();
    public final int SINGLE_HIT = 0;
    public final int PIERCE = 1;
    public final int MULTI_HIT = 2;
}
