package com.veil.extendedscripts.mixins;

import noppes.npcs.client.renderer.items.ItemCustomRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ItemCustomRenderer.class})
public class MixinItemCustomRenderer {
    // Lnoppes/npcs/client/renderer/items/ItemCustomRenderer;renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V
    /*@Inject(
        method = "renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnoppes/npcs/items/ItemCustomizable;renderOffset(Lnoppes/npcs/api/item/IItemCustomizable;)V"
        ),
        cancellable = true,
        remap = false
    )
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object[] data, CallbackInfo ci) {
        System.out.println("Intercepting!");
        Item item = itemStack.getItem();

        if (item instanceof IScriptedItemVariant) {
            IItemStack iItemStack = NpcAPI.Instance().getIItemStack(itemStack);
            ((IScriptedItemVariant) item).renderOffset((IItemCustomizable) iItemStack);

            ci.cancel();
        }
    }*/

    /*@Redirect(
        method = "renderItem(Lnet/minecraftforge/client/IItemRenderer$ItemRenderType;Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnoppes/npcs/items/ItemCustomizable;renderOffset(Lnoppes/npcs/api/item/IItemCustomizable;)V"
        ),
        remap = false
    )
    private void safeRenderOffset(ItemCustomizable instance, IItemCustomizable custom) {
        // Safety check before calling
        if (instance != null) {
            System.out.println("Redirected renderOffset for: " + instance.getClass().getName());
            try {
                instance.renderOffset(custom); // original call if you want it
            } catch (Exception e) {
                System.err.println("Suppressed renderOffset crash: " + e);
            }
        } else {
            System.out.println("Skipped renderOffset: instance was null or wrong type.");
        }
    }*/
}
