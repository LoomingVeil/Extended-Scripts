
/**
 * All {@link noppes.npcs.api.entity.IEntity} implement getType(). You can compare that result with this class's fields
 * to determine what kind of entity it is or use it in certain functions {@link noppes.npcs.api.entity.IEntity#getSurroundingEntities(int, int)}
 * This object is available to all scripting handlers through the "EntityType" keyword.
 */
declare namespace EntityType {
	var ENTITY: number;
	var PLAYER: number;
	var NPC: number;
	var MONSTER: number;
	var ANIMAL: number;
	var LIVING: number;
	var ITEM: number;
	var PROJECTILE: number;
	var PIXELMON: number;
	var VILLAGER: number;
}

