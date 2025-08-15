package noppes.npcs.extendedapi.item;

public interface IItemCustomizable {
    /**
     * Sets the harvest level for a toolClass
     * @param toolClass vanilla tool classes include pickaxe, axe, shove, and hoe (hoe may only be in newer versions). Other mods may add their own toolClasses.
     * @param level -1: Nothing; Same as mining with a stick, 0: wood/gold tools, 1: stone tools, 2: iron tools, 3: diamond tools
     */
    void setHarvestLevel(String toolClass, int level);

    int getHarvestLevel(String toolClass);
}
