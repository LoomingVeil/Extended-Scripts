package com.veil.extendedscripts;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.PacketClient;
import kamkeel.npcs.network.packets.player.ScreenSizePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class UpdateScreenResolutionPacket implements IMessage {

    public UpdateScreenResolutionPacket() { }

    @Override
    public void toBytes(ByteBuf buf) { }

    @Override
    public void fromBytes(ByteBuf buf) { }

    public static class Handler implements IMessageHandler<UpdateScreenResolutionPacket, IMessage> {
        @Override
        public IMessage onMessage(final UpdateScreenResolutionPacket message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    Minecraft mc = Minecraft.getMinecraft();
                    ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                    PacketHandler.INSTANCE.sendToServer(new ScreenResolutionPacket(resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaleFactor()));
                }
            });
            return null;
        }
    }
}
