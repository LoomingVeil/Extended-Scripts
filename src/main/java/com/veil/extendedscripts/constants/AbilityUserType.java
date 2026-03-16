package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityUserType;

public class AbilityUserType implements AbstractAbilityUserType {
    public static final AbilityUserType Instance = new AbilityUserType();
    public final int NPC_ONLY = 0;
    public final int PLAYER_ONLY = 1;
    public final int BOTH = 2;
    public final int NONE = 3;
}
