package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.item.ArmorScripted;
import com.veil.extendedscripts.item.ExtendedScriptCustomItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value={ContainerPlayer.class})
public abstract class MixinContainerPlayer extends Container {
    @Inject(
        method = "transferStackInSlot",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onTransferStackInSlot(EntityPlayer player, int index, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack() && index >= 9) {
            ItemStack itemstack = slot.getStack();
            Item item = itemstack.getItem();

            if (!(item instanceof ArmorScripted)) {
                return;
            }

            ItemStack original = itemstack.copy();

            ExtendedScriptCustomItem scriptedItem = new ExtendedScriptCustomItem(itemstack);
            int armorType = scriptedItem.getArmorType();

            if (armorType == -2) return;

            if (armorType == -1) {
                for (int i = 0; i < 4; i++) {
                    Slot armorSlot = this.inventorySlots.get(5 + i);
                    if (!armorSlot.getHasStack()) {
                        if (this.mergeItemStack(itemstack, 5 + i, 5 + i + 1, false)) {
                            break;
                        }
                    }
                }
            } else {
                Slot armorSlot = this.inventorySlots.get(5 + armorType);
                if (!armorSlot.getHasStack()) {
                    if (!this.mergeItemStack(itemstack, 5 + armorType, 5 + armorType + 1, false)) {
                        cir.setReturnValue(null);
                        return;
                    }
                }
            }

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.stackSize == original.stackSize) {
                cir.setReturnValue(null);
                return;
            }

            slot.onPickupFromSlot(player, itemstack);
            cir.setReturnValue(original);
        }
    }
}
