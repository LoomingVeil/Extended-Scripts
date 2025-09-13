package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.event.*;
import noppes.npcs.client.gui.script.GuiScriptInterface;
import noppes.npcs.client.gui.script.GuiScriptItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScriptItem.class, remap = false)
public class MixinGuiScriptItem extends GuiScriptInterface {
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onConstructorComplete(CallbackInfo info) {
        this.hookList.add(new ArmorDamagedEvent().getHookName());
    }
}
