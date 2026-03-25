package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractTimeAvailability;

public class TimeAvailability implements AbstractTimeAvailability {
    public static final TimeAvailability Instance = new TimeAvailability();
    public final int ALWAYS = 0;
    public final int DAY = 1;
    public final int NIGHT = 2;
}
