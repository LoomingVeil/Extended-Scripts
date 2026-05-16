package com.veil.extendedscripts.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNoCallback;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "noppes.npcs.client.gui.script.GuiScriptInterface", remap = false)
public abstract class MixinGuiScriptInterface extends GuiNPCInterface {
    @Shadow
    protected int activeTab = 0;

    @Inject(method = "initSettingsTab", at = @At(value = "RETURN"), remap = false)
    public void initGui(CallbackInfo cif) {
        GuiNpcButton copyButton = getButton(100);
        GuiNpcButton clearButton = getButton(102);
        if (copyButton == null || clearButton == null) {
            return;
        }

        copyButton.yPosition += 21;
        clearButton.yPosition += 21;

        GuiNpcButton refButton = getButton(110);
        if (refButton != null) {
            addButton(new GuiNpcButton(142, refButton.xPosition, refButton.yPosition + 21, 80, 20, "gui.extendeddoc"));
        }

        refButton = getButton(109);
        if (refButton != null) {
            addButton(new GuiNpcButton(143, refButton.xPosition, refButton.yPosition + 21, 80, 20, "gui.extendedwebsite"));
        }
    }

    @Inject(method = "actionPerformed", at = @At(value = "RETURN"))
    protected void actionPerformed(GuiButton guibutton, CallbackInfo cif) {
        if (guibutton.id == 142) {
            this.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback) (Object) this, "https://loomingveil.github.io/Extended-Scripts-Combined-API/", 4, true));
        } else if (guibutton.id == 143) {
            this.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback) (Object) this, "https://github.com/LoomingVeil/Extended-Scripts", 4, true));
        }
    }
}
