package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.scripted.entity.ScriptEntity;
import noppes.npcs.scripted.entity.ScriptPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptEntity.class})
public class MixinEntityExtension {
    @Shadow
    protected Entity entity;

    @Unique
    /**
     * Sets the gravity of the entity. Ex 0.1 for 10% gravity, 10 for 1000% gravity
     */
    public void setGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setGravity(value);
    }

    @Unique
    public float getGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getGravity();
    }

    /**
     * Sets the entity's gravity when moving upward. Upward gravity overrides general gravity's upward force.
     * @param value gravity strength. -1 for disabled
     */
    public void setUpwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUpwardGravity(value);
    }

    public float getUpwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUpwardGravity();
    }

    /**
     * Sets the entity's gravity when falling downward. Downward gravity overrides general gravity's downward force.
     * @param value gravity strength. -1 for disabled
     */
    public void setDownwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setDownwardGravity(value);
    }

    public float getDownwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getDownwardGravity();
    }

    public void setUnderwaterGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterGravity(value);
    }

    public float getUnderwaterGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getDownwardGravity();
    }

    public void setUnderwaterUpwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterUpwardGravity(value);
    }

    public float getUnderwaterUpwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUnderwaterUpwardGravity();
    }

    public void setUnderwaterDownwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterDownwardGravity(value);
    }

    public float getUnderwaterDownwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUnderwaterDownwardGravity();
    }

    /**
     * Sets the max fall distance before an entity takes fall damage. Default is 3.
     */
    public void setMaxFallDistance(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setMaxFallDistance(value);
    }

    public float getMaxFallDistance() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getMaxFallDistance();
    }

    public void setCanMove(boolean value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setCanMove(value);
    }

    public boolean getCanMove() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getCanMove();
    }

    /**
     * Modify how high you go when you jump. Default is 1.
     * @param value
     */
    public void setJumpBoost(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setVerticalJumpPower(value);
    }

    public float getJumpBoost() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getVerticalJumpPower();
    }

    /*
    public void setJumpBreadth(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setHorizontalJumpPower(value);
    }

    public float getJumpBreadth() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getHorizontalJumpPower();
    }
     */
}
