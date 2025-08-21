package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.PlayerAttribute;
import kamkeel.npcs.controllers.data.attribute.tracker.AttributeRecalcEvent;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.entity.player.EntityPlayer;

public class AttributeEventHandler {
    public static void init() {
        AttributeRecalcEvent.registerListener(new AttributeRecalcEventHandler());
    }
}

class AttributeRecalcEventHandler implements AttributeRecalcEvent.Listener {
    @Override
    public void onPost(EntityPlayer player, PlayerAttributeTracker playerAttributeTracker) {
        float flightSpeed = ExtendedAPI.getAttribute(player, PlayerAttribute.FLIGHT_SPEED_HORIZONTAL) * 0.01F * 0.05F;
        if (player.capabilities.getFlySpeed() != flightSpeed + 0.05F) {
            player.capabilities.setFlySpeed(flightSpeed + 0.05F);
            player.sendPlayerAbilities();
        }
    }
}
