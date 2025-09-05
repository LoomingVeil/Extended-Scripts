package com.veil.extendedscripts;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.PacketClient;
import kamkeel.npcs.network.packets.player.ScreenSizePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;

public class UpdateScreenSizePacket implements IMessage {
    public UpdateScreenSizePacket() {}

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    public static class UpdateScreenSizePacketHandler implements IMessageHandler<UpdateScreenSizePacket, IMessage> {
        @Override
        public IMessage onMessage(final UpdateScreenSizePacket message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    Minecraft mc = Minecraft.getMinecraft();
                    PacketClient.sendClient(new ScreenSizePacket(mc.displayWidth, mc.displayHeight));
                }
            });
            return null;
        }
    }
}
