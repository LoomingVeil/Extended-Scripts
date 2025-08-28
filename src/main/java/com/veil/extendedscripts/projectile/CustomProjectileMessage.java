package com.veil.extendedscripts.projectile;

import com.veil.extendedscripts.extendedapi.entity.ICustomProjectileRenderProperties;
import com.veil.extendedscripts.properties.EntityPropertyUpdateMessage;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CustomProjectileMessage implements IMessage {
    private int entityId;
    private String shatterParticle;
    private boolean stopRotatingOnImpact;
    private boolean onImpactSnapToInitRotation;

    public CustomProjectileMessage() {}

    public CustomProjectileMessage(Entity entity, String shatterParticle, boolean stopRotatingOnImpact, boolean onImpactSnapToInitRotation) {
        System.out.println("Making new packet: "+entity.getEntityId());
        this.entityId = entity.getEntityId();
        this.shatterParticle = shatterParticle;
        this.stopRotatingOnImpact = stopRotatingOnImpact;
        this.onImpactSnapToInitRotation = onImpactSnapToInitRotation;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);

        buf.writeShort(this.shatterParticle.length());
        buf.writeBytes(this.shatterParticle.getBytes());
        buf.writeBoolean(this.stopRotatingOnImpact);
        buf.writeBoolean(this.onImpactSnapToInitRotation);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();

        byte[] strBytes = new byte[buf.readShort()];
        buf.readBytes(strBytes);
        this.shatterParticle = new String(strBytes);

        this.stopRotatingOnImpact = buf.readBoolean();
        this.onImpactSnapToInitRotation = buf.readBoolean();
    }

    public static class CustomProjectileMessageHandler implements IMessageHandler<CustomProjectileMessage, IMessage> {
        @Override
        public IMessage onMessage(final CustomProjectileMessage message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    WorldClient world = Minecraft.getMinecraft().theWorld;

                    // Find the entity based on the ID from the packet
                    Entity entity = world.getEntityByID(message.entityId);

                    if (entity instanceof EntityCustomProjectile) {
                        EntityCustomProjectile projectile = (EntityCustomProjectile) entity;

                        // Apply the data from the packet to the client-side entity
                        projectile.setShatterParticle(message.shatterParticle);

                        ICustomProjectileRenderProperties renderProperties = projectile.getRenderProperties();
                        renderProperties.setStopRotatingOnImpact(message.stopRotatingOnImpact);
                        renderProperties.setOnImpactSnapToInitRotation(message.onImpactSnapToInitRotation);
                        System.out.println("Packet sent!");
                        return;
                    }

                    if (entity == null) {
                        System.out.println("Entity doesn't exist??? Id was "+message.entityId);
                    }
                }
            });
            return null;
        }
    }
}
