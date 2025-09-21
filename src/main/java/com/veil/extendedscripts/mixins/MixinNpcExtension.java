package com.veil.extendedscripts.mixins;

import com.mojang.authlib.GameProfile;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.extendedapi.entity.ICustomNpc;
import noppes.npcs.scripted.entity.ScriptNpc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptNpc.class})
public class MixinNpcExtension implements ICustomNpc {
    @Shadow
    public EntityNPCInterface npc;

    @Unique
    public int getAnimationType() {
        return npc.currentAnimation.ordinal();
    }

    @Unique
    public void setPlayerSkinName(String playerName) {
        npc.display.playerProfile = new GameProfile(null, playerName);
        this.npc.updateClient = true;
    }

    @Unique
    public String getPlayerSkinName() {
        if (npc.display.playerProfile == null) {
            return "";
        }
        String ret = npc.display.playerProfile.getName();
        if (ret == null) return "";
        return ret;
    }

    /**
     * @param type 0: Texture, 1: Player, 2: Url, 3 Url64
     */
    @Unique
    public void setSkinType(byte type) {
        npc.display.skinType = type;
    }

    @Unique
    public int getSkinType() {
        return npc.display.skinType;
    }
}
