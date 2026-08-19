package com.veil.extendedscripts.mixins;

import baubles.common.container.ContainerPlayerExpanded;
import com.veil.extendedscripts.constants.ItemType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.extendedapi.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = ContainerPlayerExpanded.class, remap = true)
public class MixinContainerBaubles extends Container {

    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true, remap = false)
    public void onTransferStackInSlot(EntityPlayer player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {

        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot == null || !slot.getHasStack()) {
            return;
        }

        // Only intercept items coming from the player's inventory.
        if (slotIndex < 13 || slotIndex >= 49) {
            return;
        }

        ItemStack itemstack1 = slot.getStack();
        ItemStack stack = itemstack1.copy();

        IItemStack iItemStack = (IItemStack) NpcAPI.Instance().getIItemStack(stack);

        if (iItemStack.getType() != ItemType.Instance.SCRIPTED_ITEM) {
            return;
        }

        IItemCustomizable scriptedItem = (IItemCustomizable) iItemStack;

        int baubleType = scriptedItem.getBaubleType();

        // -2 means this isn't a bauble.
        if (baubleType == -2) {
            return;
        }

        boolean moved = false;

        if (baubleType == -1) {
            // Can go into any bauble slot.
            moved = tryMoveToBaubleSlot(stack, 9)
                || tryMoveToBaubleSlot(stack, 10)
                || tryMoveToBaubleSlot(stack, 11)
                || tryMoveToBaubleSlot(stack, 12);

        } else if (baubleType == 0) {
            // Amulet
            moved = tryMoveToBaubleSlot(stack, 10)
                || tryMoveToBaubleSlot(stack, 11);

        } else if (baubleType == 1) {
            // Ring
            moved = tryMoveToBaubleSlot(stack, 9);

        } else if (baubleType == 2) {
            // Belt
            moved = tryMoveToBaubleSlot(stack, 12);
        }

        if (moved) {
            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            cir.setReturnValue(stack.copy());
            return;
        }
    }

    private boolean tryMoveToBaubleSlot(ItemStack stack, int slotIndex) {
        return this.mergeItemStack(stack, slotIndex, slotIndex + 1, false);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
