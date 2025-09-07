package com.veil.extendedscripts.item;

import com.veil.extendedscripts.ExtendedItemDisplayData;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.extendedapi.item.IExtendedItemCustom;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.item.IItemCustom;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.INpcScriptHandler;
import noppes.npcs.controllers.data.ItemDisplayData;
import noppes.npcs.scripted.CustomNPCsException;
import noppes.npcs.scripted.item.ScriptCustomItem;
import noppes.npcs.scripted.item.ScriptItemStack;

import java.util.*;

public class ExtendedScriptCustomItem extends ScriptItemStack implements IExtendedItemCustom, INpcScriptHandler {
    public final ItemDisplayData itemDisplay;
    public List<ScriptContainer> scripts = new ArrayList();
    public List<Integer> errored = new ArrayList();
    public String scriptLanguage = "ECMAScript";
    public boolean enabled = false;
    public boolean loaded = false;

    public double durabilityValue = 1.0D;
    public int stackSize = 64;

    public int maxItemUseDuration = 20;
    public int itemUseAction = 0; //0: none, 1: block, 2: bow, 3: eat, 4: drink

    public boolean isNormalItem = false;
    public boolean isTool = false;
    public int digSpeed = 1;
    public int armorType = -2; //-2: Fits in no armor slot,  -1: Fits in all slots, 0 - 4: Fits in Head -> Boots slot respectively
    public int enchantability;
    public long lastInited = -1;
    public String defaultTexture = "minecraft:textures/items/stick.png";
    public Integer defaultColor = ExtendedScripts.SIGNATURE_COLOR;
    public ExtendedScriptCustomItem(ItemStack item) {
        super(item);

        setDefaults();
        this.itemDisplay = new ExtendedItemDisplayData(
            defaultTexture, defaultColor
        );

        loadItemData();
    }

    public void setDefaults() { }

    public void replaceItem() {
        this.item = new ItemStack(Items.diamond_axe, 1);
    }

    public NBTTagCompound getScriptNBT(NBTTagCompound compound) {
        compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
        compound.setString("ScriptLanguage", this.scriptLanguage);
        compound.setBoolean("ScriptEnabled", this.enabled);
        return compound;
    }

    public void setScriptNBT(NBTTagCompound compound) {
        if (compound.hasKey("Scripts")) {
            this.scripts = NBTTags.GetScriptOld(compound.getTagList("Scripts", 10), this);
            this.scriptLanguage = compound.getString("ScriptLanguage");
            this.enabled = compound.getBoolean("ScriptEnabled");
        }
    }

    public int getType() {
        return 6;
    }

    private boolean isEnabled() {
        return this.enabled && ScriptController.HasStart;
    }

    @Override
    public INpcScriptHandler getScriptHandler() {
        return this;
    }

    public void callScript(EnumScriptType type, Event event) {
        this.callScript(type.function, event);
    }

    @Override
    public void callScript(String hookName, Event event) {
        if (!this.loaded) {
            this.loadScriptData();
            this.loaded = true;
        }

        if (!this.isEnabled())
            return;

        if (ScriptController.Instance.lastLoaded > lastInited) {
            lastInited = ScriptController.Instance.lastLoaded;
            if (!Objects.equals(hookName, EnumScriptType.INIT.function)) {
                EventHooks.onScriptItemInit(this);
            }
        }

        for (int i = 0; i < this.scripts.size(); i++) {
            ScriptContainer script = this.scripts.get(i);
            if (!this.errored.contains(i)) {
                if (script == null || script.errored || !script.hasCode())
                    continue;

                script.run(hookName, event);

                if (script.errored) {
                    this.errored.add(i);
                }
            }
        }
    }

    public boolean isClient() {
        return false;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public String getLanguage() {
        return this.scriptLanguage;
    }

    public void setLanguage(String lang) {
        this.scriptLanguage = lang;
    }

    public void setScripts(List<ScriptContainer> list) {
        this.scripts = list;
    }

    public List<ScriptContainer> getScripts() {
        return this.scripts;
    }

    public String noticeString() {
        return "ScriptedItem";
    }

    public Map<Long, String> getConsoleText() {
        return new TreeMap<>();
    }

    public void clearConsole() {
    }

    public int getMaxStackSize() {
        return this.stackSize;
    }

    public void setArmorType(int armorType) {
        this.armorType = armorType;
        saveItemData();
    }

    public int getArmorType() {
        return this.armorType;
    }

    public void setIsTool(boolean isTool) {
        this.isTool = isTool;
        saveItemData();
    }

    public boolean isTool() {
        return this.isTool;
    }

    public void setIsNormalItem(boolean normalItem) {
        this.isNormalItem = normalItem;
        saveItemData();
    }

    public boolean isNormalItem() {
        return this.isNormalItem;
    }


    public void setDigSpeed(int digSpeed) {
        this.digSpeed = digSpeed;
        saveItemData();
    }

    public int getDigSpeed() {
        return this.digSpeed;
    }

    public void setMaxStackSize(int size) {
        if (size >= 1 && size <= 127) {
            this.stackSize = size;
            saveItemData();
        } else {
            throw new CustomNPCsException("Stacksize has to be between 1 and 127", new Object[0]);
        }
    }

    public double getDurabilityValue() {
        return this.durabilityValue;
    }

    public void setDurabilityValue(float value) {
        this.durabilityValue = (double) value;
        saveItemData();
    }

    public int getMaxItemUseDuration() {
        return this.maxItemUseDuration;
    }

    public void setMaxItemUseDuration(int duration) {
        this.maxItemUseDuration = duration;
        saveItemData();
    }

    public void setItemUseAction(int action) {
        this.itemUseAction = action;
        saveItemData();
    }

    public int getItemUseAction() {
        return this.itemUseAction;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public void setEnchantability(int enchantability) {
        this.enchantability = enchantability;
        saveItemData();
    }

    public void saveScriptData() {
        NBTTagCompound c = this.item.getTagCompound();
        if (c == null) {
            this.item.setTagCompound(c = new NBTTagCompound());
        }

        c.setTag("ScriptedData", this.getScriptNBT(new NBTTagCompound()));
    }

    public void loadScriptData() {
        NBTTagCompound c = this.item.getTagCompound();
        if (c != null) {
            this.setScriptNBT(c.getCompoundTag("ScriptedData"));
        }
    }

    public NBTTagCompound getMCNbt() {
        NBTTagCompound compound = super.getMCNbt();
        compound.setTag("ItemData", this.getItemNBT(new NBTTagCompound()));
        compound.setTag("ScriptedData", this.getScriptNBT(new NBTTagCompound()));
        return compound;
    }

    public void setMCNbt(NBTTagCompound compound) {
        super.setMCNbt(compound);
        setItemNBT(compound.getCompoundTag("ItemData"));
        setScriptNBT(compound.getCompoundTag("ScriptedData"));
    }

    public NBTTagCompound getItemNBT(NBTTagCompound compound) {
        this.itemDisplay.writeToNBT(compound);

        compound.setDouble("DurabilityValue", this.durabilityValue);
        compound.setInteger("MaxStackSize", this.stackSize);

        compound.setBoolean("IsTool", this.isTool);
        compound.setBoolean("IsNormalItem", this.isNormalItem);
        compound.setInteger("DigSpeed", this.digSpeed);
        compound.setInteger("ArmorType", this.armorType);
        compound.setInteger("Enchantability", this.enchantability);

        compound.setInteger("MaxItemUseDuration", this.maxItemUseDuration);
        compound.setInteger("ItemUseAction", this.itemUseAction);
        return compound;
    }

    public void setItemNBT(NBTTagCompound compound) {
        this.itemDisplay.readFromNBT(compound);

        this.durabilityValue = compound.getDouble("DurabilityValue");
        this.stackSize = compound.getInteger("MaxStackSize");

        this.isTool = compound.getBoolean("IsTool");
        this.isNormalItem = compound.getBoolean("IsNormalItem");
        this.digSpeed = compound.getInteger("DigSpeed");
        this.armorType = compound.getInteger("ArmorType");
        this.enchantability = compound.getInteger("Enchantability");

        this.maxItemUseDuration = compound.getInteger("MaxItemUseDuration");
        this.itemUseAction = compound.getInteger("ItemUseAction");
    }

    public String getTexture() {
        String ret = this.itemDisplay.texture == null ? "" : this.itemDisplay.texture;
        // System.out.println("Returning: "+ret);
        return ret;
    }

    public void setTexture(String texture) {
        if (texture == null)
            texture = "";
        this.itemDisplay.texture = texture;
        saveItemData();
    }

    public Boolean getDurabilityShow() {
        return this.itemDisplay.durabilityShow != null ? this.itemDisplay.durabilityShow : false;
    }

    public void setDurabilityShow(Boolean bo) {
        this.itemDisplay.durabilityShow = bo;
        saveItemData();
    }

    public Integer getDurabilityColor() {
        return this.itemDisplay.durabilityColor != null ? this.itemDisplay.durabilityColor : -1;
    }

    public void setDurabilityColor(Integer color) {
        this.itemDisplay.durabilityColor = color;
        saveItemData();
    }

    public Integer getColor() {
        return this.itemDisplay.itemColor != null ? this.itemDisplay.itemColor : 0x8B4513;
    }

    public void setColor(Integer color) {
        System.out.println("Setting item color");
        this.itemDisplay.itemColor = color;
        saveItemData();
    }


    public void setRotation(Float rotationX, Float rotationY, Float rotationZ) {
        this.itemDisplay.rotationX = rotationX;
        this.itemDisplay.rotationY = rotationY;
        this.itemDisplay.rotationZ = rotationZ;
        saveItemData();
    }

    public void setRotationRate(Float rotationXRate, Float rotationYRate, Float rotationZRate) {
        this.itemDisplay.rotationXRate = rotationXRate;
        this.itemDisplay.rotationYRate = rotationYRate;
        this.itemDisplay.rotationZRate = rotationZRate;
        saveItemData();
    }

    public void setScale(Float scaleX, Float scaleY, Float scaleZ) {
        this.itemDisplay.scaleX = scaleX;
        this.itemDisplay.scaleY = scaleY;
        this.itemDisplay.scaleZ = scaleZ;
        saveItemData();
    }

    public void setTranslate(Float translateX, Float translateY, Float translateZ) {
        this.itemDisplay.translateX = translateX;
        this.itemDisplay.translateY = translateY;
        this.itemDisplay.translateZ = translateZ;
        saveItemData();
    }

    public Float getRotationX() {
        return this.itemDisplay.rotationX != null ? this.itemDisplay.rotationX : 0;
    }

    public Float getRotationY() {
        return this.itemDisplay.rotationY != null ? this.itemDisplay.rotationY : 0;
    }

    public Float getRotationZ() {
        return this.itemDisplay.rotationZ != null ? this.itemDisplay.rotationZ : 0;
    }

    public Float getRotationXRate() {
        return this.itemDisplay.rotationXRate != null ? this.itemDisplay.rotationXRate : 0;
    }

    public Float getRotationYRate() {
        return this.itemDisplay.rotationYRate != null ? this.itemDisplay.rotationYRate : 0;
    }

    public Float getRotationZRate() {
        return this.itemDisplay.rotationZRate != null ? this.itemDisplay.rotationZRate : 0;
    }

    public Float getScaleX() {
        return this.itemDisplay.scaleX != null ? this.itemDisplay.scaleX : 0;
    }

    public Float getScaleY() {
        return this.itemDisplay.scaleY != null ? this.itemDisplay.scaleY : 0;
    }

    public Float getScaleZ() {
        return this.itemDisplay.scaleZ != null ? this.itemDisplay.scaleZ : 0;
    }

    public Float getTranslateX() {
        return this.itemDisplay.translateX != null ? this.itemDisplay.translateX : 0;
    }

    public Float getTranslateY() {
        return this.itemDisplay.translateY != null ? this.itemDisplay.translateY : 0;
    }

    public Float getTranslateZ() {
        return this.itemDisplay.translateZ != null ? this.itemDisplay.translateZ : 0;
    }

    public void saveItemData() {
        NBTTagCompound c = this.item.getTagCompound();
        if (c == null) {
            this.item.setTagCompound(c = new NBTTagCompound());
        }
        c.setTag("ItemData", this.getItemNBT(new NBTTagCompound()));
    }

    public void loadItemData() {
        NBTTagCompound c = this.item.getTagCompound();
        if (c != null && !c.getCompoundTag("ItemData").hasNoTags()) {
            this.setItemNBT(c.getCompoundTag("ItemData"));
        }
    }
}
