package com.veil.extendedscripts;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.EntityPropertyUpdateMessage;
import com.veil.extendedscripts.properties.PlayerPropertyUpdateMessage;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

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
    }
}
