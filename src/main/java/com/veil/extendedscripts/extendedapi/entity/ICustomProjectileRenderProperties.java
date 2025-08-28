package com.veil.extendedscripts.extendedapi.entity;

import net.minecraft.util.ResourceLocation;

public interface ICustomProjectileRenderProperties {
    byte getRenderType();

    void setRenderType(byte renderType);

    String getTexturePath();

    void setTexture(String texturePath);

    ResourceLocation getTexture();

    int getNumSimpleRenderPlanes();

    void setNumSimpleRenderPlanes(int numSimpleRenderPlanes);

    float getRollOffset();
    void setRollOffset(float rollOffset);

    float getRotationOffset();

    void setRotationOffset(float rotationOffset);

    float getRotatingRotation();

    void setRotatingRotation(float rotatingRotation);

    float getForwardOffset();

    void setForwardOffset(float forwardOffset);

    float getRotationSpeed();

    void setRotationSpeed(float rotationSpeed);

    float getScale();

    void setScale(float scale);

    boolean shouldStopRotatingOnImpact();

    void setStopRotatingOnImpact(boolean stopRotatingOnImpact);

    boolean shouldOnImpactSnapToInitRotation();

    void setOnImpactSnapToInitRotation(boolean onImpactSnapToInitRotation);
}
