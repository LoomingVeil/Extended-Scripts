package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractBaubleSlot;

public class BaubleSlot implements AbstractBaubleSlot {
    public static final BaubleSlot Instance = new BaubleSlot();
    public final int NONE = -2;
    public final int ALL = -1;
    public final int RING = 0;
    public final int AMULET = 1;
    public final int BELT = 2;
}
