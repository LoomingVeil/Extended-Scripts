package com.veil.extendedscripts;

import com.veil.extendedscripts.event.HotbarSlotChangedEvent;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

            if (!player.worldObj.isRemote) {
                int currentSlot = player.inventory.currentItem;
                UUID playerUUID = player.getUniqueID();
                Integer previousSlot = lastHotbarSlot.get(playerUUID);

                if (previousSlot == null || previousSlot != currentSlot) {
                    if (previousSlot == null) {
                        lastHotbarSlot.put(playerUUID, currentSlot);
                    } else {
                        ItemStack oldStack = player.inventory.getStackInSlot(previousSlot);
                        ItemStack newStack = player.inventory.getStackInSlot(currentSlot);

                        HotbarSlotChangedEvent hotbarEvent = new HotbarSlotChangedEvent((IPlayer) AbstractNpcAPI.Instance().getIEntity(player), previousSlot, currentSlot, oldStack, newStack);

                        PlayerDataScript handler = ScriptController.Instance.getPlayerScripts(hotbarEvent.getPlayer());
                        handler.callScript(hotbarEvent.getHookName(), hotbarEvent);
                        AbstractNpcAPI.Instance().events().post(hotbarEvent);

                        lastHotbarSlot.put(playerUUID, currentSlot);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null) {
            if (ClientProxy.openScriptingActionKey.isPressed()) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                ItemStack item = player.getHeldItem();

                if (item == null) {
                    return;
                }

                if (item.getItem() instanceof ItemNpcScripter) {
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

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
            boolean keepInventoryEnabled = event.entity.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory");

            if ((boolean) props.get(PlayerAttribute.KEEP_INVENTORY) && !keepInventoryEnabled) {
                event.entityLiving.capturedDrops.clear();

                ItemStack[] tempInvStorage = new ItemStack[player.inventory.mainInventory.length];
                for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                    if (player.inventory.mainInventory[i] != null) {
                        tempInvStorage[i] = player.inventory.mainInventory[i].copy();
                    }
                }
                props.tempInvStorage = tempInvStorage;
                props.xpLevel = player.experienceLevel;
                props.xpTotal = player.experienceTotal;
                props.xp = player.experience;
                props.score = player.getScore();

                player.inventory.clearInventory(null, -1);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.wasDeath) {
            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(event.original);
            if (!((boolean) props.get(PlayerAttribute.KEEP_INVENTORY)) || event.original.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) return;

            // Assuming `props.tempInvStorage` is an ItemStack[]
            if (props.tempInvStorage != null) {
                // Manually copy the items from our saved array to the new player's inventory
                for (int i = 0; i < props.tempInvStorage.length; i++) {
                    event.entityPlayer.inventory.mainInventory[i] = props.tempInvStorage[i];
                }
            }

            /*int count = 0;
            ItemStack[] inv = event.entityPlayer.inventory.mainInventory;
            for (int i = 0; i < inv.length; i++) {
                if (inv[i] != null) {
                    count += 1;
                }
            }*/

            event.entityPlayer.experienceLevel = props.xpLevel;
            event.entityPlayer.experienceTotal = props.xpTotal;
            event.entityPlayer.experience = props.xp;
            event.entityPlayer.setScore(props.score);
        }
    }

    @SubscribeEvent
    public void onLogin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        ((noppes.npcs.extendedapi.entity.IPlayer) AbstractNpcAPI.Instance().getPlayer(event.player.getDisplayName())).resyncScreenSize();
    }
}
