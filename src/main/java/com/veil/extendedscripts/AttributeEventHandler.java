package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.PlayerAttribute;
import kamkeel.npcs.controllers.data.attribute.tracker.AttributeRecalcEvent;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;

public class AttributeEventHandler {
    public static void init() {
        AttributeRecalcEvent.registerListener((player, tracker) -> {
            // System.out.println("Post attribute recalc for " + player.getDisplayName());
            float flightSpeed = ExtendedAPI.getAttribute(player, PlayerAttribute.FLIGHT_SPEED_HORIZONTAL) * 0.01F * 0.05F;
            if (player.capabilities.getFlySpeed() != flightSpeed + 0.05F) {
                player.capabilities.setFlySpeed(flightSpeed + 0.05F);
                player.sendPlayerAbilities();
            }
        });
    }
}
