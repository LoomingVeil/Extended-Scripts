package com.veil.extendedscripts.extendedapi.entity;

import com.veil.extendedscripts.CustomProjectileRenderProperties;
import com.veil.extendedscripts.EntityCustomProjectile;
import net.minecraft.entity.Entity;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;

public interface ICustomProjectile {
    void moveToward(double towardX, double towardY, double towardZ, float speed);

    void placeInFrontOfEntity(IEntity entity, float distance);

    void setVelocity(float velocity);

    int getType();

    CustomProjectileRenderProperties getRenderProperties();

    boolean isPickupable();

    void setPickupable(boolean pickupable);

    IItemStack getPickupItem();

    void setPickupItem(IItemStack pickupItem);

    boolean doesShatterOnImpact();

    void setShatterOnImpact(boolean shatterOnImpact);

    float getGravity();

    void setGravity(float gravity);

    float getInitialVelocity();

    void setInitialVelocity(float initialVelocity);

    String getParticleTrail();

    void setParticleTrail(String particleTrail);

    boolean getDoesVelocityAddDamage();

    void setDoesVelocityAddDamage(boolean doesVelocityAddDamage);

    String getShatterParticle();

    void setShatterParticle(String shatterParticle);

    byte getInvulnerableCollisionBehavior();

    void setInvulnerableCollisionBehavior(byte invulnerableCollisionBehavior);

    int getPenetrationCount();

    void setPenetrationCount(int penetrationCount);

    String getHitSound();

    void setHitSound(String hitSound);

    double getDamage();

    void setDamage(double damage);

    int getKnockbackStrength();

    void setKnockbackStrength(int knockbackStrength);

    IEntity getOwner();

    EntityCustomProjectile getMCEntity();
}
