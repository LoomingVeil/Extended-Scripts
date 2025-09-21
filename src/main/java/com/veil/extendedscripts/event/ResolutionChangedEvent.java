package com.veil.extendedscripts.event;

import com.veil.extendedscripts.ScreenResolution;
import com.veil.extendedscripts.extendedapi.IScreenResolution;
import com.veil.extendedscripts.extendedapi.event.IResolutionChangedEvent;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class ResolutionChangedEvent extends PlayerEvent implements IResolutionChangedEvent {
    IScreenResolution oldResolution;
    public ResolutionChangedEvent() {
        super(null);
    }

    public ResolutionChangedEvent(IPlayer player, ScreenResolution resolution) {
        super(player);
        this.oldResolution = resolution;
    }

    public String getHookName() {
        return "resolutionChanged";
    }

    public IScreenResolution getOldResolution() {
        return oldResolution;
    }
}
