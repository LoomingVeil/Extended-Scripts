package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.constants.DataType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.EnumMap;

public class ExtendedScriptEntityProperties implements IExtendedEntityProperties {
    public static final String PROPERTY_ID = ExtendedScripts.MODID + "_EntityProperties";
    private final Entity entity;
    private final EnumMap<EntityAttribute, Object> entityAttributes = new EnumMap<>(EntityAttribute.class);

    public ExtendedScriptEntityProperties(Entity entity) {
        this.entity = entity;
        for (EntityAttribute attr : EntityAttribute.values()) {
            entityAttributes.put(attr, attr.getDefaultValue());
        }
    }

    // Static helper to register properties onto an entity
    public static void register(Entity entity) {
        if (entity != null) {
            entity.registerExtendedProperties(PROPERTY_ID, new ExtendedScriptEntityProperties(entity));
        } else {
            System.out.println("Entity is null");
        }
    }


    // Static helper to retrieve properties from an entity
    public static ExtendedScriptEntityProperties get(Entity entity) {
        return (ExtendedScriptEntityProperties) entity.getExtendedProperties(PROPERTY_ID);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound savedNBT = new NBTTagCompound();

        for (EntityAttribute attr : entityAttributes.keySet()) {
            if (attr.getType() == Float.class) {
                savedNBT.setFloat(attr.asCamelCase(), (float) entityAttributes.get(attr));
            } else if (attr.getType() == Boolean.class) {
                savedNBT.setBoolean(attr.asCamelCase(), (boolean) entityAttributes.get(attr));
            }
        }

        compound.setTag("extendedEntityData", savedNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("extendedEntityData", 10)) {
            NBTTagCompound savedNBT = compound.getCompoundTag("extendedEntityData");
            for (EntityAttribute attr : entityAttributes.keySet()) {
                if (savedNBT.hasKey(attr.asCamelCase(), DataType.Instance.valueOf(attr.getType().getTypeName()))) {
                    if (attr.getType() == Float.class) {
                        entityAttributes.put(attr, savedNBT.getFloat(attr.asCamelCase()));
                    } else if (attr.getType() == Boolean.class) {
                        entityAttributes.put(attr, savedNBT.getBoolean(attr.asCamelCase()));
                    }
                }
            }
        }
    }

    public void init(Entity entity, World world) {
        // No special initialization needed here as loadNBTData handles data loading
    }

    @SuppressWarnings("unchecked")
    public <T> T get(EntityAttribute attr) {
        return (T) entityAttributes.get(attr);
    }

    // Generic setter
    public <T> void set(EntityAttribute attr, T value) {
        if (!attr.getType().isInstance(value)) {
            throw new IllegalArgumentException("Invalid type for " + attr + ". Expected " + attr.getType());
        }
        entityAttributes.put(attr, value);
    }

    public void syncToPlayer() {
        if (!this.entity.worldObj.isRemote && this.entity instanceof EntityPlayerMP) {
            PacketHandler.INSTANCE.sendTo(new EntityPropertyUpdateMessage(this), (EntityPlayerMP) this.entity);
        }
    }

    /*public float getGravity() {
        return gravity;
    }

    public void setGravity(float value) {
        this.gravity = value;
        syncToPlayer();
    }

    public float getDownwardGravity() {
        return downwardGravity;
    }

    public void setDownwardGravity(float downwardGravity) {
        this.downwardGravity = downwardGravity;
        syncToPlayer();
    }

    public float getUpwardGravity() {
        return upwardGravity;
    }

    public void setUpwardGravity(float upwardGravity) {
        this.upwardGravity = upwardGravity;
        syncToPlayer();
    }

    public float getUnderwaterGravity() {
        return underwaterGravity;
    }

    public void setUnderwaterGravity(float underwaterGravity) {
        this.underwaterGravity = underwaterGravity;
        syncToPlayer();
    }

    public float getUnderwaterDownwardGravity() {
        return underwaterDownwardGravity;
    }

    public void setUnderwaterDownwardGravity(float underwaterDownwardGravity) {
        this.underwaterDownwardGravity = underwaterDownwardGravity;
        syncToPlayer();
    }

    public float getUnderwaterUpwardGravity() {
        return underwaterUpwardGravity;
    }

    public void setUnderwaterUpwardGravity(float underwaterUpwardGravity) {
        this.underwaterUpwardGravity = underwaterUpwardGravity;
        syncToPlayer();
    }

    public float getVerticalJumpPower() {
        return verticalJumpPower;
    }

    public void setVerticalJumpPower(float verticalJumpPower) {
        this.verticalJumpPower = verticalJumpPower;
        syncToPlayer();
    }

    public float getHorizontalJumpPower() {
        return horizontalJumpPower;
    }

    public void setHorizontalJumpPower(float horizontalJumpPower) {
        this.horizontalJumpPower = horizontalJumpPower;
        syncToPlayer();
    }

    public float getMaxFallDistance() {
        return maxFallDistance;
    }

    public void setMaxFallDistance(float maxFallDistance) {
        this.maxFallDistance = maxFallDistance;
        syncToPlayer();
    }

    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }*/
}
