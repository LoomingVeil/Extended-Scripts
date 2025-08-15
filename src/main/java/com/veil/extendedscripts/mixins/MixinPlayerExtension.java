package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.ExtendedScripts;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.entity.ScriptPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptPlayer.class})
public class MixinPlayerExtension {
    @Shadow
    public EntityPlayerMP player;

    @Unique
    /**
     * @return Whether the player is currently flying
     */
    public boolean isFlying() {
        return player.capabilities.isFlying;
    }

    @Unique
    /**
     * @return Whether the player can fly
     */
    public boolean canFly() {
        return player.capabilities.allowFlying;
    }

    @Unique
    /**
     * Gives/Takes away the ability to fly. Doesn't impact those in creative mode.
     */
    public void setCanFly(boolean value) {
        ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(player);
        properties.setCanFly(value);
    }

    public void forceFlyState(boolean value) {
        player.capabilities.isFlying = value;
        player.sendPlayerAbilities();
    }

    @Unique
    public float getFlightSpeed() {
        return player.capabilities.getFlySpeed() * 20;
    }

    @Unique
    /**
     * Sets the player's horizontal fly speed. Default is 1. See {@link #setVerticalFlightSpeed(float)} for vertical flight speed.
     */
    public void setFlightSpeed(float value) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);

        props.setHorizontalFlightSpeed(value);
    }

    @Unique
    public float getVerticalFlightSpeed() {
        ExtendedScriptPlayerProperties props = ExtendedScriptPlayerProperties.get(player);

        return props.getVerticalFlightSpeed();
    }

    @Unique
    public void setVerticalFlightSpeed(float value) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);

        props.setVerticalFlightSpeed(value);
    }

    @Unique
    /**
     * Only for those who know what they're doing
     */
    public InventoryEnderChest getMCEnderChest() {
        return player.getInventoryEnderChest();
    }

    @Unique
    public IContainer getEnderChest() {
        return AbstractNpcAPI.Instance().getIContainer(getMCEnderChest());
    }

    @Unique
    public IItemStack getEnderChestSlot(int slot) {
        return AbstractNpcAPI.Instance().getIItemStack(getMCEnderChest().getStackInSlot(slot));
    }

    @Unique
    public void setEnderChest(IContainer contents) {
        IInventory enderChest = getMCEnderChest();
        IInventory invContents = contents.getMCInventory();

        for (int i = 0; i < Math.min(invContents.getSizeInventory(), enderChest.getSizeInventory()); i++) {
            enderChest.setInventorySlotContents(i, invContents.getStackInSlot(i));
        }
    }

    @Unique
    public void setEnderChest(IItemStack[] items) {
        IInventory enderChest = getMCEnderChest();

        for (int i = 0; i < Math.min(items.length, enderChest.getSizeInventory()); i++) {
            enderChest.setInventorySlotContents(i, items[i].getMCItemStack());
        }
    }

    @Unique
    public void setEnderChestSlot(int slot, IItemStack item) {
        IInventory enderChest = getMCEnderChest();
        enderChest.setInventorySlotContents(slot, item.getMCItemStack());
    }

    @Unique
    public void clearEnderChest() {
        IInventory enderChest = getMCEnderChest();
        for (int i = 0; i < enderChest.getSizeInventory(); i++) {
            enderChest.setInventorySlotContents(i, null);
        }
    }

    @Unique
    /**
     * Only for those who know what they're doing
     */
    public InventoryPlayer getMCInventory() {
        return player.inventory;
    }

    @Unique
    /**
     * @return A 40 slot container where (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    public IContainer getInventoryContainer() {
        InventoryPlayer inventory = getMCInventory();
        return AbstractNpcAPI.Instance().getIContainer(inventory);
    }

    @Unique
    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    public IItemStack getInventorySlot(int slot) {
        InventoryPlayer inventory = getMCInventory();
        return AbstractNpcAPI.Instance().getIContainer(inventory).getSlot(slot);
    }

    @Unique
    public void setInventory(IContainer contents) {
        IInventory inventory = getMCInventory();
        IInventory invContents = contents.getMCInventory();

        for (int i = 0; i < Math.min(invContents.getSizeInventory(), inventory.getSizeInventory()); i++) {
            inventory.setInventorySlotContents(i, invContents.getStackInSlot(i));
        }
    }

    @Unique
    public void setInventory(IItemStack[] items) {
        IInventory inventory = getMCInventory();

        for (int i = 0; i < Math.min(items.length, inventory.getSizeInventory()); i++) {
            inventory.setInventorySlotContents(i, items[i].getMCItemStack());
        }
    }

    @Unique
    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    public void setInventorySlot(int slot, IItemStack item) {
        IInventory inventory = getMCInventory();
        inventory.setInventorySlotContents(slot, item.getMCItemStack());
    }

    @Unique
    /**
     * Opens the player's ender chest for themselves.
     */
    public void openEnderChest() {
        player.displayGUIChest(player.getInventoryEnderChest());
    }

    @Unique
    /**
     * Opens a player's ender chest for a specified player
     * @param enderChestOwner The IPlayer whose ender chest is viewed
     */
    public void openEnderChest(IPlayer enderChestOwner) {
        EntityPlayerMP MCEnderChestOwner = (EntityPlayerMP) enderChestOwner.getMCEntity();
        player.displayGUIChest(MCEnderChestOwner.getInventoryEnderChest());
    }

    @Unique
    /**
     * Opens a crafting table. Any stored items are returned to the player when closed.
     * @param player The player to open the crafting table for
     */
    public void openCraftingTable() {
        World MCWorld = player.worldObj;
        player.openGui(ExtendedScripts.instance, ExtendedScripts.GUI_VIRTUAL_CRAFTING_TABLE, MCWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

    @Unique
    /**
     * Opens a furnace for the player
     * @param persistent When true the furnace will retain its items. Persistent and non-persistent furnaces are separate.
     */
    public void openFurnace(boolean persistent) {
        World MCWorld = player.worldObj;

        int ID = ExtendedScripts.GUI_VIRTUAL_FURNACE;
        if (persistent) ID = ExtendedScripts.GUI_PERSISTENT_VIRTUAL_FURNACE;

        player.openGui(ExtendedScripts.instance, ID, MCWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

    public void openAnvil() {
        World MCWorld = player.worldObj;
        player.openGui(ExtendedScripts.instance, ExtendedScripts.GUI_VIRTUAL_ANVIL, MCWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

    @Unique
    /**
     * Returns the specified player's playerdata in NBT form.
     */
    public INbt getPlayerData() {
        NBTTagCompound playerData = new NBTTagCompound();
        player.writeToNBT(playerData);

        return AbstractNpcAPI.Instance().getINbt(playerData);
    }

    @Unique
    public int getSelectedHotbarSlot() {
        INbt playerData = getPlayerData();
        return playerData.getInteger("SelectedItemSlot");
    }

    @Unique
    /**
     * Only works on server if at all.
     * @return Returns the permission level 0 - 4
     */
    public int getPermissionLevel() {
        for (int i = 4; i <= 0; i--) {
            if (player.canCommandSenderUseCommand(i, "")) {
                return i;
            }
        }
        return -1;
    }

    public boolean isOperator() {
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }

}
