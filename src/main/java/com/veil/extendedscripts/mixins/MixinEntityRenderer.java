package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ClientTransferStorage;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer {
    @ModifyConstant(method = "getMouseOver", constant = @Constant(doubleValue = 6.0D, ordinal = 0))
    private double modifyReachCreative(double original) {
        return ClientTransferStorage.attackReach + 3;
    }

    @ModifyConstant(method = "getMouseOver", constant = @Constant(doubleValue = 3.0D, ordinal = 0))
    private double modifyReachSurvival1(double original) {
        return ClientTransferStorage.attackReach;
    }

    @ModifyConstant(method = "getMouseOver", constant = @Constant(doubleValue = 3.0D, ordinal = 1))
    private double modifyReachSurvival2(double original) {
        return ClientTransferStorage.attackReach;
    }

    @Redirect(
        method = "getMouseOver",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getBlockReachDistance()F"
        )
    )
    private float redirectBlockReachDistance(PlayerControllerMP controller) {
        return ClientTransferStorage.attackReach + 3.0F;
    }
}

