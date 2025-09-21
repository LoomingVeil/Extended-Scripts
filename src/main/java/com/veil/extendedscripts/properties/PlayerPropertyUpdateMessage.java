package com.veil.extendedscripts.properties;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;

public class PlayerPropertyUpdateMessage implements IMessage {
    private boolean canFly = false;
    private boolean lastSeenFlying = false;
    private boolean keepInventory = false;


    public PlayerPropertyUpdateMessage() {} // Required default constructor

    public PlayerPropertyUpdateMessage(ExtendedScriptPlayerProperties props) {
        this.canFly = props.get(PlayerAttribute.CAN_FLY);
        this.lastSeenFlying = props.get(PlayerAttribute.LAST_SEEN_FLYING);
        this.keepInventory = props.get(PlayerAttribute.KEEP_INVENTORY);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.canFly = buf.readBoolean();
        this.lastSeenFlying = buf.readBoolean();
        this.keepInventory = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.canFly);
        buf.writeBoolean(this.lastSeenFlying);
        buf.writeBoolean(this.keepInventory);
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
                            props.set(PlayerAttribute.CAN_FLY, message.canFly);
                            props.set(PlayerAttribute.LAST_SEEN_FLYING, message.lastSeenFlying);
                            props.set(PlayerAttribute.KEEP_INVENTORY, message.keepInventory);

                            if (!player.capabilities.isCreativeMode) {
                                player.capabilities.allowFlying = message.canFly;
                                player.sendPlayerAbilities();
                            }

                            // System.out.println("Client received player EEP sync");
                        }
                    }
                }
            });
            return null;
        }
    }
}
