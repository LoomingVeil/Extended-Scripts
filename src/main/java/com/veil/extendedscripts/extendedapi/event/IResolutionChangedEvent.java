package com.veil.extendedscripts.extendedapi.event;

import com.veil.extendedscripts.extendedapi.IScreenResolution;
import noppes.npcs.api.event.IPlayerEvent;

/**
 * Fired when a player changes their screen resolution.
 *
 * @hookName resolutionChanged
 */
public interface IResolutionChangedEvent extends IPlayerEvent {
    IScreenResolution getOldResolution();

    IScreenResolution getNewResolution();
}
