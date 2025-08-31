package com.veil.extendedscripts.extendedapi.entity;

import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;

public interface ICustomProjectile {
    /**
     * Turns the projectile to face the given position.
     */
    void moveToward(double towardX, double towardY, double towardZ, float speed);

    public void moveToward(IEntity entity, float speed);

    void placeInFrontOfEntity(IEntity entity, float distance);

    /**
     * Modifies the velocity. Velocity can not be zero if you need to check if the projectile is not moving,
     * Do something like if (proj.getVelocity() < 0.001. Any more zeroes and it may wrongly return false.
     * @param velocity
     */
    void setVelocity(float velocity);

    /**
     * Gets the entity's type. See {@link com.veil.extendedscripts.constants.EntityType}.
     * @return {@link com.veil.extendedscripts.constants.EntityType#PROJECTILE}
     */
    int getType();

    /**
     * Get properties related to the visual aspects of the projectile
     */
    ICustomProjectileRenderProperties getRenderProperties();

    boolean isPickupable();

    void setPickupable(boolean pickupable);

    IItemStack getPickupItem();

    void setPickupItem(IItemStack pickupItem);

    boolean doesShatterOnImpact();

    /**
     * This value only concerns block impacts.
     */
    void setShatterOnImpact(boolean shatterOnImpact);

    float getGravity();

    void setGravity(float gravity);

    float getInitialVelocity();

    /**
     * Change this value before spawning the entity and when it is spawned in, it will start with this velocity.
     */
    void setInitialVelocity(float initialVelocity);

    String getParticleTrail();

    /**
     * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
     */
    void setParticleTrail(String particleTrail);

    boolean getDoesVelocityAddDamage();

    /**
     * Normal projectiles like arrows scale damage based on velocity. If you want consistent damage, set to false.
     */
    void setDoesVelocityAddDamage(boolean doesVelocityAddDamage);

    String getShatterParticle();

    /**
     * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
     */
    void setShatterParticle(String shatterParticle);

    byte getInvulnerableCollisionBehavior();

    /**
     * For valid types see {@link com.veil.extendedscripts.constants.CustomProjectileInvulnerableCollisionType}
     */
    void setInvulnerableCollisionBehavior(byte invulnerableCollisionBehavior);

    int getPenetrationCount();

    /**
     * Acts just like the crossbow piercing enchantment.
     */
    void setPenetrationCount(int penetrationCount);

    String getHitSound();

    void setHitSound(String hitSound);

    double getProjectileDamage();

    void setProjectileDamage(double damage);

    int getKnockbackStrength();

    void setKnockbackStrength(int knockbackStrength);

    int getID();

    /**
     * Sets the ID. This ID is used in {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileImpactEvent}
     * and {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent}. Multiple projectiles can share ids.
     * ID will be 0 if not specified.
     */
    void setID(int id);

    IEntity getOwner();

    /**
     * Set the owner of the projectile. When the projectile does damage, it uses this entity for the {@link noppes.npcs.api.IDamageSource}.
     */
    void setOwner(IEntity owner);

    /**
     * For expert use only.
     */
    EntityCustomProjectile getMCEntity();

    /**
     * Due to certain limitations, you can not directly call IEntity's methods on ICustomProjectile.
     * Instead, you can call them on this object.
     */
    IEntity getIEntity();
}
