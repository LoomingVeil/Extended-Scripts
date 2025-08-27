package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.CustomProjectileImpactEvent;
import com.veil.extendedscripts.HotbarSlotChangedEvent;
import noppes.npcs.client.gui.script.GuiScriptInterface;
import noppes.npcs.client.gui.script.GuiScriptPlayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// This class is basically just copy and pasted from Kamkeel's DBC addon. All I did was add my own hook names.
@Mixin(value = GuiScriptPlayers.class, remap = false)
public abstract class MixinGuiScriptPlayers extends GuiScriptInterface {

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onConstructorComplete(CallbackInfo info) {
        this.hookList.add(new HotbarSlotChangedEvent().getHookName());
        this.hookList.add(new CustomProjectileImpactEvent().getHookName());
    }
}

