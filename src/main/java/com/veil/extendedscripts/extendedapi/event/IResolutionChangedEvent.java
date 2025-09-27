package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.IScreenResolution;
import noppes.npcs.api.event.IPlayerEvent;

public interface IResolutionChangedEvent extends IPlayerEvent {
    IScreenResolution getOldResolution();

    IScreenResolution getNewResolution();
}
