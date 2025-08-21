package com.veil.extendedscripts.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.veil.extendedscripts.ExtendedScripts;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerEquipmentTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Mixin(value={PlayerAttributeTracker.class})
public class MixinPlayerAttributeTracker {

    @ModifyVariable(
        method = "recalcAttributes(Lnet/minecraft/entity/player/EntityPlayer;)V",
        at = @At(
            value = "STORE",
            ordinal = 0
        ),
        index = 3,
        remap = false
    )
    private ItemStack[] modifyEquipmentArray(ItemStack[] original, @Local EntityPlayer player) {
        ItemStack[] newArray = Arrays.copyOf(original, original.length + 1);
        newArray[original.length] = ExtendedScripts.getPlayerProperties(player).getAttributeCore();
        return newArray;
    }
}


