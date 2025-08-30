package com.veil.extendedscripts;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.items.ItemScripted;

public class ScriptedObjectEventHandler {
    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.world.isRemote) {
            return;
        }

        // Only process right-clicks (air or block)
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR && event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getHeldItem();

        if (heldItem == null || !(heldItem.getItem() instanceof ItemScripted)) {
            return;
        }

        boolean inCreativeMode = player.capabilities.isCreativeMode;
        // Avoid equipping armor when trying to modify it
        if (inCreativeMode && player.isSneaking()) {
            return;
        }

        IItemCustomizable item = (IItemCustomizable) AbstractNpcAPI.Instance().getIItemStack(heldItem);
        // For whatever reason CustomNpcs getArmorType() returns the number backwards
        int armorType = 3 - item.getArmorType();

        if (armorType < 0 || armorType >= player.inventory.armorInventory.length || player.inventory.armorInventory[armorType] != null) {
            return;
        }

        player.inventory.armorInventory[armorType] = heldItem.copy();
        player.inventory.armorInventory[armorType].stackSize = 1;

        if (!inCreativeMode) {
            player.getHeldItem().stackSize -= 1;
        }

        event.setCanceled(true);

        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            playerMP.sendContainerToPlayer(playerMP.inventoryContainer);
        }

    }
}
