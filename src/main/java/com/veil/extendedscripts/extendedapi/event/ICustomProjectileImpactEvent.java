package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.entity.IEntity;

public interface ICustomProjectileImpactEvent {
    String getHookName();

    ICustomProjectile getProjectile();

    int getID();

    IEntity getTarget();

    IBlock getBlock();

    boolean didShatter();

    boolean hitEntity();

    boolean hitBlock();
}
