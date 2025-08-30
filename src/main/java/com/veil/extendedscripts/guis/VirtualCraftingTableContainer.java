package com.veil.extendedscripts.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class VirtualCraftingTableContainer extends Container {
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World worldObj;
    private final EntityPlayer player;

    public VirtualCraftingTableContainer(InventoryPlayer playerInventory, World world) {
        this.worldObj = world;
        this.player = playerInventory.player;
        this.craftMatrix = new InventoryCrafting(this, 3, 3);
        this.craftResult = new InventoryCraftResult();

        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));

        // Add the 3x3 crafting grid slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
                if (itemstack != null) {
                    if (!player.inventory.addItemStackToInventory(itemstack)) {
                        player.dropPlayerItemWithRandomChoice(itemstack, false);
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    // Your transferStackInSlot logic was mostly correct, but this version
    // is slightly cleaner and standard for a crafting table.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // If shift-clicking from the result slot
            if (slotIndex == 0) {
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            // If shift-clicking from player inventory into crafting grid
            else if (slotIndex >= 10 && slotIndex < 46) {
                if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
                    // Try merging into hotbar if it was in main inventory or vice versa
                    if (slotIndex < 37) { // From main inventory to hotbar
                        if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
                            return null;
                        }
                    } else { // From hotbar to main inventory
                        if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
                            return null;
                        }
                    }
                }
            }
            // If shift-clicking from the crafting grid into player inventory
            else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
