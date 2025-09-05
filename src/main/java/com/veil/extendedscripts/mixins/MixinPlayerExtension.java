package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.UpdateScreenSizePacket;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.PlayerAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IScreenSize;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.entity.ScriptPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptPlayer.class})
public class MixinPlayerExtension implements noppes.npcs.extendedapi.entity.IPlayer {
    @Shadow
    public EntityPlayerMP player;

    /**
     * @return Whether the player is currently flying
     */
    @Unique
    public boolean isFlying() {
        return player.capabilities.isFlying;
    }

    /**
     * @return Whether the player can fly
     */
    @Unique
    public boolean canFly() {
        return player.capabilities.allowFlying;
    }

    /**
     * Gives/Takes away the ability to fly. Doesn't impact those in creative mode.
     */
    @Unique
    public void setCanFly(boolean value) {
        ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(player);
        properties.set(PlayerAttribute.CAN_FLY, value);
    }

    public void forceFlyState(boolean value) {
        player.capabilities.isFlying = value;
        player.sendPlayerAbilities();
    }

    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    @Unique
    public float getFlightSpeed() {
        return getCoreAttribute(PlayerAttribute.FLIGHT_SPEED_HORIZONTAL.asSnakeCase());
    }

    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     * Sets the player's horizontal fly speed. Default is 1. See {@link #setVerticalFlightSpeed(float)} for vertical flight speed.
     */
    @Unique
    public void setFlightSpeed(float value) {
        setAttribute(PlayerAttribute.FLIGHT_SPEED_HORIZONTAL.asSnakeCase(), value);
    }

    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    @Unique
    public float getVerticalFlightSpeed() {
        return getCoreAttribute(PlayerAttribute.FLIGHT_SPEED_HORIZONTAL.asSnakeCase());
    }

    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     */
    @Unique
    public void setVerticalFlightSpeed(float value) {
        setAttribute(PlayerAttribute.FLIGHT_SPEED_VERTICAL.asSnakeCase(), value);
    }

    /**
     * Enables keep inventory per player. However, your items may be lost if you close the game between dying and respawning.
     * Use at your own risk.
     */
    @Unique
    public void setKeepInventory(boolean keepInventory) {
        ExtendedScripts.getPlayerProperties(player).set(PlayerAttribute.KEEP_INVENTORY, keepInventory);
    }

    @Unique
    public boolean getKeepInventory() {
        return ExtendedScripts.getPlayerProperties(player).get(PlayerAttribute.KEEP_INVENTORY);
    }

    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    @Unique
    public void setAttribute(String key, float value) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.setCoreAttribute(key, value);
    }

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    @Unique
    public float getCoreAttribute(String key) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        return props.getCoreAttribute(key);
    }

    @Unique
    public void resetCoreAttributes() {
        ExtendedScripts.getPlayerProperties(player).setAttributeCore(null);
    }

    /**
     * Gets the attribute core as an item that can be given to the player.
     */
    @Unique
    public IItemStack getAttributeCore() {
        return AbstractNpcAPI.Instance().getIItemStack(ExtendedScripts.getPlayerProperties(player).getAttributeCore());
    }

    @Unique
    public String[] getCoreAttributeKeys() {
        IItemStack attributeCore = getAttributeCore();
        return attributeCore.getCustomAttributeKeys();
    }

    /**
     * Only for those who know what they're doing
     */
    @Unique
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

    /**
     * Only for those who know what they're doing
     */
    @Unique
    public InventoryPlayer getMCInventory() {
        return player.inventory;
    }

    /**
     * @return A 40 slot container where (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    @Unique
    public IContainer getInventoryContainer() {
        InventoryPlayer inventory = getMCInventory();
        return AbstractNpcAPI.Instance().getIContainer(inventory);
    }

    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    @Unique
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

    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    @Unique
    public void setInventorySlot(int slot, IItemStack item) {
        IInventory inventory = getMCInventory();
        inventory.setInventorySlotContents(slot, item.getMCItemStack());
    }

    /**
     * Opens the player's ender chest for themselves.
     */
    @Unique
    public void openEnderChest() {
        player.displayGUIChest(player.getInventoryEnderChest());
    }

    /**
     * Opens a player's ender chest for a specified player
     * @param enderChestOwner The IPlayer whose ender chest is viewed
     */
    @Unique
    public void openEnderChest(IPlayer enderChestOwner) {
        EntityPlayerMP MCEnderChestOwner = (EntityPlayerMP) enderChestOwner.getMCEntity();
        player.displayGUIChest(MCEnderChestOwner.getInventoryEnderChest());
    }

    /**
     * Opens a crafting table. Any stored items are returned to the player when closed.
     */
    @Unique
    public void openCraftingTable() {
        World MCWorld = player.worldObj;
        player.openGui(ExtendedScripts.instance, ExtendedScripts.GUI_VIRTUAL_CRAFTING_TABLE, MCWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

    /**
     * Opens a furnace for the player
     * @param persistent When true the furnace will retain its items. Persistent and non-persistent furnaces are separate.
     */
    @Unique
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

    /**
     * Returns the specified player's playerdata in NBT form.
     */
    @Unique
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

    /**
     * Only works on server if at all.
     * @return Returns the permission level 0 - 4
     */
    @Unique
    public int getPermissionLevel() {
        for (int i = 4; i >= 0; i--) {
            if (player.canCommandSenderUseCommand(i, "")) {
                return i;
            }
        }
        return -1;
    }

    @Unique
    public boolean isOperator() {
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }

    /**
     * Due to CustomNPC+'s code, sometimes {@link IScreenSize#getHeight()} and {@link IScreenSize#getWidth()} will
     * return -1 until the screen size is changed. Extended Scripts tries to fix this issue, but if it is ever not
     * enough, this method can force an update.
     */
    @Unique
    public void resyncScreenSize() {
        PacketHandler.INSTANCE.sendTo(new UpdateScreenSizePacket(), player);
    }
}
