/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * Fired a custom projectile hits a mob or entity.
 *
 * @hookName customProjectileImpact
  * @javaFqn com.veil.extendedscripts.extendedapi.event.ICustomProjectileImpactEvent
*/
export interface ICustomProjectileImpactEvent {
    getHookName(): String;
    getProjectile(): import('../entity/ICustomProjectile').ICustomProjectile;
    getID(): import('./int').int;
    getTarget(): IEntity;
    getBlock(): IBlock;
    didShatter(): import('./boolean').boolean;
    hitEntity(): import('./boolean').boolean;
    hitBlock(): import('./boolean').boolean;
}
