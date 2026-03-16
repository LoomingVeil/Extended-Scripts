/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi
 */

/**
 * This class stores a block's data. This data is not attached to a position, so if the source block is removed,
 * The data will not change.
  * @javaFqn com.veil.extendedscripts.extendedapi.IBlockData
*/
export interface IBlockData {
    getBlock(): Block;
    setBlock(block: Block): import('./void').void;
    setBlock(item: IItemStack): import('./void').void;
    setBlock(block: IBlock): import('./void').void;
    getTileNbt(): NBTTagCompound;
    setTileNbt(tileNbt: NBTTagCompound): import('./void').void;
    getMeta(): import('./int').int;
    setMeta(meta: import('./int').int): import('./void').void;
}
