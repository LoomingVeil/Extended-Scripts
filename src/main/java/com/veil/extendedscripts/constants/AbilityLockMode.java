package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityLockMode;

public class AbilityLockMode implements AbstractAbilityLockMode {
    public static final AbilityLockMode Instance = new AbilityLockMode();
    public final int NEVER = 0;
    public final int WINDUP = 1;
    public final int ACTIVE = 2;
    public final int WINDUP_AND_ACTIVE = 3;
}
