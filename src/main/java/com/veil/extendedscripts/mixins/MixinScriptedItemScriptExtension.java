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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public String armorOverlayTexture1 = "";
    public String armorOverlayTexture2 = "";
    private ResourceLocation armorOverlayResource1 = null;
    private ResourceLocation armorOverlayResource2 = null;

    public boolean useFirstPersonOverrides = false;
    public Float fpTranslateX = 0.0F;
    public Float fpTranslateY = 0.0F;
    public Float fpTranslateZ = 0.0F;
    public Float fpScaleX = 1.0F;
    public Float fpScaleY = 1.0F;
    public Float fpScaleZ = 1.0F;
    public Float fpRotationX = 0.0F;
    public Float fpRotationY = 0.0F;
    public Float fpRotationZ = 0.0F;

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

        saveExtendedItemData();
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
    public String getArmorOverlayTexture1() {
        return armorOverlayTexture1;
    }

    @Unique
    public void setArmorOverlayTexture1(String armorOverlayTexture1) {
        this.armorOverlayTexture1 = armorOverlayTexture1;
        if (armorOverlayTexture1 == null) {
            armorOverlayResource1 = null;
        } else if (armorOverlayResource1 == null || !armorOverlayResource1.getResourcePath().equals(armorOverlayTexture1)) {
            armorOverlayResource1 = new ResourceLocation(armorOverlayTexture1);
        }

        saveExtendedItemData();
    }

    @Unique
    public String getArmorOverlayTexture2() {
        return armorOverlayTexture2;
    }

    @Unique
    public void setArmorOverlayTexture2(String armorOverlayTexture2) {
        this.armorOverlayTexture2 = armorOverlayTexture2;
        if (armorOverlayTexture2 == null) {
            armorOverlayResource2 = null;
        } else if (armorOverlayResource2 == null || !armorOverlayResource2.getResourcePath().equals(armorOverlayTexture2)) {
            armorOverlayResource2 = new ResourceLocation(armorOverlayTexture2);
        }

        saveExtendedItemData();
    }

    @Unique
    public ResourceLocation getArmorOverlayResource(int slot) {
        if (slot == 0 || slot == 1 || slot == 3) { // Helmet, Chestplate, Boots
            return armorOverlayResource1;
        } else if (slot == 2) { // Leggings
            return armorOverlayResource2;
        }

        return null;
    }

    public boolean usesFirstPersonOverrides() {
        return useFirstPersonOverrides;
    }

    public void setUseFirstPersonOverrides(boolean useFirstPersonOverrides) {
        this.useFirstPersonOverrides = useFirstPersonOverrides;
        saveExtendedItemData();
    }

    public Float getFirstPersonTranslateX() { return fpTranslateX; }
    public Float getFirstPersonTranslateY() { return fpTranslateY; }
    public Float getFirstPersonTranslateZ() { return fpTranslateZ; }

    public Float getFirstPersonScaleX() { return fpScaleX; }
    public Float getFirstPersonScaleY() { return fpScaleY; }
    public Float getFirstPersonScaleZ() { return fpScaleZ; }

    public Float getFirstPersonRotationX() { return fpRotationX; }
    public Float getFirstPersonRotationY() { return fpRotationY; }
    public Float getFirstPersonRotationZ() { return fpRotationZ; }

    public void setFirstPersonTranslate(Float x, Float y, Float z) {
        fpTranslateX = x;
        fpTranslateY = y;
        fpTranslateZ = z;
        saveExtendedItemData();
    }

    public void setFirstPersonScale(Float x, Float y, Float z) {
        fpScaleX = x;
        fpScaleY = y;
        fpScaleZ = z;
        saveExtendedItemData();
    }

    public void setFirstPersonRotation(Float x, Float y, Float z) {
        fpRotationX = x;
        fpRotationY = y;
        fpRotationZ = z;
        saveExtendedItemData();
    }

    @Unique
    public void saveExtendedItemData() {
        NBTTagCompound tag = item.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            item.setTagCompound(tag);
        }

        if (!tag.hasKey("ExtendedItemData")) {
            tag.setTag("ExtendedItemData", new NBTTagCompound());
        }
        NBTTagCompound itemData = tag.getCompoundTag("ExtendedItemData");

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

        itemData.setString("armorOverlayTexture1", armorOverlayTexture1);
        itemData.setString("armorOverlayTexture2", armorOverlayTexture2);

        itemData.setBoolean("useFirstPersonOverrides", useFirstPersonOverrides);

        itemData.setFloat("firstPersonTranslateX", fpTranslateX);
        itemData.setFloat("firstPersonTranslateY", fpTranslateY);
        itemData.setFloat("firstPersonTranslateZ", fpTranslateZ);

        itemData.setFloat("firstPersonScaleX", fpScaleX);
        itemData.setFloat("firstPersonScaleY", fpScaleY);
        itemData.setFloat("firstPersonScaleZ", fpScaleZ);

        itemData.setFloat("firstPersonRotationX", fpRotationX);
        itemData.setFloat("firstPersonRotationY", fpRotationY);
        itemData.setFloat("firstPersonRotationZ", fpRotationZ);
    }

    @Unique
    public void loadExtendedItemData() {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) return;

        if (!tag.hasKey("ExtendedItemData")) {
            return;
        }
        NBTTagCompound itemData = tag.getCompoundTag("ExtendedItemData");

        if (!itemData.hasKey("harvestLevels")) {
            NBTTagCompound harvestLevelData = itemData.getCompoundTag("harvestLevels");
            for (String key : harvestLevelData.func_150296_c()) {
                harvestLevels.put(key, harvestLevelData.getInteger(key));
            }
        }

        this.armorColor = itemData.getInteger("armorColor");

        this.armorTexture1 = itemData.getString("armorTexture1");
        if (armorResource1 != null && !armorResource1.getResourcePath().equals(armorTexture1)) {
            armorResource1 = new ResourceLocation(armorTexture1);
        }
        this.armorTexture2 = itemData.getString("armorTexture2");
        if (armorResource2 != null && !armorResource2.getResourcePath().equals(armorTexture2)) {
            armorResource2 = new ResourceLocation(armorTexture2);
        }

        this.armorOverlayTexture1 = itemData.getString("armorOverlayTexture1");
        if (armorOverlayResource1 == null || !armorOverlayResource1.getResourcePath().equals(armorOverlayTexture1)) {
            armorOverlayResource1 = new ResourceLocation(armorOverlayTexture1);
        }
        this.armorOverlayTexture2 = itemData.getString("armorOverlayTexture2");
        if (armorOverlayResource2 == null || !armorOverlayResource2.getResourcePath().equals(armorOverlayTexture2)) {
            armorOverlayResource2 = new ResourceLocation(armorOverlayTexture2);
        }

        useFirstPersonOverrides = itemData.getBoolean("useFirstPersonOverrides");

        fpTranslateX = itemData.getFloat("firstPersonTranslateX");
        fpTranslateY = itemData.getFloat("firstPersonTranslateY");
        fpTranslateZ = itemData.getFloat("firstPersonTranslateZ");

        fpScaleX = itemData.hasKey("firstPersonScaleX") ? itemData.getFloat("firstPersonScaleX") : 1.0F;
        fpScaleY = itemData.hasKey("firstPersonScaleY") ? itemData.getFloat("firstPersonScaleY") : 1.0F;
        fpScaleZ = itemData.hasKey("firstPersonScaleZ") ? itemData.getFloat("firstPersonScaleZ") : 1.0F;

        fpRotationX = itemData.getFloat("firstPersonRotationX");
        fpRotationY = itemData.getFloat("firstPersonRotationY");
        fpRotationZ = itemData.getFloat("firstPersonRotationZ");
    }
}
