package com.veil.extendedscripts.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import noppes.npcs.client.gui.GuiScript;
import noppes.npcs.client.gui.script.GuiScriptInterface;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScript.class)
public class MixinNpcGuiScript extends GuiScriptInterface {
    @Inject(method = "initGui", at = @At(value = "RETURN"), remap = false)
    public void initGui(CallbackInfo cif) {
        GuiMenuTopButton refButton = getTopButton(15);
        addTopButton(new GuiMenuTopButton(42, refButton, "gui.extendedapi"));
    }

    @Inject(method = "actionPerformed", at = @At(value = "RETURN"))
    protected void actionPerformed(GuiButton guibutton, CallbackInfo cif) {
        if (guibutton.id == 42) {
            this.displayGuiScreen(new GuiConfirmOpenLink(((GuiScriptInterface) (Object) this), "https://loomingveil.github.io/Extended-Scripts-Combined-API/", 4, true));
        }
    }
}
