package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.item.IExtendedItemCustom;
import com.veil.extendedscripts.item.ExtendedScriptCustomItem;
import com.veil.extendedscripts.item.IScriptedItemVariant;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.event.ItemEvent;

import java.util.HashMap;

public class ExtendedScriptItemEventHandler {
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving == null || event.entityLiving.worldObj == null || event.entityLiving instanceof EntityPlayer || event.entityLiving.ticksExisted % 10 != 0)
            return;

        if (event.entityLiving instanceof EntityCustomNpc) {
            HashMap<Integer, ItemStack> armor = ((EntityCustomNpc) event.entityLiving).inventory.armor;
            HashMap<Integer, ItemStack> weapons = ((EntityCustomNpc) event.entityLiving).inventory.weapons;
            HashMap<Integer, ItemStack>[] inventories = new HashMap[]{armor, weapons};

            for (HashMap<Integer, ItemStack> inventory : inventories) {
                for (ItemStack stack : inventory.values()) {
                    if (stack != null && NoppesUtilServer.isScriptableItem(stack.getItem())) {
                        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
                        EventHooks.onScriptItemUpdate((IItemCustomizable) istack, event.entityLiving);
                    }
                }
            }
        } else if (event.entityLiving.getHeldItem() != null && NoppesUtilServer.isScriptableItem(event.entityLiving.getHeldItem().getItem()) && !event.isCanceled()) {
            IItemStack itemStack = NpcAPI.Instance().getIItemStack(event.entityLiving.getHeldItem());
            EventHooks.onScriptItemUpdate((IItemCustomizable) itemStack, event.entityLiving);
        }
    }

    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {
        if (!isValidContext(event.player) || event.isCanceled())
            return;

        if (event.entityItem == null)
            return;

        EntityItem entityItem = event.entityItem;
        IItemCustomizable customItem = getCustomizable(entityItem.getEntityItem());
        if (customItem != null) {
            event.setCanceled(EventHooks.onScriptItemTossed(customItem, event.player, entityItem));
        }
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (!isValidContext(event.player) || event.isCanceled())
            return;

        IItemCustomizable customItem = getCustomizable(event.player.getHeldItem());
        if (customItem != null) {
            EventHooks.onScriptItemPickedUp(customItem, event.player);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.world.isRemote || !(e.entity instanceof EntityItem))
            return;

        EntityItem itemEnt = (EntityItem) e.entity;
        IItemCustomizable c = getCustomizable(itemEnt.getEntityItem());
        if (c != null) {
            e.setCanceled(EventHooks.onScriptItemSpawn(c, itemEnt));
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        if (!isValidContext(player) || event.isCanceled())
            return;

        IItemCustomizable customItem = getCustomizable(player.getHeldItem());
        IPlayer npcPlayer = NoppesUtilServer.getIPlayer(player);
        if (customItem != null && npcPlayer != null) {
            boolean cancelA = EventHooks.onScriptItemInteract(
                customItem, new ItemEvent.InteractEvent(customItem, npcPlayer, 2, NpcAPI.Instance().getIEntity(event.target))
            );
            boolean cancelB = EventHooks.onScriptItemRightClick(
                customItem, new ItemEvent.RightClickEvent(customItem, npcPlayer, 1, NpcAPI.Instance().getIEntity(event.target))
            );
            if (cancelA || cancelB) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        if (player == null || event.action == null) return;

        if (!isValidContext(event.entityPlayer))
            return;

        // Scriptable-item RIGHT_CLICK hooks
        if (PlayerDataController.Instance == null) {
            return;
        }
        PlayerData playerData = PlayerData.get(player);
        if (playerData == null) {
            return;
        }

        ItemStack held = player.getHeldItem();
        IItemCustomizable customItem = getCustomizable(held);
        IPlayer npcPlayer = NoppesUtilServer.getIPlayer(player);
        if (customItem == null || npcPlayer == null) {
            return;
        }

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (playerData.hadInteract) {
                playerData.hadInteract = false;
                return;
            }
            if (EventHooks.onScriptItemRightClick(
                customItem, new ItemEvent.RightClickEvent(customItem, npcPlayer, 0, null))) {
                event.setCanceled(true);
            }

        } else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            playerData.hadInteract = true;
            IWorld iw = (IWorld) NpcAPI.Instance().getIWorld(event.world);
            Object blockCtx = NpcAPI.Instance().getIBlock(iw, event.x, event.y, event.z);
            if (EventHooks.onScriptItemRightClick(
                customItem, new ItemEvent.RightClickEvent(customItem, npcPlayer, 2, blockCtx))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onStartUseItem(PlayerUseItemEvent.Start event) {
        if (!isValidContext(event.entityPlayer))
            return;

        IItemCustomizable customItem = getCustomizable(event.item);
        IPlayer npcPlayer = NoppesUtilServer.getIPlayer(event.entityPlayer);
        if (customItem != null && npcPlayer != null && EventHooks.onStartUsingCustomItem(customItem, npcPlayer, event.duration)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUseItemTick(PlayerUseItemEvent.Tick event) {
        if (!isValidContext(event.entityPlayer))
            return;

        IItemCustomizable customItem = getCustomizable(event.item);
        IPlayer npcPlayer = NoppesUtilServer.getIPlayer(event.entityPlayer);
        if (customItem != null && npcPlayer != null && EventHooks.onUsingCustomItem(customItem, npcPlayer, event.duration)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onStopUseItem(PlayerUseItemEvent.Stop e) {
        if (!isValidContext(e.entityPlayer))
            return;

        IItemCustomizable c = getCustomizable(e.item);
        IPlayer ip = NoppesUtilServer.getIPlayer(e.entityPlayer);
        if (c != null && ip != null
            && EventHooks.onStopUsingCustomItem(c, ip, e.duration)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFinishUseItem(PlayerUseItemEvent.Finish e) {
        if (!isValidContext(e.entityPlayer))
            return;

        IItemCustomizable c = getCustomizable(e.item);
        IPlayer ip = NoppesUtilServer.getIPlayer(e.entityPlayer);
        if (c != null && ip != null) {
            EventHooks.onFinishUsingCustomItem(c, ip, e.duration);
        }
    }

    /**
     * This method is from CustomNpc+
     */
    private boolean isValidContext(EntityPlayer player) {
        return player != null
            && player.worldObj instanceof WorldServer
            && !player.worldObj.isRemote
            && !(player instanceof FakePlayer);
    }

    /**
     * This method is similar to a CustomNpc+ method by the same name
     */
    private IExtendedItemCustom getCustomizable(ItemStack stack) {
        if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof IScriptedItemVariant)) {
            return null;
        }

        IItemStack raw = NpcAPI.Instance().getIItemStack(stack);
        return (raw instanceof IExtendedItemCustom) ? (IExtendedItemCustom) raw : null;
    }
}
