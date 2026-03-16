/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.event.IArmorChangedEvent
 */
export interface IArmorChangedEvent extends IPlayerEvent {
    getOldArmor(): IItemStack[];
    getNewArmor(): IItemStack[];
    wasChanged(slot: import('./int').int): import('./boolean').boolean;
    wasEquipped(slot: import('./int').int): import('./boolean').boolean;
    wasUnequipped(slot: import('./int').int): import('./boolean').boolean;
}
