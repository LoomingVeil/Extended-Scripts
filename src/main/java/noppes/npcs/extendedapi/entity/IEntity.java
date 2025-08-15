package noppes.npcs.extendedapi.entity;

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
     * Modify how high you go when you jump. Default is 1.
     * This function is not linearly scaled. Hopefully it will be in the future.
     * @param value
     */
    void setJumpBoost(float value);

    float getJumpBoost();
}
