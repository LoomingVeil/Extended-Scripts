/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.event.IHotbarSlotChangedEvent
 */
export interface IHotbarSlotChangedEvent extends IPlayerEvent {
    getOldSlot(): import('./int').int;
    getNewSlot(): import('./int').int;
    getOldStack(): IItemStack;
    getNewStack(): IItemStack;
}
