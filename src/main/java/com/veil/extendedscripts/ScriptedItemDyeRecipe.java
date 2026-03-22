package com.veil.extendedscripts;

import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import noppes.npcs.api.item.IItemCustom;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.scripted.NpcAPI;

import java.util.ArrayList;

public class ScriptedItemDyeRecipe implements IRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting craftingInv, World world) {
        ItemStack foundItem = null;
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < craftingInv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = craftingInv.getStackInSlot(i);

            if (itemstack1 == null) continue;

            if (itemstack1.getItem() instanceof ItemScripted) {
                IItemStack scriptedItem = NpcAPI.Instance().getIItemStack(itemstack1);
                IItemCustomizable extendedScriptedItem = (IItemCustomizable) scriptedItem;

                if (!extendedScriptedItem.isDyeable() || foundItem != null) {
                    return false;
                }

                foundItem = itemstack1;
            }
            else {
                if (itemstack1.getItem() != Items.dye) {
                    return false;
                }

                arraylist.add(itemstack1);
            }

        }

        return foundItem != null && !arraylist.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting grid) {
        ItemStack resultStack = null;
        int[] colorComponents = new int[3]; // r, g, b accumulators
        int maxBrightnessAccumulator = 0;
        int colorCount = 0;

        for (int slot = 0; slot < grid.getSizeInventory(); ++slot) {
            ItemStack slotStack = grid.getStackInSlot(slot);

            if (slotStack != null) {
                if (slotStack.getItem() instanceof ItemScripted) {
                    if (resultStack != null) return null; // two armor pieces, invalid

                    resultStack = slotStack.copy();
                    resultStack.stackSize = 1;

                    IItemCustom scriptedItem = (IItemCustom) NpcAPI.Instance().getIItemStack(resultStack);
                    IItemCustomizable extendedItem = (IItemCustomizable) scriptedItem;

                    int existingColor = scriptedItem.getColor();
                    if (existingColor != -1) {
                        float r = (float)(existingColor >> 16 & 255) / 255.0F;
                        float g = (float)(existingColor >> 8 & 255) / 255.0F;
                        float b = (float)(existingColor & 255) / 255.0F;
                        maxBrightnessAccumulator += (int)(Math.max(r, Math.max(g, b)) * 255.0F);
                        colorComponents[0] += (int)(r * 255.0F);
                        colorComponents[1] += (int)(g * 255.0F);
                        colorComponents[2] += (int)(b * 255.0F);
                        ++colorCount;
                    }
                } else if (slotStack.getItem() == Items.dye) {
                    float[] dyeColor = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(slotStack.getItemDamage())];
                    int dyeR = (int)(dyeColor[0] * 255.0F);
                    int dyeG = (int)(dyeColor[1] * 255.0F);
                    int dyeB = (int)(dyeColor[2] * 255.0F);
                    maxBrightnessAccumulator += Math.max(dyeR, Math.max(dyeG, dyeB));
                    colorComponents[0] += dyeR;
                    colorComponents[1] += dyeG;
                    colorComponents[2] += dyeB;
                    ++colorCount;
                } else {
                    return null; // invalid item in grid
                }
            }
        }

        if (resultStack == null) return null;

        // Compute the blended color
        int avgR = colorComponents[0] / colorCount;
        int avgG = colorComponents[1] / colorCount;
        int avgB = colorComponents[2] / colorCount;
        float avgMaxBrightness = (float) maxBrightnessAccumulator / (float) colorCount;
        float currentMaxBrightness = (float) Math.max(avgR, Math.max(avgG, avgB));
        avgR = (int)(avgR * avgMaxBrightness / currentMaxBrightness);
        avgG = (int)(avgG * avgMaxBrightness / currentMaxBrightness);
        avgB = (int)(avgB * avgMaxBrightness / currentMaxBrightness);

        int finalColor = (avgR << 16) | (avgG << 8) | avgB;

        IItemCustom scriptedItem = (IItemCustom) NpcAPI.Instance().getIItemStack(resultStack);
        IItemCustomizable extendedItem = (IItemCustomizable) scriptedItem;
        scriptedItem.setColor(finalColor);
        extendedItem.setArmorColor(finalColor);

        return resultStack;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return null;
    }
}
