package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityTargetFilter;

public class AbilityTargetFilter implements AbstractAbilityTargetFilter {
    public static final AbilityTargetFilter Instance = new AbilityTargetFilter();
    public final int ALLIES = 0;
    public final int ENEMIES = 1;
    public final int ALL = 2;
}
