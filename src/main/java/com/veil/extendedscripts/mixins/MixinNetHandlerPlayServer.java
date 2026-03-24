package com.veil.extendedscripts.mixins;

import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {
    @ModifyConstant(method = "processUseEntity", constant = @Constant(doubleValue = 36.0D))
    private double modifyAttackReach(double original) {
        return 400.0D; // 20 blocks squared
    }
}
