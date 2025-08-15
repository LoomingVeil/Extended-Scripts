package com.veil.extendedscripts;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HotbarSlotChangedEvent extends Event {
    public final EntityPlayer player;
    public final int oldSlot;
    public final int newSlot;
    public final ItemStack oldStack; // Item that was in the old slot (can be null)
    public final ItemStack newStack; // Item that is now in the new slot (can be null)

    public HotbarSlotChangedEvent(EntityPlayer player, int oldSlot, int newSlot, ItemStack oldStack, ItemStack newStack) {
        this.player = player;
        this.oldSlot = oldSlot;
        this.newSlot = newSlot;
        this.oldStack = oldStack;
        this.newStack = newStack;
    }
}
