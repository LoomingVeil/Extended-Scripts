package noppes.npcs.extendedapi.handler.data;

import noppes.npcs.api.item.IItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IPlayerAttributes {
    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    void setCoreAttribute(String group, String key, float value);

    void modifyCoreAttribute(String group, String key, float delta);

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    float getCoreAttribute(String group, String key);

    float getCoreAttribute(String key);

    void removeCoreAttribute(String group, String key);

    void removeGroup(String group);

    String[] getGroups();

    void resetCoreAttributes();

    /**
     * Gets the attribute core as an item that can be given to the player.
     * Returns a combined core with all attributes from all groups.
     */
    IItemStack getWholeAttributeCore(String group);

    /**
     * Gets the attribute core as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     * Returns a combined core with all attributes from all groups.
     */
    IItemStack getWholeAttributeCore(String group, boolean canBeRedeemed);

    /**
     * Gets the attribute core for a specific group as an item that can be given to the player.
     * @param canBeRedeemed When true and right-clicking the core for 3 seconds will give you all the attributes associated with the core.
     */
    IItemStack getAttributeCore(String group, boolean canBeRedeemed);

    boolean hasCoreAttribute(String key);

    String[] getCoreAttributeKeys(String group);

    String[] getAttributeKeys();
}
