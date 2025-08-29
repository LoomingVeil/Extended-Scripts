package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.EntityAttribute;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kamkeel.npcs.controllers.AttributeController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerDataScript;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemScripted;

import java.util.*;

public class CommonEventHandler {
    private final Map<UUID, Integer> lastHotbarSlot = new HashMap<>();
    private final float DEFAULT_JUMP_POWER = 0.42F;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;

            if (!player.worldObj.isRemote) { // Server-side World
                int currentSlot = player.inventory.currentItem; // Get the current hotbar slot index (0-8)
                UUID playerUUID = player.getUniqueID();

                // Get the last known slot for this player
                Integer previousSlot = lastHotbarSlot.get(playerUUID);

                // Check if the slot has actually changed since the last tick

                if (previousSlot == null || previousSlot != currentSlot) {
                    // Hotbar slot has changed!

                    // Get the ItemStacks for the old and new slots (can be null if slot is empty)
                    if (previousSlot == null) {
                        lastHotbarSlot.put(playerUUID, currentSlot);
                    } else {
                        ItemStack oldStack = player.inventory.getStackInSlot(previousSlot);
                        ItemStack newStack = player.inventory.getStackInSlot(currentSlot);

                        HotbarSlotChangedEvent hotbarEvent = new HotbarSlotChangedEvent((IPlayer) AbstractNpcAPI.Instance().getIEntity(player), previousSlot, currentSlot, oldStack, newStack);

                        PlayerDataScript handler = ScriptController.Instance.getPlayerScripts(hotbarEvent.getPlayer());
                        handler.callScript(hotbarEvent.getHookName(), hotbarEvent);
                        AbstractNpcAPI.Instance().events().post(hotbarEvent);

                        // Update the last known slot for the next tick
                        lastHotbarSlot.put(playerUUID, currentSlot);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            if (ExtendedScripts.openScriptingActionKey.isPressed()) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                ItemStack item = player.getHeldItem();

                if (item == null) {
                    return;
                }

                if (item.getItem() instanceof ItemNpcScripter) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("Holding Scripter")
                    );
                    item.getItem().onItemRightClick(item, player.worldObj, player);
                    return;
                } else if (item.getItem() instanceof ItemScripted) {
                    PacketHandler.INSTANCE.sendToServer(new ScriptItemClickPacket());
                    return;
                }

                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatUtils.fillChatWithColor("§cYou must be holding a scripted item and have the proper permissions for this!")
                );
            }
        }
    }


    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ExtendedScripts.worldClippers) {
                ExtendedScripts.worldClippers.setBlockTarget(event.entityPlayer.getHeldItem(), event.world, event.entityPlayer, event.x, event.y, event.z);
            }
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        String[] args = event.parameters;

        if (event.command.getCommandName().equals("gamemode") && event.sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.sender;
            ExtendedScripts.getPlayerProperties(player).syncToPlayer();
        }

        if (!event.command.getCommandName().equals("kamkeel")) {
            return;
        }

        if (args.length == 1 && args[0].equals("attribute")) {
            ChatUtils.sendDelayedChatMessage(event.sender, new ChatComponentText(EnumChatFormatting.GRAY+"For more related commands, see "+EnumChatFormatting.YELLOW+"/veil attribute"), 100);
        }
    }
}
