/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi
 */

/**
 * @javaFqn noppes.npcs.extendedapi.IWorld
 */
export interface IWorld {
    setAllBlocks(positions: IPos[], block: IBlock): import('./boolean').boolean;
    setAllBlocks(positions: IPos[], item: IItemStack): import('./boolean').boolean;
    setAllBlocks(positions: IPos[], state: BlockData): import('./void').void;
    removeAllBlocks(positions: IPos[]): import('./void').void;
    setBlock(pos: IPos, state: import('../../../com/veil/extendedscripts/extendedapi/IBlockData').IBlockData): import('./void').void;
    /**
     * Get the block as a IBlockData object. Returns null if the target block is air.
     */
    getBlockData(pos: IPos): import('../../../com/veil/extendedscripts/extendedapi/IBlockData').IBlockData;
    /**
     * Spawns an item on the ground in the world
     */
    spawnItem(item: IItemStack, pos: IPos): IEntity;
    fireProjectile(projectile: import('../../../com/veil/extendedscripts/extendedapi/entity/ICustomProjectile').ICustomProjectile): import('./void').void;
    fireProjectile(projectile: import('../../../com/veil/extendedscripts/extendedapi/entity/ICustomProjectile').ICustomProjectile, velocity: import('./float').float): import('./void').void;
    /**
     * Use in conjunction with {@link noppes.npcs.api.IWorld#setRaining(boolean)} to make it storm.
     * @param thundering
     */
    setThundering(thundering: import('./boolean').boolean): import('./void').void;
    isThundering(): import('./boolean').boolean;
}
