package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.item.ExtendedScriptCustomItem;
import com.veil.extendedscripts.item.IScriptedItemVariant;
import com.veil.extendedscripts.item.ScriptCustomArmor;
import com.veil.extendedscripts.item.ArmorScripted;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        if (itemstack != null && itemstack.getItem() instanceof IScriptedItemVariant) {
            Item item = itemstack.getItem();
            if (item instanceof ArmorScripted) {
                callbackInfo.setReturnValue(new ScriptCustomArmor(itemstack));
            }
            callbackInfo.setReturnValue(new ExtendedScriptCustomItem(itemstack));
        }
    }
}
