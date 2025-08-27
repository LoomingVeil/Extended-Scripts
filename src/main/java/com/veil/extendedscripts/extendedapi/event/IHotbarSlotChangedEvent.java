package com.veil.extendedscripts.extendedapi.event;

import noppes.npcs.api.item.IItemStack;

public interface IHotbarSlotChangedEvent {
    int oldSlot = 0;
    int newSlot = 0;
    IItemStack oldStack = null;
    IItemStack newStack = null;

    int getOldSlot();

    int getNewSlot();

    IItemStack getOldStack();

    IItemStack getNewStack();
}
