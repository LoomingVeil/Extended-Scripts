package noppes.npcs.extendedapi.entity;

import org.spongepowered.asm.mixin.Unique;

public interface ICustomNpc {
    /**
     * See {@link com.veil.extendedscripts.constants.AnimationType}
     */
    int getAnimationType();

    void setPlayerSkinName(String playerName);

    String getPlayerSkinName();

    /**
     * @param type 0: Texture, 1: Player, 2: Url, 3 Url64
     */
    void setSkinType(byte type);

    int getSkinType();
}
