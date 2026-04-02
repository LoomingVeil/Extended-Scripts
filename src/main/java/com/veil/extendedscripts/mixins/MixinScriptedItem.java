package com.veil.extendedscripts.mixins;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Extends the scripted item itself
@Pseudo
@Mixin(targets = {
    "noppes.npcs.items.ItemCustomizable",
    "noppes.npcs.items.ItemScripted"
}, remap = false)
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

    @Unique
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
}
