package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.Sys;

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

                        HotbarSlotChangedEvent customEvent = new HotbarSlotChangedEvent(player, previousSlot, currentSlot, oldStack, newStack);
                        MinecraftForge.EVENT_BUS.post(customEvent);
                        // NpcAPI.EVENT_BUS.post(customEvent);

                        // Update the last known slot for the next tick
                        lastHotbarSlot.put(playerUUID, currentSlot);
                    }
                }
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
            ChatUtils.sendDelayedChatMessage(event.sender, new ChatComponentText(EnumChatFormatting.GRAY+"For adding and removing attributes, see "+EnumChatFormatting.YELLOW+"/veil attribute"), 100);
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && ExtendedScriptPlayerProperties.get((EntityPlayer) event.entity) == null) {
            ExtendedScriptPlayerProperties.register((EntityPlayer) event.entity);
            ExtendedScriptEntityProperties.register(event.entity);
        }
    }

    // Copies extended properties from the old player entity to the new one upon death/respawn
    @SubscribeEvent
    public void onPlayerClone(Clone event) {
        System.out.println("Attempting to sync player properties.");
        ExtendedScriptPlayerProperties origPlayerProps = ExtendedScriptPlayerProperties.get(event.original);
        ExtendedScriptPlayerProperties newPlayerProps = ExtendedScriptPlayerProperties.get(event.entityPlayer);
        if (origPlayerProps != null && newPlayerProps != null) {
            NBTTagCompound compound = new NBTTagCompound();
            origPlayerProps.saveNBTData(compound);
            newPlayerProps.loadNBTData(compound);
        }

        ExtendedScriptEntityProperties origEntityProps = ExtendedScriptEntityProperties.get(event.original);
        ExtendedScriptEntityProperties newEntityProps = ExtendedScriptEntityProperties.get(event.entityPlayer);
        if (origEntityProps != null && newEntityProps != null) {
            NBTTagCompound compound = new NBTTagCompound();
            origEntityProps.saveNBTData(compound);
            newEntityProps.loadNBTData(compound);
            boolean canFly = origPlayerProps.getCanFly();
            boolean lastSeenFlying = newPlayerProps.getLastSeenFlying();
            event.entityPlayer.capabilities.allowFlying = canFly;
            // event.entityPlayer.capabilities.isFlying = lastSeenFlying;
            event.entityPlayer.sendPlayerAbilities();
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ExtendedScriptEntityProperties.get(event.player).syncToPlayer();
        ExtendedScriptPlayerProperties.get(event.player).syncToPlayer();
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            ExtendedScriptPlayerProperties.get(player).setLastSeenFlying(player.capabilities.isFlying);
        }
    }

    @SubscribeEvent
    public void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ExtendedScriptEntityProperties.get(event.player).syncToPlayer();
        ExtendedScriptPlayerProperties.get(event.player).syncToPlayer();
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Attempting to sync player properties.");

        ExtendedScriptEntityProperties.get(event.player).syncToPlayer();
        ExtendedScriptPlayerProperties playerProperties = ExtendedScriptPlayerProperties.get(event.player);

        boolean lastSeenState = playerProperties.getLastSeenFlying();
        if (lastSeenState) {
            // event.player.capabilities.isFlying = true;
            // event.player.sendPlayerAbilities();
        }


        playerProperties.syncToPlayer();
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if (event.entity instanceof EntityPlayer) {
            ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties((EntityPlayer) event.entity);
            if (properties.getCanFly()) {
                event.setCanceled(true);
            }
        }

        if (event.isCanceled()) return;

        ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
        float maxFallDistance = properties.getMaxFallDistance();
        event.distance -= (maxFallDistance - 3);
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
        float jumpBoost = properties.getVerticalJumpPower();
        event.entity.motionY += 0.225 * (jumpBoost - 1);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!event.entity.worldObj.isRemote) {
            return;
        }

        if (event.entity instanceof EntityPlayer) {
            EntityPlayer genericPlayer = (EntityPlayer) event.entity;
            EntityPlayerSP ClientPlayer = (EntityPlayerSP) event.entity;

            boolean isFlying = ClientPlayer.capabilities.isFlying;
            boolean isMovingUp = ClientPlayer.movementInput.jump;
            boolean isMovingDown = ClientPlayer.movementInput.sneak;

            if (isFlying) {
                ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(ClientPlayer);
                float verticalFlightSpeed = properties.getVerticalFlightSpeed();
                if (isMovingUp && !isMovingDown) {
                    genericPlayer.motionY = verticalFlightSpeed * 0.2;
                } else if (isMovingDown && !isMovingUp) {
                    genericPlayer.motionY = -verticalFlightSpeed * 0.2;
                }
            }
        }

        ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
        if (!properties.getCanMove()) {
            if (event.entity instanceof EntityPlayer) {
                EntityPlayer genericPlayer = (EntityPlayer) event.entity;
            }
        }

        boolean isFlying = false;
        boolean isSwimming = event.entity.isInWater();
        if (event.entity instanceof EntityPlayer) {
            isFlying = ((EntityPlayer) (event.entity)).capabilities.isFlying;
        }
        boolean onGround = event.entity.onGround;

        double gravity, downwardGravity, upwardGravity;
        if (!isFlying && !onGround && !isSwimming) {
            gravity = properties.getGravity();
            double vanillaGravityEffect = 0.08D; // Base vanilla gravity pull per tick
            downwardGravity = properties.getDownwardGravity();
            upwardGravity = properties.getUpwardGravity();

            if (Math.abs(downwardGravity + 1) < 0.0001) {
                downwardGravity = gravity;
            }
            if (Math.abs(upwardGravity + 1) < 0.0001) {
                upwardGravity = gravity;
            }

            if (event.entity.motionY > 0) {
                event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * upwardGravity);
            } else if (event.entity.motionY < 0) {
                event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * downwardGravity);
            }
        } else if (!isFlying && !onGround) {
            gravity = properties.getUnderwaterGravity();
            double vanillaGravityEffect = 0.02D; // Base vanilla underwater gravity pull per tick
            downwardGravity = properties.getUnderwaterDownwardGravity();
            upwardGravity = properties.getUnderwaterUpwardGravity();

            if (Math.abs(downwardGravity + 1) < 0.0001) {
                downwardGravity = gravity;
            }
            if (Math.abs(upwardGravity + 1) < 0.0001) {
                upwardGravity = gravity;
            }

            if (event.entity.motionY > 0) {
                event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * upwardGravity);
            } else if (event.entity.motionY < 0) {
                event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * downwardGravity);
            }
        }
    }
}
