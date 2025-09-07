package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedItemScriptPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PacketHandler.class})
public class MixinNpcPacketHandler {
    @Shadow
    @Final
    public static PacketChannel REQUEST_PACKET;

    @Inject(method = "registerRequestPackets", at = @At(value = "HEAD"), remap = false)
    public void registerRequestPackets(CallbackInfo ci) {
        REQUEST_PACKET.registerPacket(new ExtendedItemScriptPacket());
    }
}
