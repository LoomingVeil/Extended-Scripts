package com.veil.extendedscripts.mixins;

import com.google.common.collect.Lists;
import com.veil.extendedscripts.*;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.INbt;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.ICustomAttribute;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.handler.data.IPlayerAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value={PlayerAttributeTracker.class})
public abstract class MixinPlayerAttributeTracker implements IPlayerAttributes {
    @Shadow
    public abstract ICustomAttribute[] getAttributes();

    @Unique
    EntityPlayer player;

    @Inject(
        method = "recalcAttributes(Lnet/minecraft/entity/player/EntityPlayer;)V",
        at = @At("HEAD"),
        remap = false
    )
    private void onRecalcAttributes(EntityPlayer player, CallbackInfo ci) {
        this.player = player;
    }

    @ModifyVariable(
        method = "recalcAttributes(Lnet/minecraft/entity/player/EntityPlayer;)V",
        at = @At(
            value = "STORE",
            ordinal = 0
        ),
        index = 3,
        remap = false
    )
    private ItemStack[] modifyEquipmentArray(ItemStack[] original) {
        ArrayList<ItemStack> equipment = new ArrayList<>(Arrays.asList(original));
        if (CommonProxy.isBaublesPreset) {
            ItemStack[] baubles = BaublesCompatability.getPlayerBaubles(player);
            if (baubles != null) {
                for (int i = 0; i < 4; i++) {
                    if (baubles[i] != null) equipment.add(baubles[i]);
                }
            }
        }

        // Add all attribute cores to equipment list so base mod processes them
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        if (props != null) {
            String[] groups = props.getGroups();
            for (String group : groups) {
                ItemStack core = props.getAttributeCore(group);
                if (core != null) {
                    equipment.add(core);
                }
            }
        }

        if (Config.extraAttributeSlots.getStringList().length > 0) {
            IPlayer iPlayer = AbstractNpcAPI.Instance().getPlayer(player.getCommandSenderName());
            INbt playerNbt = iPlayer.getAllNbt();
            ArrayList<String> extraSlots = new ArrayList<>(Arrays.asList(Config.extraAttributeSlots.getStringList()));
            for (String slotLocation : extraSlots) {
                try {
                    if (slotLocation.contains(":")) {
                        String[] parts = slotLocation.split(":");
                        slotLocation = parts[0];
                        int slotIndex = Integer.parseInt(parts[1]);

                        Object returnedData = ExtendedAPI.traverseNbt(playerNbt, slotLocation.split("\\."));
                        if (returnedData.getClass().isArray()) {
                            for (Object slot : (Object[]) returnedData) {
                                INbt nbt = (INbt) slot;
                                List<String> keys = Arrays.asList(nbt.getKeys());
                                if (keys.contains("Slot") && nbt.getInteger("Slot") == slotIndex) {
                                    NBTTagCompound mcNbt = nbt.getMCNBT();
                                    ItemStack item = ItemStack.loadItemStackFromNBT(mcNbt);
                                    if (item != null) {
                                        equipment.add(item);
                                    }
                                }
                            }
                        } else {
                            throw new RuntimeException("Extra attribute slot " + slotLocation + " is not an array");
                        }
                    } else {
                        INbt slotNbt = (INbt) ExtendedAPI.traverseNbt(playerNbt, slotLocation.split("\\."));
                        NBTTagCompound mcNbt = slotNbt.getMCNBT();
                        ItemStack item = ItemStack.loadItemStackFromNBT(mcNbt);
                        if (item != null) {
                            equipment.add(item);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load item from NBT: " + slotLocation + ", " + e.getMessage());
                }
            }
        }


        return equipment.toArray(new ItemStack[0]);
    }

    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    @Unique
    public void setCoreAttribute(String group, String key, float value) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.setCoreAttribute(group, key, value);
    }

    @Unique
    public void modifyCoreAttribute(String group, String key, float delta) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.setCoreAttribute(group, key, props.getCoreAttribute(group, key) + delta);
    }

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    @Unique
    public float getCoreAttribute(String group, String key) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        return props.getCoreAttribute(group, key);
    }

    @Unique
    public float getCoreAttribute(String key) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        return props.getCoreAttribute(key);
    }

    @Unique
    public void removeCoreAttribute(String group, String key) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.removeCoreAttribute(group, key);
    }

    @Unique
    public void removeGroup(String group) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.removeGroup(group);
    }

    @Unique
    public String[] getGroups() {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        return props.getGroups();
    }

    @Unique
    public void resetCoreAttributes() {
        ExtendedScripts.getPlayerProperties(player).resetCoreAttributes();
    }

    /**
     * Gets the attribute core as an item that can be given to the player.
     * Returns a combined core with all attributes from all groups.
     */
    @Unique
    public IItemStack getWholeAttributeCore(String groupName) {
        IItemStack core = AbstractNpcAPI.Instance().getIItemStack(ExtendedScripts.getPlayerProperties(player).getWholeAttributeCore());
        core.getNbt().setBoolean("canBeRedeemed", false);
        core.getNbt().setString("attributeGroup", groupName);
        return core;
    }

    /**
     * Gets the attribute core as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     * Returns a combined core with all attributes from all groups.
     */
    @Unique
    public IItemStack getWholeAttributeCore(String groupName, boolean canBeRedeemed) {
        IItemStack core = AbstractNpcAPI.Instance().getIItemStack(ExtendedScripts.getPlayerProperties(player).getWholeAttributeCore());
        core.getNbt().setBoolean("canBeRedeemed", canBeRedeemed);
        core.getNbt().setString("attributeGroup", groupName);
        return core;
    }

    /**
     * Gets the attribute core for a specific group as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     */
    @Unique
    public IItemStack getAttributeCore(String group, boolean canBeRedeemed) {
        ItemStack core = ExtendedScripts.getPlayerProperties(player).getAttributeCore(group);
        if (core == null) {
            return null;
        }
        IItemStack itemCore = AbstractNpcAPI.Instance().getIItemStack(core);
        itemCore.getNbt().setBoolean("canBeRedeemed", canBeRedeemed);
        itemCore.getNbt().setString("attributeGroup", group);
        return itemCore;
    }

    @Unique
    public boolean hasCoreAttribute(String key) {
        return Arrays.asList(getAttributeKeys()).contains(key);
    }

    @Unique
    public String[] getCoreAttributeKeys(String group) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        ItemStack core = props.getAttributeCore(group);
        if (core == null) {
            return new String[0];
        }
        IItemStack attributeCore = AbstractNpcAPI.Instance().getIItemStack(core);
        return attributeCore.getCustomAttributeKeys();
    }

    @Unique
    public String[] getAttributeKeys() {
        ICustomAttribute[] attrs = getAttributes();
        String[] keys = new String[attrs.length];
        for (int i = 0; i < attrs.length; i++) {
            keys[i] = attrs[i].getAttribute().getKey();
        }

        return keys;
    }
}


