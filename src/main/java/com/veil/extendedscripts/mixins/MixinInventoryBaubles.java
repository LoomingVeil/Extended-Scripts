package com.veil.extendedscripts.mixins;

import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import com.veil.extendedscripts.constants.ItemType;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.extendedapi.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = InventoryBaubles.class, remap = true)
public class MixinInventoryBaubles {

    @Shadow
    public ItemStack[] stackList;
    @Shadow
    public Container eventHandler;
    @Shadow
    public void syncSlotToClients(int slot) {}

    @Inject(method = "func_70299_a", at = @At(value = "TAIL"), remap = false)
    public void onSetInventorySlotContents(int slot, ItemStack stack, CallbackInfo ci) {
        if (stack == null) return;
        IItemStack iItemStack = (IItemStack) NpcAPI.Instance().getIItemStack(stack);
        if (iItemStack.getType() != ItemType.Instance.SCRIPTED_ITEM) return;

        EntityPlayer player = ((InventoryBaubles)(Object)this).player.get();
        if (player != null) {
            PlayerAttributeTracker tracker = new PlayerAttributeTracker(player.getUniqueID());
            tracker.recalcAttributes(player);
        }
    }

    @Inject(method = "func_70298_a", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void onDecrStackSize(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (this.stackList[slot] == null) return;

        IItemStack iItemStack = (IItemStack) NpcAPI.Instance().getIItemStack(this.stackList[slot]);
        if (iItemStack.getType() != ItemType.Instance.SCRIPTED_ITEM) return;

        ItemStack itemstack;
        if (this.stackList[slot].stackSize <= amount) {
            itemstack = this.stackList[slot];
            this.stackList[slot] = null;
            if (eventHandler != null) {
                eventHandler.onCraftMatrixChanged((IInventory) this);
            }
            syncSlotToClients(slot);
        } else {
            itemstack = this.stackList[slot].splitStack(amount);
            if (this.stackList[slot].stackSize == 0) {
                this.stackList[slot] = null;
            }
            if (eventHandler != null) {
                eventHandler.onCraftMatrixChanged((IInventory) this);
            }
            syncSlotToClients(slot);
        }

        EntityPlayer player = ((InventoryBaubles)(Object)this).player.get();
        if (player != null) {
            PlayerAttributeTracker tracker = new PlayerAttributeTracker(player.getUniqueID());
            tracker.recalcAttributes(player);
        }

        cir.setReturnValue(itemstack);
        cir.cancel();
    }
}
