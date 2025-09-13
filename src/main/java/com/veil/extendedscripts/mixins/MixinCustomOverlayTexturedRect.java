package com.veil.extendedscripts.mixins;

import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ClientCacheHandler;
import noppes.npcs.client.gui.customoverlay.components.CustomOverlayTexturedRect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value= CustomOverlayTexturedRect.class)
public class MixinCustomOverlayTexturedRect {
    // Was hoping this would fix the issue where displaying an overlay over and over makes your game lag, but alas, it did not.
    @Redirect(
        method = "<init>(ILjava/lang/String;IIIIII)V",
        at = @At(
            value = "NEW",
            target = "Lnet/minecraft/util/ResourceLocation;"
        ),
        remap = true // or false if you're mixing into a non-obf mod
    )
    private ResourceLocation redirectNewResourceLocation(String directory) {
        return ClientCacheHandler.getImageData(directory).getLocation();
    }
}
