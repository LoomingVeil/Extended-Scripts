package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.commands.IInventoryEffectRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainerCreative.class})
public abstract class MixinGuiContainerCreative extends InventoryEffectRenderer {
    public MixinGuiContainerCreative(Container p_i1089_1_) {
        super(p_i1089_1_);
    }

    @Inject(method = "actionPerformed", at = @At(value = "HEAD"))
    public void handleAction(GuiButton button, CallbackInfo cif) {
        IInventoryEffectRenderer effectRenderer = ((IInventoryEffectRenderer) (Object) this);
        effectRenderer.onActionPerformed(button);
    }
}
