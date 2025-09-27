package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedAPI;
import noppes.npcs.extendedapi.overlay.IOverlayLabel;
import noppes.npcs.scripted.overlay.ScriptOverlayLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(value = ScriptOverlayLabel.class)
public abstract class MixinScriptOverlayLabelExtension implements IOverlayLabel {
    @Shadow
    public abstract String getText();

    /**
     * Gets the width of the string in pixels. This can be useful for centering text.
     * Having certain non-standard special characters may produce inaccurate results.
     */
    public int getStringWidth() {
        return ExtendedAPI.Instance.getStringPixelWidth(getText());
    }
}
