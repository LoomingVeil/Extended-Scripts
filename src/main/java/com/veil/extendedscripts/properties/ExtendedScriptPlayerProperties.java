package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.guis.VirtualFurnace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedScriptPlayerProperties implements IExtendedEntityProperties {
    // Unique identifier for these properties
    public static final String PROPERTY_ID = ExtendedScripts.MODID + "_PlayerProperties";
    private final EntityPlayer player;
    private VirtualFurnace virtualFurnace;
    private float verticalFlightSpeed = 1;
    private float horizontalFlightSpeed = 1;
    private boolean canFly = false;
    private boolean lastSeenFlying = false;

    public ExtendedScriptPlayerProperties(EntityPlayer player) {
        this.player = player;
    }

    // Static helper to register properties onto a player
    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(PROPERTY_ID, new ExtendedScriptPlayerProperties(player));
    }

    // Static helper to retrieve properties from a player
    public static ExtendedScriptPlayerProperties get(EntityPlayer player) {
        return (ExtendedScriptPlayerProperties) player.getExtendedProperties(PROPERTY_ID);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound savedNBT = new NBTTagCompound();
        if (this.virtualFurnace != null) {
            this.virtualFurnace.writeToNBT(savedNBT); // Save the furnace's state to a sub-tag
        }
        savedNBT.setFloat("VerticalFlightSpeed", this.verticalFlightSpeed);
        savedNBT.setFloat("HorizontalFlightSpeed", this.horizontalFlightSpeed);
        savedNBT.setBoolean("CanFly", this.canFly);
        savedNBT.setBoolean("LastSeenFlying", this.lastSeenFlying);
        compound.setTag("PlayerData", savedNBT); // Store the sub-tag in the main compound
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("PlayerData", 10)) {
            NBTTagCompound savedNBT = compound.getCompoundTag("PlayerData");
            this.virtualFurnace = new VirtualFurnace();
            this.virtualFurnace.readFromNBT(savedNBT); // Load the furnace's state from the sub-tag
            this.verticalFlightSpeed = savedNBT.getFloat("VerticalFlightSpeed");
            this.horizontalFlightSpeed = savedNBT.getFloat("HorizontalFlightSpeed");
            this.canFly = savedNBT.getBoolean("CanFly");
            this.lastSeenFlying = savedNBT.getBoolean("LastSeenFlying");
        }
    }

    public void syncToPlayer() {
        if (!this.player.worldObj.isRemote && this.player instanceof EntityPlayerMP) {
            PacketHandler.INSTANCE.sendTo(new PlayerPropertyUpdateMessage(this), (EntityPlayerMP) this.player);
        }
    }

    public void init(Entity entity, World world) {
        // No special initialization needed here as loadNBTData handles data loading
    }

    // --- Custom methods for our Virtual Furnace ---

    public VirtualFurnace getVirtualFurnace() {
        // If the furnace hasn't been loaded or created yet, create a new one.
        if (this.virtualFurnace == null) {
            this.virtualFurnace = new VirtualFurnace();
        }
        return this.virtualFurnace;
    }

    /**
     * Resets the furnace's contents and state. Used for non-persistent mode.
     */
    public void resetVirtualFurnace() {
        if (this.virtualFurnace != null) {
            for (int i = 0; i < this.virtualFurnace.getSizeInventory(); ++i) {
                this.virtualFurnace.setInventorySlotContents(i, null);
            }
            // Manually reset the burn/cook times, as they are not part of the inventory.
            this.virtualFurnace.setField(0, 0); // furnaceBurnTime
            this.virtualFurnace.setField(1, 0); // currentItemBurnTime
            this.virtualFurnace.setField(2, 0); // furnaceCookTime
        }
    }

    public boolean getCanFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        if (player.capabilities.allowFlying == canFly) {
            return;
        }
        this.canFly = canFly;
        if (!this.player.capabilities.isCreativeMode) {
            System.out.println(this.player.capabilities.isFlying);
            this.player.capabilities.allowFlying = canFly;

            if (!this.canFly) {
                this.player.capabilities.isFlying = false;
            }
            this.player.sendPlayerAbilities();
        }
        syncToPlayer();
    }

    public float getVerticalFlightSpeed() {
        return verticalFlightSpeed;
    }

    public void setVerticalFlightSpeed(float verticalFlightSpeed) {
        this.verticalFlightSpeed = verticalFlightSpeed;
        syncToPlayer();
    }

    public float getHorizontalFlightSpeed() {
        return horizontalFlightSpeed;
    }

    public void setHorizontalFlightSpeed(float horizontalFlightSpeed) {
        // Value is modified by a factor of 20 for ease of use.
        // System.out.println("Set flight speed to "+horizontalFlightSpeed);
        this.horizontalFlightSpeed = horizontalFlightSpeed;
        if (Math.abs(player.capabilities.getFlySpeed() - horizontalFlightSpeed / 20) > 0.0001) {
            player.capabilities.setFlySpeed(horizontalFlightSpeed / 20);
            player.sendPlayerAbilities();
        }
        syncToPlayer();
    }

    public boolean getLastSeenFlying() {
        return lastSeenFlying;
    }

    public void setLastSeenFlying(boolean lastSeenFlying) {
        // System.out.println("Changing last seen flying from " + this.lastSeenFlying + " to " + lastSeenFlying);
        if (this.lastSeenFlying == lastSeenFlying) return;
        this.lastSeenFlying = lastSeenFlying;
        syncToPlayer();
    }
}
