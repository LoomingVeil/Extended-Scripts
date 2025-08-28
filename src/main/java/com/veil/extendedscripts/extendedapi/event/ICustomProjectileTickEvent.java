package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;

public interface ICustomProjectileTickEvent {
    String getHookName();

    ICustomProjectile getProjectile();

    int getID();
}
