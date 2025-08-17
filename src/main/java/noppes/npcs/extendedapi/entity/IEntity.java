package noppes.npcs.extendedapi.entity;

import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.item.IItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IEntity {
    /**
     * Sets the gravity of the entity. Ex 0.1 for 10% gravity, 10 for 1000% gravity
     */
    void setGravity(float value);

    float getGravity();

    void setUpwardGravity(float value);

    float getUpwardGravity();

    void setDownwardGravity(float value);

    float getDownwardGravity();

    void setUnderwaterGravity(float value);

    float getUnderwaterGravity();

    void setUnderwaterUpwardGravity(float value);

    float getUnderwaterUpwardGravity();

    void setUnderwaterDownwardGravity(float value);

    float getUnderwaterDownwardGravity();

    /**
     * Sets the max fall distance before an entity takes fall damage. Default is 3.
     */
    void setMaxFallDistance(float value);

    float getMaxFallDistance();

    /**
     * Modify how high the entity goes when it jumps. Default is 1.
     * The effect is equivalent to having the jump boost effect of level (value - 1)
     */
    void setJumpBoost(float value);

    float getJumpBoost();

    /**
     * Modify how far the entity goes when it jumps. Default is 1.
     */
    void setJumpBreadth(float value);

    float getJumpBreadth();

    /**
     * Stores a string in the entity's stored data that can be reconstructed back into an item. Keys for storedItem functions are shared with storedData functions.
     */
    void setStoredItem(String key, IItemStack item);

    Object getStoredItem(String key);

    void clearStoredItems();

    String[] getStoredItemKeys();

    boolean hasStoredItem(String key);
}
