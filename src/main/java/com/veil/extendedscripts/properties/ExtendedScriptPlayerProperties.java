package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.constants.DataType;
import com.veil.extendedscripts.guis.VirtualFurnace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.EnumMap;

public class ExtendedScriptPlayerProperties extends ExtendedScriptEntityProperties implements IExtendedEntityProperties {
    // Unique identifier for these properties
    public static final String PROPERTY_ID = ExtendedScripts.MODID + "_PlayerProperties";
    private final EnumMap<PlayerAttribute, Object> playerAttributes = new EnumMap<>(PlayerAttribute.class);
    private final EntityPlayer player;
    private VirtualFurnace virtualFurnace;
    private ItemStack attributeCore; // Attributes added to the core apply to the player.
    private float verticalFlightSpeed = 1;
    private float horizontalFlightSpeed = 1;
    private boolean canFly = false;
    private boolean lastSeenFlying = false;

    public ExtendedScriptPlayerProperties(EntityPlayer player) {
        super(player);
        this.player = player;
        playerAttributes.put(PlayerAttribute.CAN_FLY, PlayerAttribute.CAN_FLY.getDefaultValue());
        playerAttributes.put(PlayerAttribute.LAST_SEEN_FLYING, PlayerAttribute.LAST_SEEN_FLYING.getDefaultValue());
        this.attributeCore = createNewAttributeCore();
    }

    private ItemStack createNewAttributeCore() {
        ItemStack newCore = new ItemStack(Items.nether_star);

        if (!newCore.hasTagCompound()) {
            newCore.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound root = newCore.getTagCompound();

        if (!root.hasKey("RPGCore")) {
            root.setTag("RPGCore", new NBTTagCompound());
        }

        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");

        if (!rpgCore.hasKey("Attributes")) {
            rpgCore.setTag("Attributes", new NBTTagCompound());
        }

        newCore.setTagCompound(root);
        return newCore;
    }

    public boolean doesCoreHaveAttributes(ItemStack core) {
        if (!core.hasTagCompound()) {
            core.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound root = core.getTagCompound();

        return root.hasKey("RPGCore");
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
            this.virtualFurnace.writeToNBT(savedNBT);
        }

        // Save attributeCore directly
        if (attributeCore != null) {
            NBTTagCompound coreTag = new NBTTagCompound();
            attributeCore.writeToNBT(coreTag);
            savedNBT.setTag("coreAttributes", coreTag);
        }

        for (PlayerAttribute attr : playerAttributes.keySet()) {
            if (attr.getType() == Float.class) {
                savedNBT.setFloat(attr.asCamelCase(), (float) playerAttributes.get(attr));
            } else if (attr.getType() == Boolean.class) {
                savedNBT.setBoolean(attr.asCamelCase(), (boolean) playerAttributes.get(attr));
            }
        }

        compound.setTag("extendedPlayerData", savedNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("extendedPlayerData", 10)) {
            NBTTagCompound savedNBT = compound.getCompoundTag("extendedPlayerData");
            this.virtualFurnace = new VirtualFurnace();
            this.virtualFurnace.readFromNBT(savedNBT);

            if (savedNBT.hasKey("coreAttributes")) {
                this.attributeCore = ItemStack.loadItemStackFromNBT(savedNBT.getCompoundTag("coreAttributes"));
            } else {
                this.attributeCore = createNewAttributeCore(); // fallback
            }

            for (PlayerAttribute attr : playerAttributes.keySet()) {
                if (savedNBT.hasKey(attr.asCamelCase(), DataType.Instance.valueOf(attr.getType().getTypeName()))) {
                    if (attr.getType() == Float.class) {
                        playerAttributes.put(attr, savedNBT.getFloat(attr.asCamelCase()));
                    } else if (attr.getType() == Boolean.class) {
                        playerAttributes.put(attr, savedNBT.getBoolean(attr.asCamelCase()));
                    }
                }
            }
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

    @SuppressWarnings("unchecked")
    public <T> T get(PlayerAttribute attr) {
        return (T) playerAttributes.get(attr);
    }

    // Generic setter
    public <T> void set(PlayerAttribute attr, T value) {
        if (!attr.getType().isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for " + attr + ". Expected " + attr.getType());
        }
        playerAttributes.put(attr, value);
    }

    public ItemStack getAttributeCore() {
        if (attributeCore == null || !doesCoreHaveAttributes(attributeCore)) {
            attributeCore = createNewAttributeCore();
        }
        NBTTagCompound nbt = new NBTTagCompound();
        attributeCore.writeToNBT(nbt);
        return attributeCore;
    }

    public void setAttributeCore(ItemStack stack) {
        this.attributeCore = stack != null ? stack : createNewAttributeCore();
        ExtendedAPI.Instance.updatePlayerAttributes(player);
    }

    public void setCoreAttribute(String key, float value) {
        NBTTagCompound root = attributeCore.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        attributes.setFloat(key, value);

        rpgCore.setTag("Attributes", attributes);
        root.setTag("RPGCore", rpgCore);
        attributeCore.setTagCompound(root);
        ExtendedAPI.Instance.updatePlayerAttributes(player);
    }

    public void removeCoreAttribute(String key) {
        NBTTagCompound root = attributeCore.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        if (!attributes.hasKey(key)) {
            return;
        }
        attributes.removeTag(key);

        rpgCore.setTag("Attributes", attributes);
        root.setTag("RPGCore", rpgCore);
        attributeCore.setTagCompound(root);
        ExtendedAPI.Instance.updatePlayerAttributes(player);
    }

    public float getCoreAttribute(String key) {
        NBTTagCompound root = attributeCore.getTagCompound();

        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        if (attributes.hasKey(key)) {
            return attributes.getFloat(key);
        } else {
            return 0;
        }
    }

    public void resetCoreAttributes() {
        this.attributeCore = createNewAttributeCore();
        ExtendedAPI.Instance.updatePlayerAttributes(player);
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
