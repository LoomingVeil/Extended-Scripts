package com.veil.extendedscripts.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class VirtualCraftingTableContainer extends Container {
    public VirtualCraftingTableMatrix craftMatrix;
    public IInventory craftResult;
    private World worldObj;

    public VirtualCraftingTableContainer(InventoryPlayer playerInventory, World world) {
        this.worldObj = world;
        this.craftMatrix = new VirtualCraftingTableMatrix(this); // Your 3x3 matrix
        this.craftResult = new InventoryCraftResult(); // The single result slot

        // Add the crafting result slot (index 0 for result)
        this.addSlotToContainer(new Slot(this.craftResult, 0, 124, 35));

        // Add the 3x3 crafting grid slots (indices 1-9 for inputs)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        // Add player inventory slots (indices 10-36 for main inventory)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar slots (indices 37-45 for hotbar)
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix); // Initial update
    }

    // Called when the crafting matrix changes (item added/removed/changed)
    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        // Update the crafting result based on current matrix contents
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }

    // This is called when the player closes the GUI
    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        // Put any remaining items from the crafting grid back into the player's inventory
        if (!this.worldObj.isRemote) { // Only do this on the server side
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
                if (itemstack != null) {
                    boolean result = player.inventory.addItemStackToInventory(itemstack);
                    if(!result) player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }

        EntityPlayerMP playerMP = (EntityPlayerMP)player;
        playerMP.sendContainerToPlayer(playerMP.inventoryContainer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // From result slot (slot 0)
            if (slotIndex == 0) {
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) { // Merge into player inventory (excluding hotbar)
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            // From crafting grid slots (slots 1-9)
            else if (slotIndex >= 1 && slotIndex < 10) {
                if (!this.mergeItemStack(itemstack1, 10, 46, false)) { // Merge into player inventory (excluding hotbar)
                    return null;
                }
            }
            // From player inventory/hotbar (slots 10-45)
            else if (slotIndex >= 10 && slotIndex < 46) {
                // Try to move to crafting grid first
                if (!this.mergeItemStack(itemstack1, 1, 10, false)) { // Merge into crafting grid slots
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
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
