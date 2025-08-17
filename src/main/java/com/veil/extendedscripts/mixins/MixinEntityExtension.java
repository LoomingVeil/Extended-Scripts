package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import net.minecraft.entity.Entity;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.entity.ScriptEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

@Mixin(value={ScriptEntity.class})
public abstract class MixinEntityExtension implements noppes.npcs.extendedapi.entity.IEntity {
    @Shadow
    protected Entity entity;

    @Shadow
    public abstract void setStoredData(String key, Object value);

    @Shadow
    public abstract void removeStoredData(String key);

    private ArrayList<String> itemKeys = new ArrayList<>();

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

    @Unique
    /**
     * Sets the entity's gravity when moving upward. Upward gravity overrides general gravity's upward force.
     * @param value gravity strength. -1 for disabled
     */
    public void setUpwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUpwardGravity(value);
    }

    @Unique
    public float getUpwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUpwardGravity();
    }

    @Unique
    /**
     * Sets the entity's gravity when falling downward. Downward gravity overrides general gravity's downward force.
     * @param value gravity strength. -1 for disabled
     */
    public void setDownwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setDownwardGravity(value);
    }

    @Unique
    public float getDownwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getDownwardGravity();
    }

    @Unique
    public void setUnderwaterGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterGravity(value);
    }

    @Unique
    public float getUnderwaterGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getDownwardGravity();
    }

    @Unique
    public void setUnderwaterUpwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterUpwardGravity(value);
    }

    @Unique
    public float getUnderwaterUpwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUnderwaterUpwardGravity();
    }

    @Unique
    public void setUnderwaterDownwardGravity(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setUnderwaterDownwardGravity(value);
    }

    @Unique
    public float getUnderwaterDownwardGravity() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getUnderwaterDownwardGravity();
    }

    @Unique
    /**
     * Sets the max fall distance before an entity takes fall damage. Default is 3.
     */
    public void setMaxFallDistance(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setMaxFallDistance(value);
    }

    @Unique
    public float getMaxFallDistance() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getMaxFallDistance();
    }

    @Unique
    public void setCanMove(boolean value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setCanMove(value);
    }

    @Unique
    public boolean getCanMove() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getCanMove();
    }

    @Unique
    /**
     * Modify how high you go when you jump. Default is 1.
     * @param value
     */
    public void setJumpBoost(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setVerticalJumpPower(value);
    }

    @Unique
    public float getJumpBoost() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getVerticalJumpPower();
    }

    public void setJumpBreadth(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.setHorizontalJumpPower(value);
    }

    public float getJumpBreadth() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.getHorizontalJumpPower();
    }

    @Unique
    /**
     * Stores a string in the entity's stored data that can be reconstructed back into an item. Keys for storedItem functions are shared with storedData functions.
     */
    public void setStoredItem(String key, IItemStack item) {
        IEntity<?> npcEntity = AbstractNpcAPI.Instance().getIEntity(entity);
        npcEntity.setStoredData(key, AbstractNpcAPI.Instance().getINbt(item.getMCNbt()).toJsonString());
    }

    @Unique
    public Object getStoredItem(String key) {
        IEntity<?> npcEntity = AbstractNpcAPI.Instance().getIEntity(entity);
        String itemString = (String) npcEntity.getStoredData(key);

        AbstractNpcAPI API = AbstractNpcAPI.Instance();
        return API.createItemFromNBT(API.stringToNbt(itemString));
    }

    @Unique
    public void clearStoredItems() {
        for (String key : getStoredItemKeys()) {
            this.removeStoredData(key);
        }
    }

    @Unique
    public String[] getStoredItemKeys() {
        return this.itemKeys.toArray(new String[0]);
    }

    @Unique
    public boolean hasStoredItem(String key) {
        return itemKeys.contains(key);
    }
}
