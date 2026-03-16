package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractCombatPolicy;

public class CombatPolicy implements AbstractCombatPolicy {
    public static final CombatPolicy Instance = new CombatPolicy();
    public final int FLIP = 0;
    public final int BRUTE = 1;
    public final int STUBBORN = 2;
    public final int TACTICAL = 3;
}
