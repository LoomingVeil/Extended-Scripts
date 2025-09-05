package com.veil.extendedscripts;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.projectile.CustomProjectileMessage;
import com.veil.extendedscripts.properties.EntityPropertyUpdateMessage;
import com.veil.extendedscripts.properties.PlayerPropertyUpdateMessage;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.enums.EnumChannelType;
import net.minecraft.entity.Entity;
import noppes.npcs.LogWriter;

import java.util.List;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ExtendedScripts.MODID);

    private static int id = 0; // Packet discriminator ID

    public static int nextID() {
        return id++;
    }

    public static void registerMessages() {
        // Register your packet types here
        // Example: INSTANCE.registerMessage(YourMessageHandler.class, YourMessage.class, nextID(), Side.SERVER);
        // We'll register the EEP sync packet here:
        INSTANCE.registerMessage(EntityPropertyUpdateMessage.EntityPropertyUpdateMessageHandler.class, EntityPropertyUpdateMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PlayerPropertyUpdateMessage.PlayerPropertyUpdateMessageHandler.class, PlayerPropertyUpdateMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(CustomProjectileMessage.CustomProjectileMessageHandler.class, CustomProjectileMessage.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(UpdateScreenSizePacket.UpdateScreenSizePacketHandler.class, UpdateScreenSizePacket.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(ScriptItemClickPacket.Handler.class, ScriptItemClickPacket.class, nextID(), Side.SERVER);
    }

    public static void sendToAllAround(IMessage packet, Entity entity) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
        INSTANCE.sendToAllAround(packet, point);
    }

    /*public void sendTracking(AbstractPacket packet, final Entity entity) {
        FMLEventChannel eventChannel = getEventChannel(packet);
        if (eventChannel == null) {
            LogWriter.error("Error: Event channel is null for packet: " + packet.getClass().getName());
            return;
        }
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 60);
        sendAllPackets(packet, p -> eventChannel.sendToAllAround(p, point));
    }

    private void sendAllPackets(AbstractPacket packet, kamkeel.npcs.network.PacketHandler.SendAction action) {
        // get all generated FMLProxyPacket objects (could be 1 for normal, or many for large)
        List<FMLProxyPacket> proxyPackets = packet.generatePackets();
        if (proxyPackets.isEmpty()) {
            LogWriter.error("Warning: No packets generated for " + packet.getClass().getName());
        }

        for (FMLProxyPacket proxy : proxyPackets) {
            action.send(proxy);
        }
    }*/
}
