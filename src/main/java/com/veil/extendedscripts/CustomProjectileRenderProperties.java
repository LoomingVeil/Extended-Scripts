package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.CustomProjectileRenderType;
import net.minecraft.util.ResourceLocation;

public class CustomProjectileRenderProperties {
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
    private float forwardOffset = -4.0F;
    private float scale;

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
}
