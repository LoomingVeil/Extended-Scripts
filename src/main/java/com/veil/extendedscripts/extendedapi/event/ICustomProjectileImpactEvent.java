package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.entity.IEntity;

/**
 * Fired a custom projectile hits a mob or entity.
 *
 * @hookName customProjectileImpact
 */
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
