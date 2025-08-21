package com.veil.extendedscripts.extendedapi.constants;

/**
 * All {@link noppes.npcs.api.entity.IEntity} have a getType(). You can compare that result with this class's fields
 * to determine what kind of entity it is. {@link noppes.npcs.api.entity.IEntity#getSurroundingEntities(int, int)}
 * This object is available to all scripting handlers through the "EntityType" keyword.
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
