package com.veil.extendedscripts.properties;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class EntityPropertyUpdateMessage implements IMessage {
    private float gravity = 1;
    private float downwardGravity = -1;
    private float upwardGravity = -1;
    private float underwaterGravity = 1;
    private float underwaterDownwardGravity = -1;
    private float underwaterUpwardGravity = -1;
    private float verticalJumpPower = 1;
    private float horizontalJumpPower = 1;
    private float maxFallDistance = 3;
    private boolean canMove = true;

    public EntityPropertyUpdateMessage() {} // Required default constructor

    public EntityPropertyUpdateMessage(ExtendedScriptEntityProperties props) {
        /*this.gravity = props.getGravity();
        this.downwardGravity = props.getDownwardGravity();
        this.upwardGravity = props.getUpwardGravity();
        this.underwaterGravity = props.getUnderwaterGravity();
        this.underwaterDownwardGravity = props.getUnderwaterDownwardGravity();
        this.underwaterUpwardGravity = props.getUnderwaterUpwardGravity();
        this.verticalJumpPower = props.getVerticalJumpPower();
        this.horizontalJumpPower = props.getHorizontalJumpPower();
        this.maxFallDistance = props.getMaxFallDistance();
        this.canMove = props.getCanMove();*/
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.gravity = buf.readFloat();
        this.downwardGravity = buf.readFloat();
        this.upwardGravity = buf.readFloat();
        this.underwaterGravity = buf.readFloat();
        this.underwaterDownwardGravity = buf.readFloat();
        this.underwaterUpwardGravity = buf.readFloat();
        this.verticalJumpPower = buf.readFloat();
        this.horizontalJumpPower = buf.readFloat();
        this.maxFallDistance = buf.readFloat();
        this.canMove = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.gravity);
        buf.writeFloat(this.downwardGravity);
        buf.writeFloat(this.upwardGravity);
        buf.writeFloat(this.underwaterGravity);
        buf.writeFloat(this.underwaterDownwardGravity);
        buf.writeFloat(this.underwaterUpwardGravity);
        buf.writeFloat(this.verticalJumpPower);
        buf.writeFloat(this.horizontalJumpPower);
        buf.writeFloat(this.maxFallDistance);
        buf.writeBoolean(this.canMove);
    }

    // --- Message Handler ---
    public static class EntityPropertyUpdateMessageHandler implements IMessageHandler<EntityPropertyUpdateMessage, IMessage> {
        @Override
        public IMessage onMessage(final EntityPropertyUpdateMessage message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().thePlayer; // Get the client player
                    if (player != null) {
                        ExtendedScriptEntityProperties props = ExtendedScriptEntityProperties.get(player);
                        if (props != null) {
                            // Update the client's local EEP data
                            /*props.setGravity(message.gravity);
                            props.setDownwardGravity(message.downwardGravity);
                            props.setUpwardGravity(message.upwardGravity);
                            props.setUnderwaterGravity(message.underwaterGravity);
                            props.setUnderwaterDownwardGravity(message.underwaterDownwardGravity);
                            props.setUnderwaterUpwardGravity(message.underwaterUpwardGravity);
                            props.setVerticalJumpPower(message.verticalJumpPower);
                            props.setHorizontalJumpPower(message.horizontalJumpPower);
                            props.setMaxFallDistance(message.maxFallDistance);
                            props.setCanMove(message.canMove);*/
                            // System.out.println("Client received entity EEP sync");
                        }
                    }
                }
            });
            return null; // No response message
        }
    }
}
