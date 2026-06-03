/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * Fired when a player changes their screen resolution.
 *
 * @hookName resolutionChanged
  * @javaFqn com.veil.extendedscripts.extendedapi.event.IResolutionChangedEvent
*/
export interface IResolutionChangedEvent extends IPlayerEvent {
    getOldResolution(): import('../IScreenResolution').IScreenResolution;
    getNewResolution(): import('../IScreenResolution').IScreenResolution;
}
