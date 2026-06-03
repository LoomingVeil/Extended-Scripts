package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;

/**
 * Custom projectile update.
 *
 * @hookName customProjectileTick
 */
public interface ICustomProjectileTickEvent {
    String getHookName();

    ICustomProjectile getProjectile();

    int getID();
}
