import { IObject } from "./../../../../../IObject";
import { IEntity } from "./../../../../../noppes/npcs/api/entity/IEntity";
import { ICustomProjectileRenderProperties } from "./ICustomProjectileRenderProperties";
import { IItemStack } from "./../../../../../noppes/npcs/api/item/IItemStack";

import { EntityCustomProjectile } from "./../../../../../missingTypes";

export interface ICustomProjectile extends IObject {
	/**
	 * Turns the projectile to face the given position.
	 */
	moveToward(towardX: number, towardY: number, towardZ: number, speed: number): void;

	moveToward(entity: IEntity, speed: number): void;

	placeInFrontOfEntity(entity: IEntity, distance: number): void;

	/**
	 * Modifies the velocity. Velocity can not be zero if you need to check if the projectile is not moving,
	 * Do something like if (proj.getVelocity() < 0.001. Any more zeroes and it may wrongly return false.
	 * 
	 * @param velocity
	 */
	setVelocity(velocity: number): void;

	/**
	 * Gets the entity's type. See {@link com.veil.extendedscripts.constants.EntityType}.
	 * 
	 * @return {@link com.veil.extendedscripts.constants.EntityType#PROJECTILE}
	 */
	getType(): number;

	/**
	 * Get properties related to the visual aspects of the projectile
	 */
	getRenderProperties(): ICustomProjectileRenderProperties;

	isPickupable(): boolean;

	setPickupable(pickupable: boolean): void;

	getPickupItem(): IItemStack;

	setPickupItem(pickupItem: IItemStack): void;

	doesShatterOnImpact(): boolean;

	/**
	 * This value only concerns block impacts.
	 */
	setShatterOnImpact(shatterOnImpact: boolean): void;

	getGravity(): number;

	setGravity(gravity: number): void;

	getInitialVelocity(): number;

	/**
	 * Change this value before spawning the entity and when it is spawned in, it will start with this velocity.
	 */
	setInitialVelocity(initialVelocity: number): void;

	getParticleTrail(): string;

	/**
	 * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
	 */
	setParticleTrail(particleTrail: string): void;

	getDoesVelocityAddDamage(): boolean;

	/**
	 * Normal projectiles like arrows scale damage based on velocity. If you want consistent damage, set to false.
	 */
	setDoesVelocityAddDamage(doesVelocityAddDamage: boolean): void;

	getShatterParticle(): string;

	/**
	 * For valid particles see {@link com.veil.extendedscripts.constants.ParticleType}
	 */
	setShatterParticle(shatterParticle: string): void;

	getInvulnerableCollisionBehavior(): number;

	/**
	 * For valid types see {@link com.veil.extendedscripts.constants.CustomProjectileInvulnerableCollisionType}
	 */
	setInvulnerableCollisionBehavior(invulnerableCollisionBehavior: number): void;

	getPenetrationCount(): number;

	/**
	 * Acts just like the crossbow piercing enchantment.
	 */
	setPenetrationCount(penetrationCount: number): void;

	getHitSound(): string;

	setHitSound(hitSound: string): void;

	getProjectileDamage(): number;

	setProjectileDamage(damage: number): void;

	getKnockbackStrength(): number;

	setKnockbackStrength(knockbackStrength: number): void;

	getID(): number;

	/**
	 * Sets the ID. This ID is used in {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileImpactEvent}
	 * and {@link com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent}. Multiple projectiles can share ids.
	 * ID will be 0 if not specified.
	 */
	setID(id: number): void;

	getOwner(): IEntity;

	/**
	 * Set the owner of the projectile. When the projectile does damage, it uses this entity for the {@link noppes.npcs.api.IDamageSource}.
	 */
	setOwner(owner: IEntity): void;

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

