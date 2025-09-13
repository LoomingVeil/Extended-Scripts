package com.veil.extendedscripts.extendedapi.event;

import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.entity.IEntity;

/**
 * Called when an equipped scripted item that has the armor_value attribute and is equipped in the armor or hand.
 */
public interface IArmorDamagedEvent {
    /**
     * Gets the amount of durability damage this attack would do to a normal item.
     */
    int getItemDamage();

    IDamageSource getDamageSource();

    /**
     * Get what entity is wearing this item
     */
    IEntity getEquippedOn();

    /**
     * 0-3 helmet -> boots. -1 for hand.
     */
    int getSlot();
}
