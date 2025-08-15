package com.veil.extendedscripts.api.constants;

/**
 * All {@link IEntities} have a getType(). You can compare that result with this class's fields
 * to determine what kind of entity it is. {@link IEntity#getSurroundingEntities()}
 */
public interface IEntityType {
    public final int ENTITY = 0;
    public final int PLAYER = 1;
    public final int NPC = 2;
    public final int MONSTER = 3;
    public final int ANIMAL = 4;
    public final int LIVING = 5;
    public final int ITEM = 6;
    public final int PROJECTILE = 7;
    public final int PIXELMON = 8;
    public final int VILLAGER = 9;
}
