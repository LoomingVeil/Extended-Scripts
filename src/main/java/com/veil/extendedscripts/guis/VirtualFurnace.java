package com.veil.extendedscripts.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;

public class VirtualFurnace extends TileEntityFurnace {
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    private boolean isPersistent;
    public VirtualFurnace() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    public void setPersistence(boolean value) { isPersistent = value; }

    public boolean getPersistence() { return isPersistent; }

    public void setField(int id, int value) {
        switch (id) {
            case 0: this.furnaceBurnTime = value; break;
            case 1: this.currentItemBurnTime = value; break;
            case 2: this.furnaceCookTime = value; break;
        }
    }

    public void writeToNBT(NBTTagCompound p_145841_1_) {
        p_145841_1_.setShort("BurnTime", (short)this.furnaceBurnTime);
        p_145841_1_.setShort("CookTime", (short)this.furnaceCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            if (this.furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("Items", nbttaglist);
    }

    @Override
    public void updateEntity() {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (this.furnaceBurnTime == 0 && this.canSmelt()) {
            this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]); // Fuel slot is 1
            this.furnaceBurnTime = this.currentItemBurnTime;

            if (this.furnaceBurnTime > 0) {
                flag1 = true;
                if (this.furnaceItemStacks[1] != null) {
                    --this.furnaceItemStacks[1].stackSize;

                    if (this.furnaceItemStacks[1].stackSize == 0) {
                        this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                    }
                }
            }
        }

        if (this.isBurning() && this.canSmelt()) {
            ++this.furnaceCookTime;
            if (this.furnaceCookTime == 200) { // 200 ticks = 10 seconds for smelting
                this.furnaceCookTime = 0;
                this.smeltItem();
                flag1 = true;
            }
        } else {
            this.furnaceCookTime = 0; // Reset cook time if not burning or can't smelt
        }

        if (flag != this.furnaceBurnTime > 0) {
            flag1 = true;
        }

        if (flag1) {
            this.markDirty(); // Mark dirty so container can update client.
        }
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) { // Input slot is 0
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true; // Output slot is 2
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int resultSize = this.furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return resultSize <= this.getInventoryStackLimit() && resultSize <= itemstack.getMaxStackSize();
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    @Override
    public void smeltItem() {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null)
            {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return this.furnaceItemStacks[p_70301_1_];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.furnaceItemStacks[p_70298_1_] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[p_70298_1_].stackSize <= p_70298_2_)
            {
                itemstack = this.furnaceItemStacks[p_70298_1_];
                this.furnaceItemStacks[p_70298_1_] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.furnaceItemStacks[p_70298_1_].stackSize == 0)
                {
                    this.furnaceItemStacks[p_70298_1_] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.furnaceItemStacks[p_70304_1_] != null)
        {
            ItemStack itemstack = this.furnaceItemStacks[p_70304_1_];
            this.furnaceItemStacks[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.furnaceItemStacks[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
    }
}
