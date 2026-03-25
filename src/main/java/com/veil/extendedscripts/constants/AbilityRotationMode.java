package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityRotationMode;

public class AbilityRotationMode implements AbstractAbilityRotationMode {
    public static final AbilityLockMode Instance = new AbilityLockMode();
    public final int FREE = 0;
    public final int LOCKED = 1;
    public final int TRACK = 2;
}
