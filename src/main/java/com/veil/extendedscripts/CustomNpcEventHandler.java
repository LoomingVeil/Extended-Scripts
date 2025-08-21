package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.tracker.AttributeRecalcEvent;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Handles events related to CustomNpcs
 */
public class CustomNpcEventHandler {
    public static void init() {
        AttributeRecalcEvent.registerListener((player, tracker) -> {
            System.out.println("Post attribute recalc for " + player.getDisplayName());
            float flightSpeed = ExtendedAPI.getAttribute(player, PlayerAttribute.FLIGHT_SPEED_HORIZONTAL) * 0.01F * 0.05F;
            if (player.capabilities.getFlySpeed() != flightSpeed + 0.05F) {
                player.capabilities.setFlySpeed(flightSpeed + 0.05F);
                player.sendPlayerAbilities();
            }
        });
    }
}
