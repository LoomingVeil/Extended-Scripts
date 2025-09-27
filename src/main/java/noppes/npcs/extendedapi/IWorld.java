package noppes.npcs.extendedapi;

import com.veil.extendedscripts.BlockData;
import com.veil.extendedscripts.extendedapi.IBlockData;
import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.IPos;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IWorld {
    boolean setAllBlocks(IPos[] positions, IBlock block);

    boolean setAllBlocks(IPos[] positions, IItemStack item);

    void setAllBlocks(IPos[] positions, BlockData state);

    void removeAllBlocks(IPos[] positions);

    void setBlock(IPos pos, IBlockData state);

    /**
     * Get the block as a IBlockData object. Returns null if the target block is air.
     */
    IBlockData getBlockData(IPos pos);

    /**
     * Spawns an item on the ground in the world
     */
    IEntity spawnItem(IItemStack item, IPos pos);

    void fireProjectile(ICustomProjectile projectile);

    void fireProjectile(ICustomProjectile projectile, float velocity);

    /**
     * Use in conjunction with {@link noppes.npcs.api.IWorld#setRaining(boolean)} to make it storm.
     * @param thundering
     */
    void setThundering(boolean thundering);

    boolean isThundering();
}
