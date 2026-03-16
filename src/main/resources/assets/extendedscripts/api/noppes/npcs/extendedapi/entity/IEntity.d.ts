/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.entity
 */

/**
 * @javaFqn noppes.npcs.extendedapi.entity.IEntity
 */
export interface IEntity {
    /**
     * Sets the gravity of the entity (not players). Ex 0.1 for 10% gravity, 10 for 1000% gravity
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setGravity(value: import('./float').float): import('./void').void;
    getGravity(): import('./float').float;
    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setUpwardGravity(value: import('./float').float): import('./void').void;
    getUpwardGravity(): import('./float').float;
    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setDownwardGravity(value: import('./float').float): import('./void').void;
    getDownwardGravity(): import('./float').float;
    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setUnderwaterGravity(value: import('./float').float): import('./void').void;
    getUnderwaterGravity(): import('./float').float;
    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setUnderwaterUpwardGravity(value: import('./float').float): import('./void').void;
    getUnderwaterUpwardGravity(): import('./float').float;
    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setUnderwaterDownwardGravity(value: import('./float').float): import('./void').void;
    getUnderwaterDownwardGravity(): import('./float').float;
    /**
     * Sets the max fall distance before an entity (not players) takes fall damage. Default is 3.
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setMaxFallDistance(value: import('./float').float): import('./void').void;
    getMaxFallDistance(): import('./float').float;
    /**
     * Modify how high the entity (not players) goes when it jumps. Default is 1.
     * The effect is equivalent to having the jump boost effect of level (value - 1)
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    setJumpBoost(value: import('./float').float): import('./void').void;
    getJumpBoost(): import('./float').float;
    /**
     * Stores a string in the entity's stored data that can be reconstructed back into an item. Keys for storedItem functions are shared with storedData functions.
     */
    setStoredItem(key: String, item: IItemStack): import('./void').void;
    getStoredItem(key: String): Object;
    clearStoredItems(): import('./void').void;
    getStoredItemKeys(): String[];
    hasStoredItem(key: String): import('./boolean').boolean;
}
