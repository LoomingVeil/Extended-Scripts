/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.constants
 */

/**
 * All {@link noppes.npcs.api.entity.IEntity} implement getType(). You can compare that result with this class's fields
 * to determine what kind of entity it is or use it in certain functions {@link noppes.npcs.api.entity.IEntity#getSurroundingEntities(int, int)}
 * This object is available to all scripting handlers through the "EntityType" keyword.
  * @javaFqn com.veil.extendedscripts.extendedapi.constants.AbstractEntityType
*/
export interface AbstractEntityType {
	/**
	 * Value is 0
	 */
	ENTITY: number;
	/**
	 * Value is 1
	 */
	PLAYER: number;
	/**
	 * Value is 2
	 */
	NPC: number;
	/**
	 * Value is 3
	 */
	MONSTER: number;
	/**
	 * Value is 4
	 */
	ANIMAL: number;
	/**
	 * Value is 5
	 */
	LIVING: number;
	/**
	 * Value is 6
	 */
	ITEM: number;
	/**
	 * Value is 7
	 */
	PROJECTILE: number;
	/**
	 * Value is 8
	 */
	PIXELMON: number;
	/**
	 * Value is 9
	 */
	VILLAGER: number;
}
