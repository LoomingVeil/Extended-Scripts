package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractClaimType;

public class ClaimType implements AbstractClaimType {
    public static final ClaimType Instance = new ClaimType();
    public final int ITEM = 0;
    public final int CURRENCY = 1;
    public final int REFUND = 2;
}
