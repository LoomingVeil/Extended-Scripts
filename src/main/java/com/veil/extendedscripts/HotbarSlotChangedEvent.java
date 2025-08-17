package com.veil.extendedscripts;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class HotbarSlotChangedEvent extends PlayerEvent {
    public final int oldSlot;
    public final int newSlot;
    public final ItemStack oldStack;
    public final ItemStack newStack;

    public HotbarSlotChangedEvent() {
        super(null);
        this.oldSlot = 0;
        this.newSlot = 0;
        this.oldStack = null;
        this.newStack = null;
    }

    public HotbarSlotChangedEvent(IPlayer player, int oldSlot, int newSlot, ItemStack oldStack, ItemStack newStack) {
        super(player);
        this.oldSlot = oldSlot;
        this.newSlot = newSlot;
        this.oldStack = oldStack;
        this.newStack = newStack;
    }

    public String getHookName() {
        return "hotbarSlotChanged";
    }

    public int getOldSlot() {
        return oldSlot;
    }

    public int getNewSlot() {
        return newSlot;
    }

    public ItemStack getOldStack() {
        return oldStack;
    }

    public ItemStack getNewStack() {
        return newStack;
    }
}
