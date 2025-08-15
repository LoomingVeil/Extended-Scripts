package com.veil.extendedscripts.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class VirtualCraftingTableMatrix extends InventoryCrafting {
    private ItemStack[] stackList;
    private Container eventHandler; // The container that this inventory belongs to

    public VirtualCraftingTableMatrix(Container container) {
        // Vanilla InventoryCrafting takes a container and width/height.
        // For a 3x3, it's 3, 3, making 9 slots.
        super(container, 3, 3);
        this.eventHandler = container;
        this.stackList = new ItemStack[9]; // 3x3 crafting grid
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= this.getSizeInventory() ? null : this.stackList[slot];
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 3 && column >= 0 && column < 3) {
            return this.getStackInSlot(column * 3 + row);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.stackList[slot] != null) {
            ItemStack itemstack;
            // Notify container of change
            if (this.stackList[slot].stackSize <= amount) {
                itemstack = this.stackList[slot];
                this.stackList[slot] = null;
            } else {
                itemstack = this.stackList[slot].splitStack(amount);
                if (this.stackList[slot].stackSize == 0) {
                    this.stackList[slot] = null;
                }
            }
            this.eventHandler.onCraftMatrixChanged(this); // Notify container of change
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.stackList[slot] != null) {
            ItemStack itemstack = this.stackList[slot];
            this.stackList[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.stackList[slot] = stack;
        this.eventHandler.onCraftMatrixChanged(this); // Notify container of change
    }

    @Override
    public String getInventoryName() {
        return "container.crafting"; // Or "container.yourmodid.crafting"
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        // No need for file saving for this in-memory inventory
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true; // Always usable by the player who has this GUI open
    }

    @Override
    public void openInventory() {} // No specific action needed
    @Override
    public void closeInventory() {} // No specific action needed

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true; // All items are valid for crafting slots
    }
}
