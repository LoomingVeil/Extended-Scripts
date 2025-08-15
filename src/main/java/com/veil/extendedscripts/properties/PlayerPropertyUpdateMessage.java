package com.veil.extendedscripts.properties;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;

public class PlayerPropertyUpdateMessage implements IMessage {
    private float verticalFlightSpeed = 1;
    private float horizontalFlightSpeed = 1;
    private boolean canFly = false;
    private boolean lastSeenFlying = false;


    public PlayerPropertyUpdateMessage() {} // Required default constructor

    public PlayerPropertyUpdateMessage(ExtendedScriptPlayerProperties props) {
        this.verticalFlightSpeed = props.getVerticalFlightSpeed();
        this.horizontalFlightSpeed = props.getHorizontalFlightSpeed();
        this.canFly = props.getCanFly();
        this.lastSeenFlying = props.getLastSeenFlying();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.verticalFlightSpeed = buf.readFloat();
        this.horizontalFlightSpeed = buf.readFloat();
        this.canFly = buf.readBoolean();
        this.lastSeenFlying = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.verticalFlightSpeed);
        buf.writeFloat(this.horizontalFlightSpeed);
        buf.writeBoolean(this.canFly);
        buf.writeBoolean(this.lastSeenFlying);
    }

    // --- Message Handler ---
    public static class PlayerPropertyUpdateMessageHandler implements IMessageHandler<PlayerPropertyUpdateMessage, IMessage> {
        @Override
        public IMessage onMessage(final PlayerPropertyUpdateMessage message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().thePlayer; // Get the client player
                    if (player != null) {
                        ExtendedScriptPlayerProperties props = ExtendedScriptPlayerProperties.get(player);
                        if (props != null) {
                            props.setVerticalFlightSpeed(message.verticalFlightSpeed);
                            props.setHorizontalFlightSpeed(message.horizontalFlightSpeed);
                            props.setCanFly(message.canFly);
                            props.setLastSeenFlying(message.lastSeenFlying);
                            // System.out.println("Client received player EEP sync");
                        }
                    }
                }
            });
            return null; // No response message
        }
    }
}
