package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAbilityPhase;

public class AbilityPhase implements AbstractAbilityPhase {
    public static final AbilityPhase Instance = new AbilityPhase();
    public final int IDLE = 0;
    public final int WINDUP = 1;
    public final int ACTIVE = 2;
    public final int DAZED = 3;
    public final int BURST_DELAY = 4;
}
