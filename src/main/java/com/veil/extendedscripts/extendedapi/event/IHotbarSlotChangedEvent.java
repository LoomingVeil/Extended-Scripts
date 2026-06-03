package com.veil.extendedscripts.extendedapi.event;

import noppes.npcs.api.event.IPlayerEvent;
import noppes.npcs.api.item.IItemStack;

/**
 * Fired when a player switches their selected hotbar slot.
 *
 * @hookName hotbarSlotChanged
 */
public interface IHotbarSlotChangedEvent extends IPlayerEvent {
    int oldSlot = 0;
    int newSlot = 0;
    IItemStack oldStack = null;
    IItemStack newStack = null;

    int getOldSlot();

    int getNewSlot();

    IItemStack getOldStack();

    IItemStack getNewStack();
}
