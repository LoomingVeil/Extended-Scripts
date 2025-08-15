package com.veil.extendedscripts.properties;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedScriptEntityProperties implements IExtendedEntityProperties {
    public static final String PROPERTY_ID = ExtendedScripts.MODID + "_EntityProperties";
    private final Entity entity;
    private float gravity = 1;
    private float downwardGravity = -1;
    private float upwardGravity = -1;
    private float underwaterGravity = 1;
    private float underwaterDownwardGravity = -1;
    private float underwaterUpwardGravity = -1;
    private float verticalJumpPower = 1;
    private float horizontalJumpPower = 1;
    private float maxFallDistance = 3;
    private boolean canMove = true;

    public ExtendedScriptEntityProperties(Entity entity) {
        this.entity = entity;
    }

    // Static helper to register properties onto an entity
    public static void register(Entity entity) {
        if (entity != null) {
            entity.registerExtendedProperties(PROPERTY_ID, new ExtendedScriptEntityProperties(entity));
        } else {
            System.out.println("Entity is null");
        }
    }


    // Static helper to retrieve properties from a entity
    public static ExtendedScriptEntityProperties get(Entity entity) {
        return (ExtendedScriptEntityProperties) entity.getExtendedProperties(PROPERTY_ID);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound savedNBT = new NBTTagCompound();
        savedNBT.setFloat("Gravity", this.gravity);
        savedNBT.setFloat("DownwardGravity", this.downwardGravity);
        savedNBT.setFloat("UpwardGravity", this.upwardGravity);
        savedNBT.setFloat("UnderwaterGravity", this.underwaterGravity);
        savedNBT.setFloat("UnderwaterDownwardGravity", this.underwaterDownwardGravity);
        savedNBT.setFloat("UnderwaterUpwardGravity", this.underwaterUpwardGravity);
        savedNBT.setFloat("VerticalJumpPower", this.verticalJumpPower);
        savedNBT.setFloat("HorizontalJumpPower", this.horizontalJumpPower);
        savedNBT.setFloat("maxFallDistance", this.maxFallDistance);
        savedNBT.setBoolean("canMove", false);
        compound.setTag("EntityData", savedNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("EntityData", 10)) {
            NBTTagCompound savedNBT = compound.getCompoundTag("EntityData");
            if (savedNBT.hasKey("Gravity", 5)) {
                this.gravity = savedNBT.getFloat("Gravity");
            }

            if (savedNBT.hasKey("DownwardGravity", 5)) {
                this.downwardGravity = savedNBT.getFloat("DownwardGravity");
            }

            if (savedNBT.hasKey("UpwardGravity", 5)) {
                this.upwardGravity = savedNBT.getFloat("UpwardGravity");
            }

            if (savedNBT.hasKey("UnderwaterGravity", 5)) {
                this.underwaterGravity = savedNBT.getFloat("UnderwaterGravity");
            }

            if (savedNBT.hasKey("UnderwaterDownwardGravity", 5)) {
                this.underwaterDownwardGravity = savedNBT.getFloat("UnderwaterDownwardGravity");
            }

            if (savedNBT.hasKey("UnderwaterUpwardGravity", 5)) {
                this.underwaterUpwardGravity = savedNBT.getFloat("UnderwaterUpwardGravity");
            }

            if (savedNBT.hasKey("VerticalJumpPower", 5)) {
                this.verticalJumpPower = savedNBT.getFloat("VerticalJumpPower");
            }

            if (savedNBT.hasKey("HorizontalJumpPower", 5)) {
                this.horizontalJumpPower = savedNBT.getFloat("HorizontalJumpPower");
            }

            if (savedNBT.hasKey("maxFallDistance", 5)) {
                this.maxFallDistance = savedNBT.getFloat("maxFallDistance");
            }
        }
    }

    public void syncToPlayer() {
        if (!this.entity.worldObj.isRemote && this.entity instanceof EntityPlayerMP) {
            PacketHandler.INSTANCE.sendTo(new EntityPropertyUpdateMessage(this), (EntityPlayerMP) this.entity);
        }
    }

    public void init(Entity entity, World world) {
        // No special initialization needed here as loadNBTData handles data loading
    }

    public float getGravity() {
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
    }
}
