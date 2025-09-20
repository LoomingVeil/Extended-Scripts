package com.veil.extendedscripts.event;

import com.veil.extendedscripts.extendedapi.event.IArmorChangedEvent;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.event.player.PlayerEvent;

public class ArmorChangedEvent extends PlayerEvent implements IArmorChangedEvent {
    public IItemStack[] oldArmor;
    public IItemStack[] newArmor;
    public byte[] changeData; // -1 No Change, 0 Unequipped, 1 Equipped

    public ArmorChangedEvent() {
        super(null);
    }

    public ArmorChangedEvent(IPlayer player, ItemStack[] oldArmor, ItemStack[] newArmor, byte[] changeData) {
        super(player);
        AbstractNpcAPI API = NpcAPI.Instance();
        this.oldArmor = new IItemStack[oldArmor.length];
        for (int i = 0; i < oldArmor.length; i++) {
            this.oldArmor[i] = API.getIItemStack(oldArmor[i]);
        }

        this.newArmor = new IItemStack[newArmor.length];
        for (int i = 0; i < newArmor.length; i++) {
            this.newArmor[i] = API.getIItemStack(newArmor[i]);
        }

        this.changeData = changeData;
    }

    @Override
    public String getHookName() {
        return "armorChanged";
    }

    public IItemStack[] getOldArmor() {
        return oldArmor;
    }

    public IItemStack[] getNewArmor() {
        return newArmor;
    }

    public boolean wasChanged(int slot) {
        return changeData[slot] != -1;
    }

    public boolean wasEquipped(int slot) {
        return changeData[slot] == 1;
    }

    public boolean wasUnequipped(int slot) {
        return changeData[slot] == 0;
    }
}
