/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.event
 */

/**
 * Called when an equipped scripted item that has the armor_value attribute and is equipped in the armor or hand.
  * @javaFqn com.veil.extendedscripts.extendedapi.event.IArmorDamagedEvent
*/
export interface IArmorDamagedEvent extends IItemEvent {
    /**
     * Gets the amount of durability damage this attack would do to a normal item.
     */
    getItemDamage(): import('./int').int;
    getDamageSource(): IDamageSource;
    /**
     * Get what entity is wearing this item.
     */
    getEquippedOn(): IEntity;
    /**
     * 0-3 helmet -> boots. -1 for hand.
     */
    getSlot(): import('./int').int;
}
