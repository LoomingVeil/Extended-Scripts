package com.veil.extendedscripts.extendedapi.constants;

/**
 * All {@link IEntities} have a getType(). You can compare that result with this class's fields
 * to determine what kind of entity it is. {@link IEntity#getSurroundingEntities()}
 */
public interface IEntityType {
    int ENTITY = 0;
    int PLAYER = 1;
    int NPC = 2;
    int MONSTER = 3;
    int ANIMAL = 4;
    int LIVING = 5;
    int ITEM = 6;
    int PROJECTILE = 7;
    int PIXELMON = 8;
    int VILLAGER = 9;
}
