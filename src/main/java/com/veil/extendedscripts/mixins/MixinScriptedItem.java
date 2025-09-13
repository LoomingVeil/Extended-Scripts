package com.veil.extendedscripts.mixins;


import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.items.ItemCustomizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

// Extends the scripted item itself
@Mixin(value={ItemCustomizable.class})
public abstract class MixinScriptedItem {


    @Unique
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        NBTTagCompound itemData = getItemDataTag(stack);
        if (!itemData.hasKey("harvestLevels")) {
            itemData.setTag("harvestLevels", new NBTTagCompound());
        }
        NBTTagCompound harvestLevels = itemData.getCompoundTag("harvestLevels");
        if (!harvestLevels.hasKey(toolClass)) {
            return -1;
        }
        return harvestLevels.getInteger(toolClass);
    }

    @Unique
    private static NBTTagCompound getItemDataTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey("ExtendedItemData")) {
            tag.setTag("ExtendedItemData", new NBTTagCompound());
        }
        return tag.getCompoundTag("ExtendedItemData");
    }
}
