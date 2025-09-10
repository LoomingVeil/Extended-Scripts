package com.veil.extendedscripts.items;

import com.veil.extendedscripts.extendedapi.item.IItemFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import noppes.npcs.scripted.item.ScriptItemStack;

public class ScriptItemFood extends ScriptItemStack implements IItemFood {
    protected ItemFood food;

    public ScriptItemFood(ItemStack item) {
        super(item);
        this.food = (ItemFood) item.getItem();
    }

    @Override
    public int getType() {
        return 7;
    }

    public int getHungerRestored() {
        return food.func_150905_g(item);
    }

    public float getSaturation() {
        return food.func_150906_h(item);
    }
}
