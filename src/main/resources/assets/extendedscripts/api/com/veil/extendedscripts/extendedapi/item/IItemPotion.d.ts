/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.item
 */

/**
 * Use this class to make custom potions.
 * To make an IPotionEffect see {@link com.veil.extendedscripts.extendedapi.AbstractExtendedAPI#getIPotionEffect(int, int, int)}
  * @javaFqn com.veil.extendedscripts.extendedapi.item.IItemPotion
*/
export interface IItemPotion {
    getEffects(): import('../IPotionEffect').IPotionEffect[];
    hasEffect(id: import('./int').int): import('./boolean').boolean;
    /**
     * Adds an effect to the potion if it does not already exist.
     */
    addEffect(effect: import('../IPotionEffect').IPotionEffect): import('./void').void;
    /**
     * Adds an effect to the potion and overrides it if it already exists.
     */
    setEffect(effect: import('../IPotionEffect').IPotionEffect): import('./void').void;
    removeEffect(id: import('./int').int): import('./void').void;
}
