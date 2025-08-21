package noppes.npcs.extendedapi.entity;

import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.item.IItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IEntity {
    /**
     * Sets the gravity of the entity (not players). Ex 0.1 for 10% gravity, 10 for 1000% gravity
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setGravity(float value);

    float getGravity();

    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setUpwardGravity(float value);

    float getUpwardGravity();

    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setDownwardGravity(float value);

    float getDownwardGravity();

    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setUnderwaterGravity(float value);

    float getUnderwaterGravity();

    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setUnderwaterUpwardGravity(float value);

    float getUnderwaterUpwardGravity();

    /**
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setUnderwaterDownwardGravity(float value);

    float getUnderwaterDownwardGravity();

    /**
     * Sets the max fall distance before an entity (not players) takes fall damage. Default is 3.
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setMaxFallDistance(float value);

    float getMaxFallDistance();

    /**
     * Modify how high the entity (not players) goes when it jumps. Default is 1.
     * The effect is equivalent to having the jump boost effect of level (value - 1)
     * For players, use {@link noppes.npcs.extendedapi.entity.IPlayer#setAttribute(String, float)}
     */
    void setJumpBoost(float value);

    float getJumpBoost();

    /**
     * Stores a string in the entity's stored data that can be reconstructed back into an item. Keys for storedItem functions are shared with storedData functions.
     */
    void setStoredItem(String key, IItemStack item);

    Object getStoredItem(String key);

    void clearStoredItems();

    String[] getStoredItemKeys();

    boolean hasStoredItem(String key);
}
