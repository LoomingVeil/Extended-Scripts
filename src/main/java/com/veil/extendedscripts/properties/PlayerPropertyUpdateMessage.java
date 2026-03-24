package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ClientTransferStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerPropertyUpdateMessage implements IMessage {
    private boolean canFly = false;
    private boolean lastSeenFlying = false;
    private boolean keepInventory = false;
    private float attackReach = 3;

    public PlayerPropertyUpdateMessage() {} // Required default constructor

    public PlayerPropertyUpdateMessage(ExtendedScriptPlayerProperties props) {
        this.canFly = props.get(PlayerAttribute.CAN_FLY);
        this.lastSeenFlying = props.get(PlayerAttribute.LAST_SEEN_FLYING);
        this.keepInventory = props.get(PlayerAttribute.KEEP_INVENTORY);
        this.attackReach = props.get(PlayerAttribute.ATTACK_REACH);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.canFly = buf.readBoolean();
        this.lastSeenFlying = buf.readBoolean();
        this.keepInventory = buf.readBoolean();
        this.attackReach = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.canFly);
        buf.writeBoolean(this.lastSeenFlying);
        buf.writeBoolean(this.keepInventory);
        buf.writeFloat(this.attackReach);
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
                            props.set(PlayerAttribute.ATTACK_REACH, message.attackReach);

                            ClientTransferStorage.attackReach = message.attackReach;

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
