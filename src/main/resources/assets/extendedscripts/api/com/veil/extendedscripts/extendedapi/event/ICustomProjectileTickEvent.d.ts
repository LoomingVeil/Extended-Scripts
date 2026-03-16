/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent
 */
export interface ICustomProjectileTickEvent {
    getHookName(): String;
    getProjectile(): import('../entity/ICustomProjectile').ICustomProjectile;
    getID(): import('./int').int;
}
