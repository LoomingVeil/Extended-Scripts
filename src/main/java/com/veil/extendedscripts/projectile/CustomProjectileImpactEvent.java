package com.veil.extendedscripts.projectile;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import com.veil.extendedscripts.extendedapi.event.ICustomProjectileImpactEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.ScriptBlockPos;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class CustomProjectileImpactEvent extends PlayerEvent implements ICustomProjectileImpactEvent {
    public ICustomProjectile projectile;
    public int projectileID = 0;
    public IEntity target;
    public IBlock block;
    public boolean shattered;

    public CustomProjectileImpactEvent() {
        super(null);
        this.projectile = null;
        this.target = null;
        this.block = null;
        this.shattered = false;
    }

    public CustomProjectileImpactEvent(IPlayer player, ICustomProjectile projectile, Entity target, BlockPos pos, boolean shattered) {
        super(player);
        this.projectile = projectile;
        this.projectileID = projectile.getID();
        if (target != null) {
            this.target = NpcAPI.Instance().getIEntity(target);
        }
        if (pos != null) {
            IPos npcPos = new ScriptBlockPos(pos);
            IWorld mcWorld = NpcAPI.Instance().getIWorld(((EntityCustomProjectile) projectile).worldObj);
            this.block = NpcAPI.Instance().getIBlock(mcWorld, npcPos);
        }
        this.shattered = shattered;
    }

    public String getHookName() {
        return "customProjectileImpact";
    }

    public ICustomProjectile getProjectile() {
        return projectile;
    }

    public int getID() {
        return projectileID;
    }

    public IEntity getTarget() {
        return target;
    }

    public IBlock getBlock() {
        return block;
    }

    public boolean didShatter() {
        return shattered;
    }

    public boolean hitEntity() {
        return target != null;
    }

    public boolean hitBlock() {
        return block != null;
    }
}
