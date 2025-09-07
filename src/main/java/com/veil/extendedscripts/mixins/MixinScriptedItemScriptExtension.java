package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.item.ScriptCustomArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.item.ScriptCustomItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

// Extends the scripting for scripted items
@Mixin(value={ScriptCustomItem.class})
public class MixinScriptedItemScriptExtension {
    public ScriptCustomItem IItemStackItem = ((ScriptCustomItem)(Object)this);
    public ItemStack item = IItemStackItem.getMCItemStack();

    /**
     * Sets the harvest level for a toolClass
     * @param toolClass vanilla tool classes include pickaxe, axe, shovel, and hoe (hoe may only be in newer versions). Other mods may add their own toolClasses.
     * @param level -1: Nothing; Same as mining with a stick, 0: wood/gold tools, 1: stone tools, 2: iron tools, 3: diamond tools
     */
    @Unique
    public void setHarvestLevel(String toolClass, int level) {
        NBTTagCompound itemData = getItemDataTag((ItemStack) item);
        if (!itemData.hasKey("harvestLevels")) {
            itemData.setTag("harvestLevels", new NBTTagCompound());
        }
        NBTTagCompound harvestLevels = itemData.getCompoundTag("harvestLevels");
        if (level < 0) {
            harvestLevels.removeTag(toolClass);
        } else {
            harvestLevels.setInteger(toolClass, level);
        }
    }

    @Unique
    public int getHarvestLevel(String toolClass) {
        NBTTagCompound itemData = getItemDataTag((ItemStack) item);
        if (!itemData.hasKey("harvestLevels")) {
            itemData.setTag("harvestLevels", new NBTTagCompound());
        }
        NBTTagCompound harvestLevels = itemData.getCompoundTag("harvestLevels");
        if (!harvestLevels.hasKey(toolClass)) {
            return -1;
        } else {
            return harvestLevels.getInteger(toolClass);
        }
    }

    @Unique
    private static NBTTagCompound getItemDataTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey("ExtendedItemData")) {
            tag.setTag("ExtendedItemData", new NBTTagCompound());
        }
        return tag.getCompoundTag("ExtendedItemData");
    }

    @Unique
    public boolean switchType(int type, IPlayer player) {
        if (type == 1) {
            EntityPlayer mcPlayer = (EntityPlayer) player.getMCEntity();
            int itemIdex = -1;
            ItemStack[] allItems = mcPlayer.inventoryContainer.getInventory().toArray(new ItemStack[0]);
            for (int i = 0; i < mcPlayer.inventory.getSizeInventory(); i++) {
                if (allItems[i].equals(item)) {
                    itemIdex = i;
                    break;
                }
            }

            if (itemIdex == -1) return false;

            ItemStack newItemStack = (new ScriptCustomArmor(allItems[itemIdex])).getMCItemStack();

            // Set the new item stack in the player's current slot.
            mcPlayer.inventory.setInventorySlotContents(itemIdex, newItemStack);
            return true;
        }

        return false;
    }
}
