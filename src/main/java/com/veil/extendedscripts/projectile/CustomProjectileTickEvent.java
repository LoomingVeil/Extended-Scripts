package com.veil.extendedscripts.projectile;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class CustomProjectileTickEvent extends PlayerEvent implements ICustomProjectileTickEvent {
    public ICustomProjectile projectile;
    public int projectileID;

    public CustomProjectileTickEvent() {
        super(null);
        this.projectile = null;
    }

    public CustomProjectileTickEvent(IPlayer player, ICustomProjectile projectile) {
        super(player);
        this.projectile = projectile;
        this.projectileID = projectile.getID();
    }

    public String getHookName() {
        return "customProjectileTick";
    }

    public ICustomProjectile getProjectile() {
        return projectile;
    }

    public int getID() {
        return projectileID;
    }
}
