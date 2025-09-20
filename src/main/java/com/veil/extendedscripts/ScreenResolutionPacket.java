package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class ScreenResolutionPacket implements IMessage {
    private int width, height;
    private int scale;

    public ScreenResolutionPacket() {}

    public ScreenResolutionPacket(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(width);
        buf.writeInt(height);
        buf.writeInt(scale);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        width = buf.readInt();
        height = buf.readInt();
        scale = buf.readInt();
    }

    public static class Handler implements IMessageHandler<ScreenResolutionPacket, IMessage> {
        @Override
        public IMessage onMessage(ScreenResolutionPacket message, MessageContext ctx) {
            // Runs on the Server side
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
            props.screenResolution.setSize(message.width, message.height);
            props.screenResolution.setScale(message.scale);

            return null;
        }
    }
}
