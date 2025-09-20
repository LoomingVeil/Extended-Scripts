package noppes.npcs.extendedapi.handler.data;

import noppes.npcs.api.item.IItemStack;

public interface IPlayerAttributes {
    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    void setCoreAttribute(String key, float value);

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    float getCoreAttribute(String key);

    void resetCoreAttributes();

    /**
     * Gets the attribute core as an item that can be given to the player.
     */
    IItemStack getAttributeCore();

    boolean hasCoreAttribute(String key);

    String[] getCoreAttributeKeys();

    String[] getAttributeKeys();
}
