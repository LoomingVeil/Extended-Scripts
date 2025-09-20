package com.veil.extendedscripts.mixins;

import net.minecraft.block.Block;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
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

    /**
     * Returns the texture of the item. This method is good for items and blocks with the same texture on all faces.
     * However, if you want all the textures from a multi textured block, see
     */
    @Unique
    public String getItemTexture() {
        if (item == null || item.getItem() == null) {
            return null;
        }

        IIcon icon = item.getItem().getIconFromDamage(item.getItemDamage());
        if (icon == null) {
            return null;
        }

        String iconName = icon.getIconName();
        if (!iconName.contains(":")) {
            iconName = "minecraft:" + iconName;
        }

        String[] split = iconName.split(":", 2);
        String domain = split[0];
        String texturePath = split[1];

        return String.format("%s:textures/items/%s.png", domain, texturePath);
    }

    /**
     * Returns the textures that make up a block
     */
    @Unique
    public String getBlockTexture(int side) {
        if (!isBlock()) return null;

        Block block = Block.getBlockFromItem(item.getItem());
        String iconName = block.getBlockTextureFromSide(side).getIconName();
        if (!iconName.contains(":")) {
            iconName = "minecraft:" + iconName;
        }

        String[] split = iconName.split(":", 2);
        String domain = split[0];
        String texturePath = split[1];

        return String.format("%s:textures/items/%s.png", domain, texturePath);
    }

    @Unique
    public boolean isBlock() {
        return item.getItem() instanceof ItemBlock;
    }

    @Unique
    public int getType() { // This function is never used because it already exists in ScriptItemStack
        return 0;
    }
}
