package com.veil.extendedscripts.mixins.vanilla;

import com.veil.extendedscripts.ClientTransferStorage;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This class allows you to change player block reach, but only on the client side.
 */
@Mixin(value = PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    /*@Inject(method = "getBlockReachDistance", at = @At("HEAD"), cancellable = true)
    private void onGetBlockReachDistance(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ClientTransferStorage.attackReach + 3.0F);
    }*/
}
