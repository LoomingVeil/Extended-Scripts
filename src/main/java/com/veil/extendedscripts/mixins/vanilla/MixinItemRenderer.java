package com.veil.extendedscripts.mixins.vanilla;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemRenderer.class)
public class MixinItemRenderer {
    @Redirect(
        method = "updateEquippedItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getIsItemStackEqual(Lnet/minecraft/item/ItemStack;)Z"
        ),
        require = 0
    )
    private boolean extendedscripts$ignoreScriptedNbtInReequipCompare(ItemStack current, ItemStack other) {
        if (current != null && other != null && current.getItem() == other.getItem() && isScriptedItem(current)) {
            return true;
        }
        return true; // current != null && current.equals(other);
    }

    @Redirect(
        method = "updateEquippedItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;areItemStacksEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
        ),
        require = 0
    )
    private boolean extendedscripts$ignoreScriptedNbtInStaticCompare(ItemStack left, ItemStack right) {
        if (left != null && right != null && left.getItem() == right.getItem() && isScriptedItem(left)) {
            return true;
        }
        return ItemStack.areItemStacksEqual(left, right);
    }

    private static boolean isScriptedItem(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        String className = stack.getItem().getClass().getName();
        return "noppes.npcs.items.ItemCustomizable".equals(className) || "noppes.npcs.items.ItemScripted".equals(className);
    }
}
