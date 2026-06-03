/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * Custom projectile update.
 *
 * @hookName customProjectileTick
  * @javaFqn com.veil.extendedscripts.extendedapi.event.ICustomProjectileTickEvent
*/
export interface ICustomProjectileTickEvent {
    getHookName(): String;
    getProjectile(): import('../entity/ICustomProjectile').ICustomProjectile;
    getID(): import('./int').int;
}
