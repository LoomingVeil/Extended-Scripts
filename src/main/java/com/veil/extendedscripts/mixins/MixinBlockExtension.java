package com.veil.extendedscripts.mixins;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.ScriptBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

@Mixin(value={ScriptBlock.class})
public class MixinBlockExtension {
    @Shadow
    public Block block;
    @Shadow
    public IWorld world;
    @Shadow
    public BlockPos pos;

    @Unique
    public IItemStack getAsItem() {
        return AbstractNpcAPI.Instance().getIItemStack(new ItemStack(Item.getItemFromBlock(block)));
    }

    @Unique
    /**
     * Simulates generating the items that would drop from a block that exists in the world without using silk touch
     */
    public IItemStack[] getDrops() {
        return getDrops(0);
    }

    @Unique
    public IItemStack[] getDrops(int fortune) {
        ArrayList<ItemStack> drops = block.getDrops(world.getMCWorld(), pos.getX(), pos.getY(), pos.getZ(), world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()), fortune);
        IItemStack[] ret = new IItemStack[drops.size()];
        for (int i = 0; i < drops.size(); i++) {
            ret[i] = AbstractNpcAPI.Instance().getIItemStack(drops.get(i));
        }
        return ret;
    }
}
