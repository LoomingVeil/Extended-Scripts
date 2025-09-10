package com.veil.extendedscripts.items;

import com.veil.extendedscripts.extendedapi.item.IItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import noppes.npcs.scripted.item.ScriptItemStack;

public class ScriptItemTool extends ScriptItemStack implements IItemTool {
    protected ItemTool tool;

    public ScriptItemTool(ItemStack item) {
        super(item);
        this.tool = (ItemTool) item.getItem();
    }

    @Override
    public int getType() {
        return 9;
    }

    public String getToolMaterial() {
        return tool.getToolMaterialName();
    }
}
