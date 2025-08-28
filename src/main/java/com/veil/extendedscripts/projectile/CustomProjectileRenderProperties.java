package com.veil.extendedscripts.projectile;

import com.veil.extendedscripts.PacketHandler;
import com.veil.extendedscripts.constants.CustomProjectileRenderType;
import com.veil.extendedscripts.extendedapi.entity.ICustomProjectileRenderProperties;
import net.minecraft.util.ResourceLocation;

public class CustomProjectileRenderProperties implements ICustomProjectileRenderProperties {
    public CustomProjectileRenderProperties(EntityCustomProjectile projectile) {
        this.projectile = projectile;
    }

    private EntityCustomProjectile projectile;
    private byte renderType = CustomProjectileRenderType.Instance.SIMPLE;
    private String texturePath = "textures/entity/arrow.png";
    private ResourceLocation texture = new ResourceLocation(texturePath);
    private int numSimpleRenderPlanes = 2;
    private float rollOffset = 45;
    private float rotationOffset = 0;
    private float rotatingRotation = 0;
    private float forwardOffset = -4.0F;
    private float rotationSpeed = 0;
    private float scale;
    private boolean stopRotatingOnImpact = true;
    private boolean onImpactSnapToInitRotation = true;

    public boolean shouldUseRotatingRotation() {
        return true;
    }

    public byte getRenderType() {
        return renderType;
    }

    public void setRenderType(byte renderType) {
        this.renderType = renderType;
    }

    public String getTexturePath() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectString(EntityCustomProjectile.TEXTURE_ID);
        }

        return texturePath;
    }

    public void setTexture(String texturePath) {
        this.texturePath = texturePath;
        texture = new ResourceLocation(texturePath);
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.TEXTURE_ID, texturePath);
    }

    public ResourceLocation getTexture() {
        String texturePath = projectile.getDataWatcher().getWatchableObjectString(EntityCustomProjectile.TEXTURE_ID);

        if (texturePath != null && !texturePath.isEmpty()) {
            if (texture == null || !texture.getResourcePath().equals(texturePath)) {
                texture = new ResourceLocation(texturePath);
            }
            return texture;
        }

        return new ResourceLocation("minecraft:textures/entity/arrow.png");
    }

    public int getNumSimpleRenderPlanes() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectInt(EntityCustomProjectile.NUM_PLANES_ID);
        }

        return numSimpleRenderPlanes;
    }

    public void setNumSimpleRenderPlanes(int numSimpleRenderPlanes) {
        this.numSimpleRenderPlanes = numSimpleRenderPlanes;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.NUM_PLANES_ID, numSimpleRenderPlanes);
    }

    public float getRollOffset() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.ROLL_OFFSET_ID);
        }

        return rollOffset;
    }

    public void setRollOffset(float rollOffset) {
        this.rollOffset = rollOffset;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.ROLL_OFFSET_ID, rollOffset);
    }

    public float getRotationOffset() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.ROTATION_OFFSET_ID);
        }

        return rotationOffset;
    }

    public void setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.ROTATION_OFFSET_ID, rotationOffset);
    }

    public float getRotatingRotation() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.ROTATING_ROTATION_ID);
        }

        return rotatingRotation;
    }

    public void setRotatingRotation(float rotatingRotation) {
        this.rotatingRotation = rotatingRotation;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.ROTATING_ROTATION_ID, rotatingRotation);
    }

    public float getForwardOffset() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.FORWARD_OFFSET_ID);
        }

        return forwardOffset;
    }

    public void setForwardOffset(float forwardOffset) {
        this.forwardOffset = forwardOffset;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.FORWARD_OFFSET_ID, forwardOffset);
    }

    public float getRotationSpeed() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.ROTATION_SPEED_ID);
        }

        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.ROTATION_SPEED_ID, rotationSpeed);
    }

    public float getScale() {
        if (projectile.worldObj.isRemote) {
            return projectile.getDataWatcher().getWatchableObjectFloat(EntityCustomProjectile.SCALE_ID);
        }

        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        projectile.getDataWatcher().updateObject(EntityCustomProjectile.SCALE_ID, scale);
    }

    public boolean shouldStopRotatingOnImpact() {
        return stopRotatingOnImpact;
    }

    public void setStopRotatingOnImpact(boolean stopRotatingOnImpact) {
        this.stopRotatingOnImpact = stopRotatingOnImpact;
    }

    public boolean shouldOnImpactSnapToInitRotation() {
        return onImpactSnapToInitRotation;
    }

    public void setOnImpactSnapToInitRotation(boolean onImpactSnapToInitRotation) {
        this.onImpactSnapToInitRotation = onImpactSnapToInitRotation;
    }
}
