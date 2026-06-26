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
    setCoreAttribute(group: String, key: String, value: import('./float').float): import('./void').void;
    modifyCoreAttribute(group: String, key: String, delta: import('./float').float): import('./void').void;
    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    getCoreAttribute(group: String, key: String): import('./float').float;
    getCoreAttribute(key: String): import('./float').float;
    removeCoreAttribute(group: String, key: String): import('./void').void;
    removeGroup(group: String): import('./void').void;
    getGroups(): String[];
    resetCoreAttributes(): import('./void').void;
    /**
     * Gets the attribute core as an item that can be given to the player.
     * Returns a combined core with all attributes from all groups.
     */
    getWholeAttributeCore(group: String): IItemStack;
    /**
     * Gets the attribute core as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     * Returns a combined core with all attributes from all groups.
     */
    getWholeAttributeCore(group: String, canBeRedeemed: import('./boolean').boolean): IItemStack;
    /**
     * Gets the attribute core for a specific group as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     */
    getAttributeCore(group: String, canBeRedeemed: import('./boolean').boolean): IItemStack;
    hasCoreAttribute(key: String): import('./boolean').boolean;
    getCoreAttributeKeys(group: String): String[];
    getAttributeKeys(): String[];
}
