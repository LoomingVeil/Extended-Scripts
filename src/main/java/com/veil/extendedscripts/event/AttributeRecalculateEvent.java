package com.veil.extendedscripts.event;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class AttributeRecalculateEvent extends PlayerEvent {
    public AttributeRecalculateEvent() {
        super(null);
    }

    public AttributeRecalculateEvent(IPlayer player) {
        super(player);
    }
}
