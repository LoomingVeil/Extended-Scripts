package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAuctionStatus;

public class AuctionStatus implements AbstractAuctionStatus {
    public static final AuctionStatus Instance = new AuctionStatus();
    public final int ACTIVE = 0;
    public final int ENDED = 1;
    public final int CANCELLED = 2;
    public final int CLAIMED = 3;
}
