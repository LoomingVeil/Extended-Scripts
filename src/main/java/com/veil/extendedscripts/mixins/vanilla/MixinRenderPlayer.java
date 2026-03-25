package com.veil.extendedscripts.mixins.vanilla;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.item.IItemCustom;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.scripted.NpcAPI;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {
    @Shadow
    public ModelBiped modelArmorChestplate;
    @Shadow
    public ModelBiped modelArmor;
    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "shouldRenderPass", at = @At("HEAD"), cancellable = true)
    private void onShouldRenderPass(AbstractClientPlayer player, int slot, float partialTicks, CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = player.inventory.armorItemInSlot(3 - slot);
        if (itemstack == null || !(itemstack.getItem() instanceof ItemScripted)) return;

        IItemStack itemStack = NpcAPI.Instance().getIItemStack(itemstack);
        IItemCustomizable custom = (IItemCustomizable) itemStack;

        try {
            this.bindTexture(custom.getArmorResource(slot));
        } catch (ReportedException e) {
            if (e.getMessage().equals("Registering texture")) {
                cir.setReturnValue(-1);
                return;
            }
        }

        ModelBiped modelbiped = slot == 2 ? this.modelArmor : this.modelArmorChestplate;
        modelbiped.bipedHead.showModel = slot == 0;
        modelbiped.bipedHeadwear.showModel = slot == 0;
        modelbiped.bipedBody.showModel = slot == 1 || slot == 2;
        modelbiped.bipedRightArm.showModel = slot == 1;
        modelbiped.bipedLeftArm.showModel = slot == 1;
        modelbiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
        modelbiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;
        modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(player, itemstack, slot, modelbiped);
        this.setRenderPassModel(modelbiped);
        modelbiped.onGround = this.mainModel.onGround;
        modelbiped.isRiding = this.mainModel.isRiding;
        modelbiped.isChild = this.mainModel.isChild;

        int color = custom.getArmorColor();
        if (color != -1) {
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            GL11.glColor3f(r, g, b);

            // 17 if overlay texture exists, 16 otherwise
            if (custom.getArmorTexture2() != null) {
                cir.setReturnValue(itemstack.isItemEnchanted() ? 32 : 17);
            } else {
                cir.setReturnValue(itemstack.isItemEnchanted() ? 31 : 16);
            }
            return;
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        if (custom.getArmorTexture2() != null) {
            cir.setReturnValue(itemstack.isItemEnchanted() ? 16 : 2);
        } else {
            cir.setReturnValue(itemstack.isItemEnchanted() ? 15 : 1);
        }
    }

    @Inject(method = "func_82408_c",
        at = @At("HEAD"),
        cancellable = true)
    private void onRenderOverlayPass(AbstractClientPlayer player, int slot, float partialTicks, CallbackInfo ci) {
        ItemStack itemstack = player.inventory.armorItemInSlot(3 - slot);
        if (itemstack == null || !(itemstack.getItem() instanceof ItemScripted)) return;

        IItemStack itemStack = NpcAPI.Instance().getIItemStack(itemstack);
        IItemCustomizable custom = (IItemCustomizable) itemStack;

        ResourceLocation overlayTexture = custom.getArmorOverlayResource(slot);
        if (overlayTexture == null || overlayTexture.getResourcePath().isEmpty()) {
            ci.cancel(); // no overlay, suppress the vanilla attempt entirely
            return;
        }

        try {
            this.bindTexture(overlayTexture);
        } catch (ReportedException e) {
            if (e.getMessage().equals("Registering texture")) {
                ci.cancel();
                return;
            }
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        ci.cancel();
    }
}
