/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.item
 */

/**
 * @javaFqn noppes.npcs.extendedapi.item.IItemStack
 */
export interface IItemStack {
    setUnbreakable(value: import('./boolean').boolean): import('./void').void;
    getNumericalId(): import('./int').int;
    /**
     * Returns the texture of the item. This method is good for items and blocks with the same texture on all faces.
     * However, if you want all the textures from a multi textured block, see
     */
    getItemTexture(): String;
    /**
     * Returns the textures that make up a block
     */
    getBlockTexture(side: import('./int').int): String;
    getType(): import('./int').int;
}
