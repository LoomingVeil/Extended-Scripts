/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi
 */

/**
 * @javaFqn noppes.npcs.extendedapi.IBlock
 */
export interface IBlock {
    getAsItem(): IItemStack;
    /**
     * Simulates generating the items that would drop from a block that exists in the world without using silk touch
     */
    getDrops(): IItemStack[];
    getDrops(fortune: import('./int').int): IItemStack[];
}
