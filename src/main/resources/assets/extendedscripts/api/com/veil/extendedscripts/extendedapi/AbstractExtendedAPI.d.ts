/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi
 */

/**
 * This object stores functions available to all scripting handlers through the "extAPI" keyword.
  * @javaFqn com.veil.extendedscripts.extendedapi.AbstractExtendedAPI
*/
export interface AbstractExtendedAPI {
    /**
     * Gets an array of the names of the registered entities in the game.
     * These names can be used in functions like {@link #createIEntity(String, IWorld)}.
     */
    getEntityNameList(): String[];
    /**
     * Creates an instance of an IEntity that can be spawned in the world with {@link IWorld#spawnEntityInWorld(noppes.npcs.api.entity.IEntity)}
     * @param entityName To see all valid names, call {@link #getEntityNameList()}
     * @return An IEntity or null if entityName is invalid
     */
    createIEntity(entityName: String, world: IWorld): IEntity;
    /**
     * Creates an instance of an ICustomProjectile that can be spawned in the world with {@link IWorld#spawnEntityInWorld(noppes.npcs.api.entity.IEntity)}
     * Once spawned in, the entity will move as if it had been fired from the shooter.
     */
    createCustomProjectile(texture: String, shooter: IEntityLivingBase): import('./entity/ICustomProjectile').ICustomProjectile;
    createCustomProjectile(shooter: IEntityLivingBase): import('./entity/ICustomProjectile').ICustomProjectile;
    /**
     * Converts a hex code to an integer color that can be used for ScriptedItem's setColor {@link noppes.npcs.api.item.IItemCustomizable#setColor(Integer)} method.
     * @param hex A length six hex code (#'s are removed automatically)
     * @return An integer color or -1 if wrong length and -2 if string is not a hex code.
     */
    hexToNpcColor(hex: String): import('./int').int;
    npcColorToHex(npcColor: import('./int').int): String;
    getAllServerPlayerNames(): String[];
    /**
     * Registers a custom attribute that can both be applied via script or /kam attribute. Attributes are registered per world.
     * @param key This, by convention, is always lowercase and words are separated by _'s. This name is used within your scripts to denote your attribute.
     * @param displayName This name will show up on your item.
     * @param colorCode Takes one of Minecraft's 16 colors 0-9 and a-f. See {@link IColorCodes}.
     * @param attributeType 0. Flat, 1. Percent, 2. Magic. See {@link IAttributeValueType}
     * @param section 0. Base, 1. Modifier, 2. Stats, 3. Info, 4. Extra. The higher the number, the further down the section is. See {@link AbstractAttributeSection}
     */
    registerAttribute(key: String, displayName: String, colorCode: import('./char').char, attributeType: import('./int').int, section: import('./int').int): import('./void').void;
    /**
     * Unregisters a custom attribute. Unregistering an attribute also removes it from all items.
     */
    unregisterAttribute(key: String): import('./boolean').boolean;
    attributeExists(key: String): import('./boolean').boolean;
    /**
     * Gets a list of all the custom attributes' keys currently registered in your world.
     */
    getAttributeKeyList(): String[];
    /**
     * Gets a list of only the custom attributes' keys that do not come natively with CustomNpcs+.
     */
    getCustomAttributeKeyList(): String[];
    getAttributeDefinition(key: String): IAttributeDefinition;
    /**
     * Creates an object which can be added to {@link com.veil.extendedscripts.extendedapi.item.IItemPotion} objects.
     * @param duration duration in ticks.
     * @param amplifier 0 indexed so 0 is level I, 1 is level II, ect.
     * @throws Exception when an invalid id is inputted.
     */
    getIPotionEffect(id: import('./int').int, duration: import('./int').int, amplifier: import('./int').int): import('./IPotionEffect').IPotionEffect;
    getIPotionEffect(id: import('./int').int): import('./IPotionEffect').IPotionEffect;
    /**
     * Gets the width of the string in pixels. This can be useful for centering {@link noppes.npcs.api.gui.ILabel}'s text.
     * Having certain non-standard special characters may produce inaccurate results.
     */
    getStringPixelWidth(text: String): import('./int').int;
}
