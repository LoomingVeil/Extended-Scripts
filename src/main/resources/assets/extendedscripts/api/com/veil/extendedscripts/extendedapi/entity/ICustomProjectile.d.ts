/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.entity
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.entity.ICustomProjectile
 */
export interface ICustomProjectile {
    /**
     * Turns the projectile to face the given position.
     */
    moveToward(towardX: import('./double').double, towardY: import('./double').double, towardZ: import('./double').double, speed: import('./float').float): import('./void').void;
    moveToward(entity: IEntity, speed: import('./float').float): import('./void').void;
    placeInFrontOfEntity(entity: IEntity, distance: import('./float').float): import('./void').void;
    /**
     * Modifies the velocity. Velocity can not be zero if you need to check if the projectile is not moving,
     * Do something like if (proj.getVelocity() < 0.001. Any more zeroes and it may wrongly return false.
     * @param velocity
     */
    setVelocity(velocity: import('./float').float): import('./void').void;
    /**
     * Gets the entity's type. See {@link com.veil.extendedscripts.constants.EntityType}.
     * @return {@link com.veil.extendedscripts.constants.EntityType#PROJECTILE}
     */
    getType(): import('./int').int;
    /**
     * Get properties related to the visual aspects of the projectile
     */
    getRenderProperties(): import('./ICustomProjectileRenderProperties').ICustomProjectileRenderProperties;
    isPickupable(): import('./boolean').boolean;
    setPickupable(pickupable: import('./boolean').boolean): import('./void').void;
    getPickupItem(): IItemStack;
    setPickupItem(pickupItem: IItemStack): import('./void').void;
    doesShatterOnImpact(): import('./boolean').boolean;
    /**
     * This value only concerns block impacts.
     */
    setShatterOnImpact(shatterOnImpact: import('./boolean').boolean): import('./void').void;
    getGravity(): import('./float').float;
    setGravity(gravity: import('./float').float): import('./void').void;
    getInitialVelocity(): import('./float').float;
    /**
     * Change this value before spawning the entity and when it is spawned in, it will start with this velocity.
     */
    setInitialVelocity(initialVelocity: import('./float').float): import('./void').void;
    getParticleTrail(): String;
    /**
     * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
     */
    setParticleTrail(particleTrail: String): import('./void').void;
    getDoesVelocityAddDamage(): import('./boolean').boolean;
    /**
     * Normal projectiles like arrows scale damage based on velocity. If you want consistent damage, set to false.
     */
    setDoesVelocityAddDamage(doesVelocityAddDamage: import('./boolean').boolean): import('./void').void;
    getShatterParticle(): String;
    /**
     * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
     */
    setShatterParticle(shatterParticle: String): import('./void').void;
    getInvulnerableCollisionBehavior(): import('./byte').byte;
    /**
     * For valid types see {@link com.veil.extendedscripts.constants.CustomProjectileInvulnerableCollisionType}
     */
    setInvulnerableCollisionBehavior(invulnerableCollisionBehavior: import('./byte').byte): import('./void').void;
    getPenetrationCount(): import('./int').int;
    /**
     * Acts just like the crossbow piercing enchantment.
     */
    setPenetrationCount(penetrationCount: import('./int').int): import('./void').void;
    getHitSound(): String;
    setHitSound(hitSound: String): import('./void').void;
    getProjectileDamage(): import('./double').double;
    setProjectileDamage(damage: import('./double').double): import('./void').void;
    getKnockbackStrength(): import('./int').int;
    setKnockbackStrength(knockbackStrength: import('./int').int): import('./void').void;
    getID(): import('./int').int;
    /**
     * Sets the ID. This ID is used in {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileImpactEvent}
     * and {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent}. Multiple projectiles can share ids.
     * ID will be 0 if not specified.
     */
    setID(id: import('./int').int): import('./void').void;
    getOwner(): IEntity;
    /**
     * Set the owner of the projectile. When the projectile does damage, it uses this entity for the {@link noppes.npcs.api.IDamageSource}.
     */
    setOwner(owner: IEntity): import('./void').void;
    /**
     * For expert use only.
     */
    getMCEntity(): EntityCustomProjectile;
    /**
     * Due to certain limitations, you can not directly call IEntity's methods on ICustomProjectile.
     * Instead, you can call them on this object.
     */
    getIEntity(): IEntity;
}
