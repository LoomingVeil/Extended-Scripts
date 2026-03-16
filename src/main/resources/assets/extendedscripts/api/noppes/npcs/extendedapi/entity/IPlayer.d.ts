/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.entity
 */

/**
 * @javaFqn noppes.npcs.extendedapi.entity.IPlayer
 */
export interface IPlayer {
    /**
     * @return Whether the player is currently flying
     */
    isFlying(): import('./boolean').boolean;
    /**
     * @return Whether the player can fly
     */
    canFly(): import('./boolean').boolean;
    /**
     * Gives/Takes away the ability to fly. Doesn't impact those in creative mode.
     */
    setCanFly(value: import('./boolean').boolean): import('./void').void;
    forceFlyState(value: import('./boolean').boolean): import('./void').void;
    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    getFlightSpeed(): import('./float').float;
    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     * Sets the player's horizontal fly speed multiplier. Default is 0. See {@link #setVerticalFlightSpeed(float)} for vertical flight speed.
     */
    setFlightSpeed(value: import('./float').float): import('./void').void;
    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    getVerticalFlightSpeed(): import('./float').float;
    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     */
    setVerticalFlightSpeed(value: import('./float').float): import('./void').void;
    /**
     * Enables keep inventory per player. However, your items may be lost if you close the game between dying and respawning.
     * Use at your own risk.
     */
    setKeepInventory(keepInventory: import('./boolean').boolean): import('./void').void;
    getKeepInventory(): import('./boolean').boolean;
    /**
     * @deprecated use {@link noppes.npcs.extendedapi.handler.data.IPlayerAttributes#setCoreAttribute(String, float)}
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    setAttribute(key: String, value: import('./float').float): import('./void').void;
    /**
     * @deprecated use {@link noppes.npcs.extendedapi.handler.data.IPlayerAttributes#getCoreAttribute(String)}
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    getCoreAttribute(key: String): import('./float').float;
    /**
     * @deprecated use {@link IPlayerAttributes#resetCoreAttributes()}
     */
    resetCoreAttributes(): import('./void').void;
    /**
     * @deprecated use {@link IPlayerAttributes#getAttributeCore()}
     * Gets the attribute core as an item that can be given to the player.
     */
    getAttributeCore(): IItemStack;
}
