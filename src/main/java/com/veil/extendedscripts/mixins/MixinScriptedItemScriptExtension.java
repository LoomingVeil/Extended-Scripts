package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedScripts;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.scripted.item.ScriptCustomItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

// Extends the scripting for scripted items.
@Mixin(value={ScriptCustomItem.class})
public class MixinScriptedItemScriptExtension implements IItemCustomizable {
    public ScriptCustomItem IItemStackItem = ((ScriptCustomItem)(Object)this);
    public ItemStack item = IItemStackItem.getMCItemStack();

    HashMap<String, Integer> harvestLevels = new HashMap<>();
    public Integer armorColor = ExtendedScripts.SIGNATURE_COLOR;
    public String armorTexture1 = "minecraft:textures/models/armor/iron_layer_1.png";
    public String armorTexture2 = "minecraft:textures/models/armor/iron_layer_2.png";
    private ResourceLocation armorResource1 = new ResourceLocation(armorTexture1);
    private ResourceLocation armorResource2 = new ResourceLocation(armorTexture2);

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void onInit(ItemStack item, CallbackInfo ci) {
        loadExtendedItemData();
    }

    @Unique
    public int getHarvestLevel(String toolClass) {
        return harvestLevels.getOrDefault(toolClass, -1);
    }

    /**
     * Sets the harvest level for a toolClass
     * @param toolClass vanilla tool classes include pickaxe, axe, shove, and hoe (hoe may only be in newer versions). Other mods may add their own toolClasses.
     * @param level -1: Nothing; Same as mining with a stick, 0: wood/gold tools, 1: stone tools, 2: iron tools, 3: diamond tools
     */
    @Unique
    public void setHarvestLevel(String toolClass, int level) {
        if (level > 0) {
            harvestLevels.put(toolClass, level);
        } else {
            harvestLevels.remove(toolClass);
        }

        saveExtendedItemData();
    }

    @Unique
    public int getArmorColor() {
        return armorColor;
    }

    @Unique
    public void setArmorColor(Integer armorColor) {
        this.armorColor = armorColor;
    }

    @Unique
    public String getArmorTexture1() {
        return armorTexture1;
    }

    @Unique
    public void setArmorTexture1(String armorTexture1) {
        this.armorTexture1 = armorTexture1;
        if (armorResource1 != null && !armorResource1.getResourcePath().equals(armorTexture1)) {
            armorResource1 = new ResourceLocation(armorTexture1);
        }

        saveExtendedItemData();
    }

    @Unique
    public String getArmorTexture2() {
        return armorTexture2;
    }

    @Unique
    public void setArmorTexture2(String armorTexture2) {
        this.armorTexture2 = armorTexture2;
        if (armorResource2 != null && !armorResource2.getResourcePath().equals(armorTexture2)) {
            armorResource2 = new ResourceLocation(armorTexture2);
        }

        saveExtendedItemData();
    }

    @Unique
    public ResourceLocation getArmorResource(int slot) {
        if (slot == 0 || slot == 1 || slot == 3) { // Helmet, Chestplate, Boots
            return armorResource1;
        } else if (slot == 2) { // Leggings
            return armorResource2;
        }

        return null;
    }

    @Unique
    private NBTTagCompound getExtendedItemDataTag() {
        NBTTagCompound tag = item.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            item.setTagCompound(tag);
        }

        if (!tag.hasKey("ExtendedItemData")) {
            tag.setTag("ExtendedItemData", new NBTTagCompound());
        }
        return tag.getCompoundTag("ExtendedItemData");
    }

    @Unique
    public void saveExtendedItemData() {
        NBTTagCompound itemData = getExtendedItemDataTag();

        // Harvest levels
        if (!itemData.hasKey("harvestLevels")) {
            itemData.setTag("harvestLevels", new NBTTagCompound());
        }
        NBTTagCompound harvestLevelData = itemData.getCompoundTag("harvestLevels");
        for (String key : harvestLevels.keySet()) {
            harvestLevelData.setInteger(key, harvestLevels.get(key));
        }

        itemData.setInteger("armorColor", armorColor);
        itemData.setString("armorTexture1", armorTexture1);
        itemData.setString("armorTexture2", armorTexture2);
    }

    @Unique
    public void loadExtendedItemData() {
        NBTTagCompound itemData = getExtendedItemDataTag();

        if (!itemData.hasKey("harvestLevels")) {
            NBTTagCompound harvestLevelData = itemData.getCompoundTag("harvestLevels");
            for (String key : harvestLevelData.func_150296_c()) {
                harvestLevels.put(key, harvestLevelData.getInteger(key));
            }
        }

        this.armorColor = itemData.getInteger("armorColor");
        setArmorTexture1(itemData.getString("armorTexture1"));
        setArmorTexture2(itemData.getString("armorTexture2"));
    }
}
