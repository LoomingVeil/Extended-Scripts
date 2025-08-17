package com.veil.extendedscripts.mixins;

import net.minecraft.block.Block;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IBlock;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.item.ScriptItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ScriptItemStack.class})
public class MixinItemExtension implements noppes.npcs.extendedapi.item.IItemStack {
    @Shadow
    public ItemStack item;

    @Unique
    public void setUnbreakable(boolean value) {
        IItemStack npcItem = AbstractNpcAPI.Instance().getIItemStack(item);
        npcItem.getNbt().setBoolean("Unbreakable", value);
    }

    @Unique
    public int getNumericalId() {
        return item.getItem().getIdFromItem(item.getItem());
    }
}
