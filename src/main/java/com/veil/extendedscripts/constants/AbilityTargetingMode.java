package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityTargetingMode;

public class AbilityTargetingMode implements AbstractAbilityTargetingMode {
    public static final AbilityTargetingMode Instance = new AbilityTargetingMode();
    public final int AGGRO_TARGET = 0;
    public final int SELF = 1;
    public final int AOE_SELF = 2;
    public final int AOE_TARGET = 3;
}
