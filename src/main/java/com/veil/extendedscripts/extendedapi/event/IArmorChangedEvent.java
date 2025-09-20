package com.veil.extendedscripts.extendedapi.event;

import noppes.npcs.api.item.IItemStack;

public interface IArmorChangedEvent {
    IItemStack[] getOldArmor();

    IItemStack[] getNewArmor();

    boolean wasChanged(int slot);

    boolean wasEquipped(int slot);

    boolean wasUnequipped(int slot);
}
