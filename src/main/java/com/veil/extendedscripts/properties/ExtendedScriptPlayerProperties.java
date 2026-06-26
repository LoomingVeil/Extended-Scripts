package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.ScreenResolution;
import com.veil.extendedscripts.guis.VirtualFurnace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.NpcAPI;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ExtendedScriptPlayerProperties extends ExtendedScriptEntityProperties implements IExtendedEntityProperties {
    // Unique identifier for these properties
    public static final String PROPERTY_ID = ExtendedScripts.MODID + "_PlayerProperties";
    private final EnumMap<PlayerAttribute, Object> playerAttributes = new EnumMap<>(PlayerAttribute.class);
    private final EntityPlayer player;
    private VirtualFurnace virtualFurnace;
    private Map<String, ItemStack> attributeCores = new HashMap<>(); // Multiple attribute cores (groups)
    private boolean canFly = false;
    private boolean lastSeenFlying = false;
    public ScreenResolution screenResolution = new ScreenResolution();
    public Map<String, Float> attributeClipboard;
    public ItemStack[] tempInvStorage;
    public int xpLevel;
    public int xpTotal;
    public float xp;
    public int score;

    public ExtendedScriptPlayerProperties(EntityPlayer player) {
        super(player);
        this.player = player;
        playerAttributes.put(PlayerAttribute.CAN_FLY, PlayerAttribute.CAN_FLY.getDefaultValue());
        playerAttributes.put(PlayerAttribute.LAST_SEEN_FLYING, PlayerAttribute.LAST_SEEN_FLYING.getDefaultValue());
        playerAttributes.put(PlayerAttribute.KEEP_INVENTORY, PlayerAttribute.KEEP_INVENTORY.getDefaultValue());
    }

    private ItemStack createNewAttributeCore() {
        ItemStack newCore = new ItemStack(ExtendedScripts.attributeCore);

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

        // Save attributeCores map
        NBTTagCompound coresTag = new NBTTagCompound();
        for (Map.Entry<String, ItemStack> entry : attributeCores.entrySet()) {
            NBTTagCompound coreTag = new NBTTagCompound();
            entry.getValue().writeToNBT(coreTag);
            coresTag.setTag(entry.getKey(), coreTag);
        }
        savedNBT.setTag("attributeCores", coresTag);

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

            // Load attributeCores map
            attributeCores.clear();
            if (savedNBT.hasKey("attributeCores")) {
                NBTTagCompound coresTag = savedNBT.getCompoundTag("attributeCores");
                for (Object groupName : coresTag.func_150296_c()) {
                    NBTTagCompound coreTag = coresTag.getCompoundTag((String) groupName);
                    ItemStack core = ItemStack.loadItemStackFromNBT(coreTag);
                    if (core != null) {
                        attributeCores.put((String) groupName, core);
                    }
                }
            }

            for (PlayerAttribute attr : playerAttributes.keySet()) {
                if (savedNBT.hasKey(attr.asCamelCase())) {
                    if (attr.getType() == Float.class) {
                        playerAttributes.put(attr, savedNBT.getFloat(attr.asCamelCase()));
                    } else if (attr.getType() == Boolean.class) {
                        playerAttributes.put(attr, savedNBT.getBoolean(attr.asCamelCase()));
                    }
                }
            }
        }
    }

    public void syncToClient() {
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
        if (!playerAttributes.containsKey(attr)) {
            playerAttributes.put(attr, attr.getDefaultValue());
        }
        return (T) playerAttributes.get(attr);
    }

    // Generic setter
    public <T> void set(PlayerAttribute attr, T value) {
        if (!attr.getType().isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for " + attr + ". Expected " + attr.getType());
        }
        playerAttributes.put(attr, value);
    }

    public boolean getLastSeenFlying() {
        return lastSeenFlying;
    }

    public void setLastSeenFlying(boolean lastSeenFlying) {
        // System.out.println("Changing last seen flying from " + this.lastSeenFlying + " to " + lastSeenFlying);
        if (this.lastSeenFlying == lastSeenFlying) return;
        this.lastSeenFlying = lastSeenFlying;
        syncToClient();
    }

    // Group-based core attribute methods

    public void setCoreAttribute(String group, String key, float value) {
        // Check if attribute exists before setting
        if (!ExtendedAPI.Instance.attributeExists(key)) {
            throw new IllegalArgumentException("Attribute "+key+" does not exist");
        }

        ItemStack core = attributeCores.get(group);
        if (core == null) {
            core = createNewAttributeCore();
            attributeCores.put(group, core);
        }

        NBTTagCompound root = core.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        attributes.setFloat(key, value);

        rpgCore.setTag("Attributes", attributes);
        root.setTag("RPGCore", rpgCore);
        core.setTagCompound(root);

        IPlayer npcPlayer = (IPlayer) NpcAPI.Instance().getIEntity(player);
        npcPlayer.getAttributes().recalculate(npcPlayer);
    }

    public void removeCoreAttribute(String group, String key) {
        if (!ExtendedAPI.Instance.attributeExists(key)) {
            throw new IllegalArgumentException("Attribute "+key+" does not exist");
        }
        ItemStack core = attributeCores.get(group);
        if (core == null) {
            return;
        }

        NBTTagCompound root = core.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        if (!attributes.hasKey(key)) {
            return;
        }
        attributes.removeTag(key);

        rpgCore.setTag("Attributes", attributes);
        root.setTag("RPGCore", rpgCore);
        core.setTagCompound(root);

        IPlayer npcPlayer = (IPlayer) NpcAPI.Instance().getIEntity(player);
        npcPlayer.getAttributes().recalculate(npcPlayer);
    }

    public float getCoreAttribute(String group, String key) {
        if (!ExtendedAPI.Instance.attributeExists(key)) {
            throw new IllegalArgumentException("Attribute "+key+" does not exist");
        }
        ItemStack core = attributeCores.get(group);
        if (core == null) {
            return 0;
        }

        NBTTagCompound root = core.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        if (attributes.hasKey(key)) {
            return attributes.getFloat(key);
        } else {
            throw new IllegalArgumentException("Attribute "+key+" does not exist in group "+group);
        }
    }

    public boolean hasCoreAttribute(String group, String key) {
        if (!ExtendedAPI.Instance.attributeExists(key)) {
            throw new IllegalArgumentException("Attribute "+key+" does not exist");
        }
        ItemStack core = attributeCores.get(group);
        if (core == null) {
            return false;
        }

        NBTTagCompound root = core.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        return attributes.hasKey(key);
    }

    // Aggregated across all groups
    public float getCoreAttribute(String key) {
        if (!ExtendedAPI.Instance.attributeExists(key)) {
            throw new IllegalArgumentException("Attribute "+key+" does not exist");
        }
        float total = 0;
        for (ItemStack core : attributeCores.values()) {
            if (core != null && core.hasTagCompound()) {
                NBTTagCompound root = core.getTagCompound();
                NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
                NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");
                if (attributes.hasKey(key)) {
                    total += attributes.getFloat(key);
                }
            }
        }
        return total;
    }

    public void removeGroup(String group) {
        if (attributeCores.containsKey(group)) {
            attributeCores.remove(group);
            IPlayer npcPlayer = (IPlayer) NpcAPI.Instance().getIEntity(player);
            npcPlayer.getAttributes().recalculate(npcPlayer);
        }
    }

    public String[] getGroups() {
        return attributeCores.keySet().toArray(new String[0]);
    }

    public ItemStack getAttributeCore(String group) {
        return attributeCores.get(group);
    }

    // Combined attribute core - aggregates all groups
    public ItemStack getWholeAttributeCore() {
        ItemStack combinedCore = createNewAttributeCore();
        NBTTagCompound root = combinedCore.getTagCompound();
        NBTTagCompound rpgCore = root.getCompoundTag("RPGCore");
        NBTTagCompound attributes = rpgCore.getCompoundTag("Attributes");

        // Aggregate attributes from all groups
        for (Map.Entry<String, ItemStack> entry : attributeCores.entrySet()) {
            ItemStack core = entry.getValue();
            if (core != null && core.hasTagCompound()) {
                NBTTagCompound coreRoot = core.getTagCompound();
                NBTTagCompound coreRpgCore = coreRoot.getCompoundTag("RPGCore");
                NBTTagCompound coreAttributes = coreRpgCore.getCompoundTag("Attributes");

                for (Object keyObj : coreAttributes.func_150296_c()) {
                    String key = (String) keyObj;
                    float value = coreAttributes.getFloat(key);
                    float currentValue = attributes.hasKey(key) ? attributes.getFloat(key) : 0;
                    attributes.setFloat(key, currentValue + value);
                }
            }
        }

        rpgCore.setTag("Attributes", attributes);
        root.setTag("RPGCore", rpgCore);
        combinedCore.setTagCompound(root);
        return combinedCore;
    }

    public void resetCoreAttributes() {
        this.attributeCores.clear();
        IPlayer npcPlayer = (IPlayer) NpcAPI.Instance().getIEntity(player);
        npcPlayer.getAttributes().recalculate(npcPlayer);
    }
}
