package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.EntityAttribute;
import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.GRAVITY, value);
        }
    }

    @Unique
    public float getGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.GRAVITY);
        }
    }

    @Unique
    public void setUpwardGravity(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.UPWARD_GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.UPWARD_GRAVITY, value);
        }
    }

    @Unique
    public float getUpwardGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.UPWARD_GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.UPWARD_GRAVITY);
        }
    }

    @Unique
    public void setDownwardGravity(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.DOWNWARD_GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.DOWNWARD_GRAVITY, value);
        }
    }

    @Unique
    public float getDownwardGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.DOWNWARD_GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.DOWNWARD_GRAVITY);
        }
    }

    @Unique
    public void setUnderwaterGravity(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.UNDERWATER_GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.UNDERWATER_GRAVITY, value);
        };
    }

    @Unique
    public float getUnderwaterGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.UNDERWATER_GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.UNDERWATER_GRAVITY);
        }
    }

    @Unique
    public void setUnderwaterUpwardGravity(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.UNDERWATER_UPWARD_GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.UNDERWATER_UPWARD_GRAVITY, value);
        }
    }

    @Unique
    public float getUnderwaterUpwardGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.UNDERWATER_UPWARD_GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.UNDERWATER_UPWARD_GRAVITY);
        }
    }

    @Unique
    public void setUnderwaterDownwardGravity(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY, value);
        }
    }

    @Unique
    public float getUnderwaterDownwardGravity() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.MAX_FALL_DISTANCE);
        }
    }

    @Unique
    public void setMaxFallDistance(float value) {
        if (entity instanceof EntityPlayer) {
            ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).setCoreAttribute(EntityAttribute.MAX_FALL_DISTANCE.asSnakeCase(), value);
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            props.set(EntityAttribute.MAX_FALL_DISTANCE, value);
        }
    }

    @Unique
    public float getMaxFallDistance() {
        if (entity instanceof EntityPlayer) {
            return ExtendedScripts.getPlayerProperties(((EntityPlayer) entity)).getCoreAttribute(EntityAttribute.MAX_FALL_DISTANCE.asSnakeCase());
        } else {
            ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
            return props.get(EntityAttribute.MAX_FALL_DISTANCE);
        }
    }

    @Unique
    private void setCanMove(boolean value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.set(EntityAttribute.CAN_MOVE, value);
    }

    @Unique
    private boolean getCanMove() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.get(EntityAttribute.CAN_MOVE);
    }

    @Unique
    public void setJumpBoost(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.set(EntityAttribute.JUMP_POWER_VERTICAL, value);
    }

    @Unique
    public float getJumpBoost() {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        return props.get(EntityAttribute.JUMP_POWER_VERTICAL);
    }

    @Unique
    public void setJumpBreadth(float value) {
        ExtendedScriptEntityProperties props = ExtendedScripts.getEntityProperties(entity);
        props.set(EntityAttribute.JUMP_POWER_HORIZONTAL, value);
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
