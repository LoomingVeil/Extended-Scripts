/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.handler.data
 */

/**
 * @javaFqn noppes.npcs.extendedapi.handler.data.IPlayerAttributes
 */
export interface IPlayerAttributes {
    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    setCoreAttribute(key: String, value: import('./float').float): import('./void').void;
    modifyCoreAttribute(key: String, delta: import('./float').float): import('./void').void;
    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    getCoreAttribute(key: String): import('./float').float;
    resetCoreAttributes(): import('./void').void;
    /**
     * Gets the attribute core as an item that can be given to the player.
     */
    getAttributeCore(): IItemStack;
    /**
     * Gets the attribute core as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     */
    getAttributeCore(canBeRedeemed: import('./boolean').boolean): IItemStack;
    hasCoreAttribute(key: String): import('./boolean').boolean;
    getCoreAttributeKeys(): String[];
    getAttributeKeys(): String[];
}
