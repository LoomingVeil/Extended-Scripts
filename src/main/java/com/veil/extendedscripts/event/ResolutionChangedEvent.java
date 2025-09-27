package com.veil.extendedscripts.event;

import com.veil.extendedscripts.ScreenResolution;
import com.veil.extendedscripts.extendedapi.IScreenResolution;
import com.veil.extendedscripts.extendedapi.event.IResolutionChangedEvent;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class ResolutionChangedEvent extends PlayerEvent implements IResolutionChangedEvent {
    IScreenResolution oldResolution;
    IScreenResolution newResolution;
    public ResolutionChangedEvent() {
        super(null);
    }

    public ResolutionChangedEvent(IPlayer player, ScreenResolution oldResolution, ScreenResolution newResolution) {
        super(player);
        this.oldResolution = oldResolution;
        this.newResolution = newResolution;
    }

    public String getHookName() {
        return "resolutionChanged";
    }

    public IScreenResolution getOldResolution() {
        return oldResolution;
    }

    public IScreenResolution getNewResolution() {
        return newResolution;
    }
}
