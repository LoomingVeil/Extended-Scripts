package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
            System.out.println("Fight Speed: "+flightSpeed);
            if (player.capabilities.getFlySpeed() != flightSpeed + 0.05F) {
                player.capabilities.setFlySpeed(flightSpeed + 0.05F);
                player.sendPlayerAbilities();
                System.out.println("Updating capabilities");
            }
        });
    }
}
