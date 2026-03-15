import { ICustomProjectile } from "./entity/ICustomProjectile";
import { IEntityLivingBase } from "./../../../../noppes/npcs/api/entity/IEntityLivingBase";
import { IPotionEffect } from "./IPotionEffect";
import { IEntity } from "./../../../../noppes/npcs/api/entity/IEntity";
import { IWorld } from "./../../../../noppes/npcs/api/IWorld";
import { IAttributeDefinition } from "./../../../../noppes/npcs/api/handler/data/IAttributeDefinition";


/**
 * This object stores functions available to all scripting handlers through the "extAPI" keyword.
 */
declare namespace extAPI {
	/**
	 * Gets an array of the names of the registered entities in the game.
	 * These names can be used in functions like {@link #createIEntity(String, IWorld)}.
	 */
	function getEntityNameList(): string[];

	/**
	 * Creates an instance of an IEntity that can be spawned in the world with {@link IWorld#spawnEntityInWorld(noppes.npcs.api.entity.IEntity)}
	 * 
	 * @param entityName To see all valid names, call {@link #getEntityNameList()}
	 * @return An IEntity or null if entityName is invalid
	 */
	function createIEntity(entityName: string, world: IWorld): IEntity;

	/**
	 * Creates an instance of an ICustomProjectile that can be spawned in the world with {@link IWorld#spawnEntityInWorld(noppes.npcs.api.entity.IEntity)}
	 * Once spawned in, the entity will move as if it had been fired from the shooter.
	 */
	function createCustomProjectile(texture: string, shooter: IEntityLivingBase): ICustomProjectile;

	function createCustomProjectile(shooter: IEntityLivingBase): ICustomProjectile;

	/**
	 * Converts a hex code to an integer color that can be used for ScriptedItem's setColor {@link noppes.npcs.api.item.IItemCustomizable#setColor(Integer)} method.
	 * 
	 * @param hex A length six hex code (#'s are removed automatically)
	 * @return An integer color or -1 if wrong length and -2 if string is not a hex code.
	 */
	function hexToNpcColor(hex: string): number;

	function npcColorToHex(npcColor: number): string;

	function getAllServerPlayerNames(): string[];

	/**
	 * Registers a custom attribute that can both be applied via script or /kam attribute. Attributes are registered per world.
	 * 
	 * @param key This, by convention, is always lowercase and words are separated by _'s. This name is used within your scripts to denote your attribute.
	 * @param displayName This name will show up on your item.
	 * @param colorCode Takes one of Minecraft's 16 colors 0-9 and a-f. See {@link IColorCodes}.
	 * @param attributeType 0. Flat, 1. Percent, 2. Magic. See {@link IAttributeValueType}
	 * @param section 0. Base, 1. Modifier, 2. Stats, 3. Info, 4. Extra. The higher the number, the further down the section is. See {@link IAttributeSection}
	 */
	function registerAttribute(key: string, displayName: string, colorCode: string, attributeType: number, section: number): void;

	/**
	 * Unregisters a custom attribute. Unregistering an attribute also removes it from all items.
	 */
	function unregisterAttribute(key: string): boolean;

	function attributeExists(key: string): boolean;

	/**
	 * Gets a list of all the custom attributes' keys currently registered in your world.
	 */
	function getAttributeKeyList(): string[];

	/**
	 * Gets a list of only the custom attributes' keys that do not come natively with CustomNpcs+.
	 */
	function getCustomAttributeKeyList(): string[];

	function getAttributeDefinition(key: string): IAttributeDefinition;

	/**
	 * Creates an object which can be added to {@link com.veil.extendedscripts.extendedapi.item.IItemPotion} objects.
	 * 
	 * @param duration duration in ticks.
	 * @param amplifier 0 indexed so 0 is level I, 1 is level II, ect.
	 * @throws Exception when an invalid id is inputted.
	 */
	function getIPotionEffect(id: number, duration: number, amplifier: number): IPotionEffect;

	function getIPotionEffect(id: number): IPotionEffect;

	/**
	 * Gets the width of the string in pixels. This can be useful for centering {@link noppes.npcs.api.gui.ILabel}'s text.
	 * Having certain non-standard special characters may produce inaccurate results.
	 */
	function getStringPixelWidth(text: string): number;

}

