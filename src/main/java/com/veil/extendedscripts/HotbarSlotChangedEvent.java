package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.event.IHotbarSlotChangedEvent;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class HotbarSlotChangedEvent extends PlayerEvent implements IHotbarSlotChangedEvent {
    public final int oldSlot;
    public final int newSlot;
    public final IItemStack oldStack;
    public final IItemStack newStack;

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
        this.oldStack = NpcAPI.Instance().getIItemStack(oldStack);
        this.newStack = NpcAPI.Instance().getIItemStack(newStack);
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

    public IItemStack getOldStack() {
        return oldStack;
    }

    public IItemStack getNewStack() {
        return newStack;
    }
}
