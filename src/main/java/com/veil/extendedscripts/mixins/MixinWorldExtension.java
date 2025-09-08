package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.BlockData;
import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.commands.InspectCommand;
import com.veil.extendedscripts.extendedapi.IBlockData;
import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.IPos;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.IWorld;
import noppes.npcs.scripted.ScriptWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptWorld.class})
public class MixinWorldExtension implements IWorld {
    @Shadow
    public WorldServer world;

    @Unique
    public boolean setAllBlocks(IPos[] positions, IBlock block) {
        Block MCBlock = block.getMCBlock();
        if (MCBlock == null) return false;

        for (IPos position : positions) {
            world.setBlock(position.getX(), position.getY(), position.getZ(), MCBlock);
        }
        return true;
    }

    @Unique
    public boolean setAllBlocks(IPos[] positions, IItemStack item) {
        ItemStack MCItem = item.getMCItemStack();
        Block block = Block.getBlockFromItem(MCItem.getItem());
        if (block == null) return false;

        for (IPos position : positions) {
            world.setBlock(position.getX(), position.getY(), position.getZ(), block);
        }
        return true;
    }

    @Unique
    public void setAllBlocks(IPos[] positions, BlockData state) {
        for (IPos position : positions) {
            setBlock(position, state);
        }
    }

    @Unique
    public void removeAllBlocks(IPos[] positions) {
        for (IPos position : positions) {
            world.setBlockToAir(position.getX(), position.getY(), position.getZ());
        }
    }

    @Unique
    public void setBlock(IPos pos, IBlockData state) {
        world.setBlock(pos.getX(), pos.getY(), pos.getZ(), state.getBlock());
        if (state.getMeta() != 0) {
            world.setBlockMetadataWithNotify(pos.getX(), pos.getY(), pos.getZ(), state.getMeta(), 1);
        }

        TileEntity destinationTileEntity = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
        if (destinationTileEntity != null) {
            NBTTagCompound tileEntityData = state.getTileNbt();
            destinationTileEntity.readFromNBT(tileEntityData);

            tileEntityData.setInteger("x", pos.getX());
            tileEntityData.setInteger("y", pos.getY());
            tileEntityData.setInteger("z", pos.getZ());
            world.setTileEntity(pos.getX(), pos.getY(), pos.getZ(), destinationTileEntity);
        }
    }

    @Unique
    public IBlockData getBlockData(IPos pos) {
        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        int meta = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());

        if (block == null) {
            return null;
        }

        NBTTagCompound tileEntityData = new NBTTagCompound();
        TileEntity sourceTileEntity = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());

        if (sourceTileEntity != null) {
            sourceTileEntity.writeToNBT(tileEntityData);
        }

        return new BlockData(block, tileEntityData, meta);
    }

    @Unique
    public IEntity spawnItem(IItemStack item, IPos pos) {
        ItemStack MCItem = item.getMCItemStack();
        EntityItem groundItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), MCItem);
        if (world.spawnEntityInWorld(groundItem)) {
            return AbstractNpcAPI.Instance().getIEntity(groundItem);
        }
        return null;
    }

    @Unique
    public void fireProjectile(ICustomProjectile projectile) {
        world.spawnEntityInWorld(projectile.getMCEntity());
    }

    @Unique
    public void fireProjectile(ICustomProjectile projectile, float velocity) {
        world.spawnEntityInWorld(projectile.getMCEntity());
    }
}
