package com.veil.extendedscripts.item;

import com.veil.extendedscripts.item.ExtendedScriptCustomItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ScriptCustomArmor extends ExtendedScriptCustomItem {

    public ScriptCustomArmor(ItemStack item) {
        super(item);
    }

    @Override
    public void setDefaults() {
        this.defaultTexture = "minecraft:textures/items/iron_chestplate.png";
        this.stackSize = 1;
    }

    public String test() {
        return "Test";
    }
}
