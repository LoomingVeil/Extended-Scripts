package noppes.npcs.extendedapi;

import noppes.npcs.api.IBlock;
import noppes.npcs.api.IPos;
import noppes.npcs.api.item.IItemStack;

public interface IWorld {
    boolean setAllBlocks(IPos[] positions, IBlock block);

    boolean setAllBlocks(IPos[] positions, IItemStack item);

    void removeAllBlocks(IPos[] positions);

    /**
     * Spawns an item on the ground in the world
     */
    void spawnItem(IItemStack item, IPos pos);
}
