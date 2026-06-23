package com.veil.extendedscripts;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BaublesCompatability {
    public static ItemStack[] getPlayerBaubles(EntityPlayer player) {
        IInventory baublesInv = BaublesApi.getBaubles(player);
        if (baublesInv == null) return null;

        ItemStack[] stacks = new ItemStack[baublesInv.getSizeInventory()];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = baublesInv.getStackInSlot(i);
        }
        return stacks;
    }

    public static IInventory getBaublesInventory(EntityPlayer player) {
        return BaublesApi.getBaubles(player);
    }
}
