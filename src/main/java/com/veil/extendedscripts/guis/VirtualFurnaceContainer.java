package com.veil.extendedscripts.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class VirtualFurnaceContainer extends ContainerFurnace {
    private VirtualFurnace virtualFurnace;

    // Constructor: Takes the player's inventory and our VirtualTileEntityFurnace
    public VirtualFurnaceContainer(InventoryPlayer inventoryPlayer, TileEntityFurnace furnace) {
        // Call the vanilla ContainerFurnace constructor.
        // It sets up the slots using the provided TileEntityFurnace.
        super(inventoryPlayer, furnace);

        // Cast and store our specific virtual furnace.
        // We ensure 'furnace' is a VirtualTileEntityFurnace from ModGuiHandler.
        if (furnace instanceof VirtualFurnace) {
            this.virtualFurnace = (VirtualFurnace) furnace;
        } else {
            // This case indicates a setup error in ModGuiHandler.
            System.err.println("Error: ContainerVirtualFurnace received non-VirtualTileEntityFurnace!");
            this.virtualFurnace = null; // Or throw an IllegalArgumentException
        }
    }

    public void setPersistence(boolean value) {
        virtualFurnace.setPersistence(value);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        // Put any remaining items from the furnace back into the player's inventory
        if (!virtualFurnace.getPersistence()) {
            for (int i = 0; i < virtualFurnace.getSizeInventory(); ++i) {
                ItemStack itemstack = this.virtualFurnace.getStackInSlot(i);
                if (itemstack != null) {
                    boolean result = player.inventory.addItemStackToInventory(itemstack);
                    if(!result) player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }

        EntityPlayerMP playerMP = (EntityPlayerMP)player;
        playerMP.sendContainerToPlayer(playerMP.inventoryContainer);
    }

    /**
     * Called every tick on the server to detect changes in the container's inventory
     * and send updates to the client. This is where we manually tick our virtual furnace.
     */
    @Override
    public void detectAndSendChanges() {
        // First, let the vanilla ContainerFurnace handle its usual inventory syncing.
        // This sends item updates, and calls getField/setField for progress bars.
        super.detectAndSendChanges();

        // Now, manually tick our virtual furnace.
        // This will execute the smelting logic defined in VirtualTileEntityFurnace's updateEntity().
        if (this.virtualFurnace != null) {
            this.virtualFurnace.updateEntity(); // Call its update method!
        }
    }
}
