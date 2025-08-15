package noppes.npcs.extendedapi;

import noppes.npcs.api.item.IItemStack;

public interface IBlock {
    IItemStack getAsItem();

    /**
     * Simulates generating the items that would drop from a block that exists in the world without using silk touch
     */
    IItemStack[] getDrops();

    IItemStack[] getDrops(int fortune);
}
