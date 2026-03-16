/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.entity
 */

/**
 * @javaFqn noppes.npcs.extendedapi.entity.ICustomNpc
 */
export interface ICustomNpc {
    /**
     * See {@link com.veil.extendedscripts.constants.AnimationType}
     */
    getAnimationType(): import('./int').int;
    setPlayerSkinName(playerName: String): import('./void').void;
    getPlayerSkinName(): String;
    /**
     * @param type 0: Texture, 1: Player, 2: Url, 3 Url64
     */
    setSkinType(type: import('./byte').byte): import('./void').void;
    getSkinType(): import('./int').int;
}
