package com.veil.extendedscripts.mixins;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.IPos;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.ScriptWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptWorld.class})
public class MixinWorldExtension {
    @Shadow
    public WorldServer world;

    @Unique
    public boolean setAllBlocks(IPos[] positions, IBlock block) {
        Block MCBlock = block.getMCBlock();
        if (MCBlock == null) return false;

        for (int i = 0; i < positions.length; i++) {
            world.setBlock(positions[i].getX(), positions[i].getY(), positions[i].getZ(), MCBlock);
        }
        return true;
    }

    @Unique
    public boolean setAllBlocks(IPos[] positions, IItemStack item) {
        ItemStack MCItem = item.getMCItemStack();
        Block block = Block.getBlockFromItem(MCItem.getItem());
        if (block == null) return false;

        for (int i = 0; i < positions.length; i++) {
            world.setBlock(positions[i].getX(), positions[i].getY(), positions[i].getZ(), block);
        }
        return true;
    }

    @Unique
    public void removeAllBlocks(IPos[] positions) {
        for (int i = 0; i < positions.length; i++) {
            world.setBlockToAir(positions[i].getX(), positions[i].getY(), positions[i].getZ());
        }
    }

    @Unique
    public void spawnItem(IItemStack item, IPos pos) {
        ItemStack MCItem = item.getMCItemStack();
        EntityItem groundItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), MCItem);
        world.spawnEntityInWorld(groundItem);
    }
}
