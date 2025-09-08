package com.veil.extendedscripts.extendedapi;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.item.IItemStack;

/**
 * This class stores a block's data. This data is not attached to a position, so if the source block is removed,
 * The data will not change.
 */
public interface IBlockData {
    Block getBlock();

    void setBlock(Block block);

    void setBlock(IItemStack item) throws Exception;

    void setBlock(IBlock block);

    NBTTagCompound getTileNbt();

    void setTileNbt(NBTTagCompound tileNbt);

    int getMeta();

    void setMeta(int meta);
}
