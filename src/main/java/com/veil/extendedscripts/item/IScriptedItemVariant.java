package com.veil.extendedscripts.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import noppes.npcs.api.item.IItemCustomizable;

public interface IScriptedItemVariant {
    void openScriptGui(World world, EntityPlayer player, boolean ignorePlayerConditions);

    void renderOffset(IItemCustomizable scriptCustomItem);
}
