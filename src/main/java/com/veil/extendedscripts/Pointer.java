package com.veil.extendedscripts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class Pointer extends Item {
    private static final String NBT_KEY = "PointerPoints";

    public Pointer() {
        this.setUnlocalizedName("pointer");
        this.setTextureName("extendedscripts:cursor");
        this.setCreativeTab(CustomItems.tab);
        this.setMaxStackSize(1);
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int unused) {
        return ExtendedScripts.SIGNATURE_COLOR;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("Right click to add a point");
        list.add("Shift + Right Click to clear points");
        list.add("Left click to save points to clipboard");
        int count = getPointsList(stack).tagCount();
        list.add("Currently storing "+count+" point" + (count == 1 ? "" : "s"));
    }

    // Right click in air or on block
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return stack;

        if (player.isSneaking()) {
            // Shift+right click: clear all points
            clearPoints(stack);
            player.addChatMessage(new ChatComponentText("§aCleared all stored points."));
        } else {
            // Ray trace to find the block the player is looking at (unlimited range)
            MovingObjectPosition mop = rayTraceFromPlayer(world, player);
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                if (hasPoint(stack, x, y, z)) {
                    removePoint(stack, x, y, z);
                    player.addChatMessage(new ChatComponentText("§cRemoved point: (" + x + ", " + y + ", " + z + ")"));
                } else {
                    addPoint(stack, x, y, z);
                    player.addChatMessage(new ChatComponentText("§aAdded point: (" + x + ", " + y + ", " + z + ")"));
                }
            }
        }

        return stack;
    }

    public boolean onLeftClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            copyToClipboard(stack, player);
        }
        return true;
    }

    void copyToClipboard(ItemStack stack, EntityPlayer player) {
        NBTTagList points = getPointsList(stack);
        int count = points.tagCount();
        String text = formatPointsForClipboard(points);

        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        player.addChatMessage(new ChatComponentText("§a" + count + " point" + (count == 1 ? "" : "s") + " saved to clipboard."));
    }

    private String formatPointsForClipboard(NBTTagList points) {
        StringBuilder sb = new StringBuilder();
        sb.append("var points = [\n");
        for (int i = 0; i < points.tagCount(); i++) {
            NBTTagCompound entry = points.getCompoundTagAt(i);
            int x = entry.getInteger("x");
            int y = entry.getInteger("y");
            int z = entry.getInteger("z");
            sb.append("\t[").append(x).append(", ").append(y).append(", ").append(z).append("],\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private NBTTagList getPointsList(ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(NBT_KEY)) tag.setTag(NBT_KEY, new NBTTagList());
        return tag.getTagList(NBT_KEY, 10); // 10 = NBTTagCompound type
    }

    private boolean hasPoint(ItemStack stack, int x, int y, int z) {
        NBTTagList points = getPointsList(stack);
        for (int i = 0; i < points.tagCount(); i++) {
            NBTTagCompound entry = points.getCompoundTagAt(i);
            if (entry.getInteger("x") == x && entry.getInteger("y") == y && entry.getInteger("z") == z)
                return true;
        }
        return false;
    }

    private void addPoint(ItemStack stack, int x, int y, int z) {
        NBTTagList points = getPointsList(stack);
        NBTTagCompound entry = new NBTTagCompound();
        entry.setInteger("x", x);
        entry.setInteger("y", y);
        entry.setInteger("z", z);
        points.appendTag(entry);
        stack.getTagCompound().setTag(NBT_KEY, points);
    }

    private void removePoint(ItemStack stack, int x, int y, int z) {
        NBTTagList points = getPointsList(stack);
        NBTTagList newPoints = new NBTTagList();
        for (int i = 0; i < points.tagCount(); i++) {
            NBTTagCompound entry = points.getCompoundTagAt(i);
            if (!(entry.getInteger("x") == x && entry.getInteger("y") == y && entry.getInteger("z") == z))
                newPoints.appendTag(entry);
        }
        stack.getTagCompound().setTag(NBT_KEY, newPoints);
    }

    private void clearPoints(ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag(NBT_KEY, new NBTTagList());
    }

    private MovingObjectPosition rayTraceFromPlayer(World world, EntityPlayer player) {
        double range = 200.0;
        Vec3 start = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 look = player.getLookVec();
        Vec3 end = Vec3.createVectorHelper(
            start.xCoord + look.xCoord * range,
            start.yCoord + look.yCoord * range,
            start.zCoord + look.zCoord * range
        );
        return world.rayTraceBlocks(start, end);
    }
}
