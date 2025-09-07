package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.ArmorType;
import com.veil.extendedscripts.item.ArmorScripted;
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

        if (heldItem == null || !(heldItem.getItem() instanceof ItemScripted || heldItem.getItem() instanceof ArmorScripted)) {
            return;
        }

        boolean inCreativeMode = player.capabilities.isCreativeMode;
        // Avoid equipping armor when trying to modify it
        if (inCreativeMode && player.isSneaking()) {
            return;
        }

        IItemCustomizable item = (IItemCustomizable) AbstractNpcAPI.Instance().getIItemStack(heldItem);
        // For whatever reason the numbers are backwards in some places
        int armorIndex = 3 - item.getArmorType();

        if (item.getArmorType() == ArmorType.Instance.ALL) {
            ItemStack[] armor = player.inventory.armorInventory;
            for (int i = armor.length - 1; i >= 0; i--) {
                if (player.inventory.armorInventory[i] == null) {
                    player.inventory.armorInventory[i] = heldItem.copy();
                    player.inventory.armorInventory[i].stackSize = 1;

                    if (!inCreativeMode) {
                        player.getHeldItem().stackSize -= 1;
                    }

                    event.setCanceled(true);

                    if (player instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) player;
                        playerMP.sendContainerToPlayer(playerMP.inventoryContainer);
                    }
                    break;
                }
            }

        } else if (item.getArmorType() != ArmorType.Instance.NONE) {
            if (armorIndex < 0 || armorIndex >= player.inventory.armorInventory.length || player.inventory.armorInventory[armorIndex] != null) {
                return;
            }

            player.inventory.armorInventory[armorIndex] = heldItem.copy();
            player.inventory.armorInventory[armorIndex].stackSize = 1;

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
}
