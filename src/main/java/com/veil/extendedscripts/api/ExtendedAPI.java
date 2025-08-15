package com.veil.extendedscripts.api;

import com.veil.extendedscripts.api.constants.*;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.IEntity;

/**
 * This object stores functions available to all scripting handlers through the "extAPI" keyword.
 */
public interface ExtendedAPI {
    public IShapeMaker getShapeMaker();

    public IKeys getKeysCodes();

    public IAnimationType getAnimationTypes();

    public IEntityType getEntityTypes();

    public IJobType getJobTypes();

    public IRoleType getRoleTypes();

    public IAttributeValueType getAttributeValueTypes();

    public IAttributeSection getArributeSections();

    public IColorCodes getColorCodes();

    /**
     * Gets an array of the names of the registered entities in the game.
     * These names can be used in functions like {@link #createIEntity(String, IWorld)}.
     */
    public String[] getEntityNameList();

    /**
     * Creates an instance of an IEntity that can be spawned in the world with IWorld.spawnEntityInWorld(entity)
     * @param entityName To see all valid names, call {@link #getEntityNameList()}
     * @return An IEntity or null if entityName is invalid
     */
    public IEntity createIEntity(String entityName, IWorld world);

    /**
     * Converts a hex code to an integer color that can be used for ScriptedItem's setColor {@link noppes.npcs.api.item.IItemCustomizable#setColor(Integer)} method.
     * @param hex A length six hex code (#'s are removed automatically)
     * @return An integer color or -1 if wrong length and -2 if string is not a hex code.
     */
    public int hexToNpcColor(String hex);

    public String npcColorToHex(int npcColor);

    /**
     * Registers a custom attribute that can both be applied via script or /kam attribute. Attributes are registered per world.
     * @param key This, by convention, is always lowercase and words are separated by _'s. This name is used within your scripts to denote your attribute.
     * @param displayName This name will show up on your item.
     * @param colorCode Takes one of Minecraft's 16 colors 0-9 and a-f. See {@link IColorCodes}.
     * @param attributeType 0. Flat, 1. Percent, 2. Magic. {@link IAttributeValueType}
     * @param section 0. Base, 1. Modifier, 2. Stats, 3. Info, 4. Extra. The higher the number, the further down the section is. See {@link IAttributeSection}
     */
    public void registerAttribute(String key, String displayName, char colorCode, int attributeType, int section);

    /**
     * Unregisters a custom attribute. Unregistering an attribute also removes it from all items.
     */
    public boolean unregisterAttribute(String key);

    /**
     * Gets a list of all the custom attributes' keys currently registered in your world.
     */
    public String[] getAttributeKeyList();

    /**
     * Gets a list of only the custom attributes' keys that do not come natively with CustomNpcs+.
     */
    public String[] getCustomAttributeKeyList();
}
