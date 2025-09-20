package com.veil.extendedscripts.event;

import com.veil.extendedscripts.extendedapi.event.IAttributeRecalculateEvent;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class AttributeRecalculateEvent extends PlayerEvent implements IAttributeRecalculateEvent {
    public AttributeRecalculateEvent() {
        super(null);
    }

    public AttributeRecalculateEvent(IPlayer player) {
        super(player);
    }

    public String getHookName() {
        return "attributeRecalculate";
    }
}
