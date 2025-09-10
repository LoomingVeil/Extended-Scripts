package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.items.ScriptItemFood;
import com.veil.extendedscripts.items.ScriptItemPotion;
import com.veil.extendedscripts.items.ScriptItemTool;
import net.minecraft.item.*;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NpcAPI.class, remap = false)
public class MixinNpcAPI {

    @Inject(method = "getIItemStack", at = @At(value = "HEAD"), cancellable = true)
    public void getIItemStack(ItemStack itemstack, CallbackInfoReturnable<IItemStack> callbackInfo) {
        if (itemstack != null) {
            Item item = itemstack.getItem();
            if (item instanceof ItemFood) {
                callbackInfo.setReturnValue(new ScriptItemFood(itemstack));
            } else if (item instanceof ItemPotion) {
                callbackInfo.setReturnValue(new ScriptItemPotion(itemstack));
            } else if (item instanceof ItemTool) { // If we decide to add specific tools like pickaxe, axe, ect, they should be checked before/inside this statement
                callbackInfo.setReturnValue(new ScriptItemTool(itemstack));
            }
        }
    }
}
