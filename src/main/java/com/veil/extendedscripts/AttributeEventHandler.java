package com.veil.extendedscripts;

import com.veil.extendedscripts.event.AttributeRecalculateEvent;
import com.veil.extendedscripts.properties.EntityAttribute;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.tracker.AttributeRecalcEvent;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerDataScript;

public class AttributeEventHandler {
    public static void init() {
        AttributeRecalcEvent.registerListener(new AttributeRecalcEventHandler());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote) {
            return; // only run once, server side
        }

        World world = event.world;
        for (Object obj : world.loadedEntityList) {
            if (!(obj instanceof IProjectile || obj instanceof EntityItem)) continue;
            Entity entity = (Entity) obj;
            ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(entity);
            /*if (!properties.getCanMove()) {
                if (event.entity instanceof EntityPlayer) {
                    EntityPlayer genericPlayer = (EntityPlayer) event.entity;
                }
            }*/

            boolean isSwimming = entity.isInWater();

            if (!isSwimming) {
                float vanillaGravityEffect = 0.08F; // Base vanilla gravity pull per tick

                applyEntityGravity(
                    properties,
                    entity,
                    vanillaGravityEffect,
                    properties.get(EntityAttribute.GRAVITY),
                    properties.get(EntityAttribute.DOWNWARD_GRAVITY),
                    properties.get(EntityAttribute.UPWARD_GRAVITY)
                );
            } else {
                float vanillaGravityEffect = 0.02F; // Base vanilla underwater gravity pull per tick

                applyEntityGravity(
                    properties,
                    entity,
                    vanillaGravityEffect,
                    properties.get(EntityAttribute.UNDERWATER_GRAVITY),
                    properties.get(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY),
                    properties.get(EntityAttribute.UNDERWATER_UPWARD_GRAVITY)
                );
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;

            if (player.worldObj.isRemote) {
                EntityPlayerSP ClientPlayer = (EntityPlayerSP) player;

                boolean isFlying = ClientPlayer.capabilities.isFlying;
                boolean isMovingUp = ClientPlayer.movementInput.jump;
                boolean isMovingDown = ClientPlayer.movementInput.sneak;

                if (isFlying) {
                    float verticalFlightSpeed = ExtendedAPI.getAttribute(player, PlayerAttribute.FLIGHT_SPEED_VERTICAL) * 0.01F;
                    if (verticalFlightSpeed != 0) {
                        if (isMovingUp && !isMovingDown) {
                            player.motionY = verticalFlightSpeed * 0.2;
                        } else if (isMovingDown && !isMovingUp) {
                            player.motionY = -verticalFlightSpeed * 0.2;
                        }
                    }
                }
            }

            float horzMoveMultiplier = ExtendedAPI.getAttribute(player, EntityAttribute.JUMP_POWER_HORIZONTAL) * 0.01F;
            float speedMultiplier = 0.01F;

            if (player.moveForward > 0.001 && !player.onGround && !player.capabilities.isFlying && !player.isInWater()) {
                player.motionX += -Math.sin(Math.toRadians(player.rotationYaw)) * (horzMoveMultiplier) * speedMultiplier;
                player.motionZ +=  Math.cos(Math.toRadians(player.rotationYaw)) * (horzMoveMultiplier) * speedMultiplier;
            }

            float waterSwimBoost = ExtendedAPI.getAttribute(player, PlayerAttribute.SWIM_BOOST_WATER) * 0.01F;
            if (player.moveForward > 0.001 && !player.capabilities.isFlying && player.isInWater()) {
                player.motionX += -Math.sin(Math.toRadians(player.rotationYaw)) * (waterSwimBoost) * speedMultiplier;
                player.motionZ +=  Math.cos(Math.toRadians(player.rotationYaw)) * (waterSwimBoost) * speedMultiplier;
            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && ExtendedScriptPlayerProperties.get((EntityPlayer) event.entity) == null) {
            ExtendedScriptPlayerProperties.register((EntityPlayer) event.entity);
            ExtendedScriptEntityProperties.register(event.entity);
        }
    }

    // Copies extended properties from the old player entity to the new one upon death/respawn
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
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
            // boolean canFly = origPlayerProps.getCanFly();
            // boolean lastSeenFlying = newPlayerProps.getLastSeenFlying();
            // event.entityPlayer.capabilities.allowFlying = canFly;
            // event.entityPlayer.capabilities.isFlying = lastSeenFlying;
            // event.entityPlayer.sendPlayerAbilities();
        }
    }

    @SubscribeEvent
    public void onRespawn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
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
    public void onChangedDimension(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        ExtendedScriptEntityProperties.get(event.player).syncToPlayer();
        ExtendedScriptPlayerProperties.get(event.player).syncToPlayer();
    }

    @SubscribeEvent
    public void onLogin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        // System.out.println("Attempting to sync player properties.");

        ExtendedScriptEntityProperties.get(event.player).syncToPlayer();
        ExtendedScriptPlayerProperties playerProperties = ExtendedScriptPlayerProperties.get(event.player);

        boolean lastSeenState = playerProperties.getLastSeenFlying();
        if (lastSeenState) {
            // event.player.capabilities.isFlying = true;
            // event.player.sendPlayerAbilities();
        }


        playerProperties.syncToPlayer();

        AttributeController.getTracker(event.player).recalcAttributes(event.player);
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if (event.entity instanceof EntityPlayer) {
            ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties((EntityPlayer) event.entity);
            if (properties.get(PlayerAttribute.CAN_FLY)) {
                event.setCanceled(true);
            }
        }

        if (event.isCanceled()) return;

        if (event.entity instanceof EntityPlayer) {
            float maxFallDistance = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.MAX_FALL_DISTANCE);
            event.distance -= (maxFallDistance);
        } else {
            ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
            float maxFallDistance = properties.get(EntityAttribute.MAX_FALL_DISTANCE);
            event.distance -= (maxFallDistance - 3);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.entity instanceof EntityPlayer) {
            float jumpBoost = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.JUMP_POWER_VERTICAL) * 0.01F;
            event.entity.motionY += jumpBoost * 0.1F;
        } else {
            ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
            float jumpBoost = properties.get(EntityAttribute.JUMP_POWER_VERTICAL);
            event.entity.motionY += (jumpBoost - 1) * 0.1F;
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        ExtendedScriptEntityProperties properties = ExtendedScripts.getEntityProperties(event.entity);
        /*if (!properties.getCanMove()) {
            if (event.entity instanceof EntityPlayer) {
                EntityPlayer genericPlayer = (EntityPlayer) event.entity;
            }
        }*/

        boolean isFlying = false;
        boolean isSwimming = event.entity.isInWater();
        if (event.entity instanceof EntityPlayer) {
            isFlying = ((EntityPlayer) (event.entity)).capabilities.isFlying;
        }
        boolean onGround = event.entity.onGround;

        float gravity, downwardGravity, upwardGravity;
        if (!isFlying && !isSwimming) {
            float vanillaGravityEffect = 0.08F; // Base vanilla gravity pull per tick

            if (event.entity instanceof EntityPlayer) {
                gravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.GRAVITY) * 0.01F;
                downwardGravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.DOWNWARD_GRAVITY) * 0.01F;
                upwardGravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.UPWARD_GRAVITY) * 0.01F;

                if (Math.abs(downwardGravity) < 0.0001) {
                    downwardGravity = gravity;
                }
                if (Math.abs(upwardGravity) < 0.0001) {
                    upwardGravity = gravity;
                }

                if (event.entity.motionY > 0) {
                    event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (upwardGravity + 1));
                } else if (event.entity.motionY < 0) {
                    event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (downwardGravity + 1));
                } else {
                    if (upwardGravity < 0) {
                        event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (upwardGravity + 1));
                    }
                }
            } else {
                applyEntityGravity(
                    properties,
                    event.entity,
                    vanillaGravityEffect,
                    properties.get(EntityAttribute.GRAVITY),
                    properties.get(EntityAttribute.DOWNWARD_GRAVITY),
                    properties.get(EntityAttribute.UPWARD_GRAVITY)
                );
            }
        } else if (!isFlying && !onGround) {
            float vanillaGravityEffect = 0.02F; // Base vanilla underwater gravity pull per tick

            if (event.entity instanceof EntityPlayer) {
                gravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.UNDERWATER_GRAVITY) * 0.01F;
                downwardGravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY) * 0.01F;
                upwardGravity = ExtendedAPI.getAttribute((EntityPlayer) event.entity, EntityAttribute.UNDERWATER_UPWARD_GRAVITY) * 0.01F;

                if (Math.abs(downwardGravity) < 0.0001) {
                    downwardGravity = gravity;
                }
                if (Math.abs(upwardGravity) < 0.0001) {
                    upwardGravity = gravity;
                }

                if (event.entity.motionY > 0) {
                    event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (upwardGravity + 1));
                } else if (event.entity.motionY < 0) {
                    event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (downwardGravity + 1));
                } else {
                    if (upwardGravity < 0) {
                        event.entity.motionY += vanillaGravityEffect - (vanillaGravityEffect * (upwardGravity + 1));
                    }
                }
            } else {
                applyEntityGravity(
                    properties,
                    event.entity,
                    vanillaGravityEffect,
                    properties.get(EntityAttribute.UNDERWATER_GRAVITY),
                    properties.get(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY),
                    properties.get(EntityAttribute.UNDERWATER_UPWARD_GRAVITY)
                );
            }
        }
    }

    private void applyEntityGravity(ExtendedScriptEntityProperties properties, Entity entity, float baseGravity, float gravity, float downwardGravity, float upwardGravity) {
        if (Math.abs(downwardGravity + 1) < 0.0001) {
            downwardGravity = gravity;
        }
        if (Math.abs(upwardGravity + 1) < 0.0001) {
            upwardGravity = gravity;
        }

        if (entity.motionY > 0) {
            entity.motionY += baseGravity - (baseGravity * upwardGravity);
        } else if (entity.motionY < 0) {
            entity.motionY += baseGravity - (baseGravity * downwardGravity);
        } else {
            if (upwardGravity < 0) {
                entity.motionY += baseGravity - (baseGravity * upwardGravity);
            }
        }
    }
}

class AttributeRecalcEventHandler implements AttributeRecalcEvent.Listener {
    @Override
    public void onPost(EntityPlayer player, PlayerAttributeTracker playerAttributeTracker) {
        float flightSpeed = ExtendedAPI.getAttribute(player, PlayerAttribute.FLIGHT_SPEED_HORIZONTAL) * 0.01F * 0.05F;
        if (player.capabilities.getFlySpeed() != flightSpeed + 0.05F) {
            player.capabilities.setFlySpeed(flightSpeed + 0.05F);
            player.sendPlayerAbilities();
        }

        AttributeRecalculateEvent attributeRecalcEvent = new AttributeRecalculateEvent((IPlayer) AbstractNpcAPI.Instance().getIEntity(player));

        PlayerDataScript handler = ScriptController.Instance.getPlayerScripts(attributeRecalcEvent.getPlayer());
        handler.callScript(attributeRecalcEvent.getHookName(), attributeRecalcEvent);
        AbstractNpcAPI.Instance().events().post(attributeRecalcEvent);
    }
}
