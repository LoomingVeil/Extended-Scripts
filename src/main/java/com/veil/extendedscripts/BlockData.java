package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.IBlockData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.item.IItemStack;

public class BlockData implements IBlockData {
    public Block block;
    public NBTTagCompound tileNbt;
    public int meta;

    public BlockData(Block block, NBTTagCompound tileNbt, int meta) {
        this.block = block;
        this.tileNbt = tileNbt;
        this.meta = meta;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlock(IItemStack item) throws Exception {
        if (item.getMCItemStack().getItem() instanceof ItemBlock) {
            this.block = Block.getBlockFromItem(item.getMCItemStack().getItem());
        } else {
            throw new Exception("Cannot convert non block item to block!");
        }
    }

    public void setBlock(IBlock block) {
        this.block = block.getMCBlock();
    }

    public NBTTagCompound getTileNbt() {
        return tileNbt;
    }

    public void setTileNbt(NBTTagCompound tileNbt) {
        this.tileNbt = tileNbt;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }
}
