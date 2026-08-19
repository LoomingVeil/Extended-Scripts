package com.veil.extendedscripts.mixins;

import baubles.api.BaubleType;
import baubles.common.container.SlotBauble;
import com.veil.extendedscripts.constants.ItemType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.extendedapi.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = SlotBauble.class, remap = true)
public abstract class MixinSlotBauble  {
    @Shadow
    private BaubleType type;

//    @Shadow
//    public abstract ItemStack getStack();
//
//    @Shadow
//    public abstract boolean getHasStack();

    @Inject(method = "func_75214_a", at = @At("HEAD"), cancellable = true, remap = false)
    public void onIsItemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack == null) return;
        IItemStack iItemStack = (IItemStack) NpcAPI.Instance().getIItemStack(stack);
        if (iItemStack.getType() != ItemType.Instance.SCRIPTED_ITEM) return;
        IItemCustomizable scriptedItem = (IItemCustomizable) iItemStack;
        if (scriptedItem.getBaubleType() == -1 || scriptedItem.getBaubleType() == type.ordinal()) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "func_82869_a", at = @At("HEAD"), cancellable = true, remap = false)
    public void onCanTakeStack(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        Slot slot = (Slot) (Object) this;
        if (!slot.getHasStack()) return;
        IItemStack iItemStack = (IItemStack) NpcAPI.Instance().getIItemStack(slot.getStack());
        if (iItemStack.getType() == ItemType.Instance.SCRIPTED_ITEM) {
            cir.setReturnValue(true);
        }
    }
}
