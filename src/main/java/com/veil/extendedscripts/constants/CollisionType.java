package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractCollisionType;

public class CollisionType implements AbstractCollisionType {
    public static final CollisionType Instance = new CollisionType();
    public final int ALL = 0;
    public final int NONE = 1;
    public final int NPCS = 2;
    public final int PLAYERS = 3;
    public final int BOTH = 4;
}
