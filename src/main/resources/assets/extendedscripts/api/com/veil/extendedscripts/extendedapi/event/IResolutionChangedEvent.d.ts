/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.event.IResolutionChangedEvent
 */
export interface IResolutionChangedEvent extends IPlayerEvent {
    getOldResolution(): import('../IScreenResolution').IScreenResolution;
    getNewResolution(): import('../IScreenResolution').IScreenResolution;
}
