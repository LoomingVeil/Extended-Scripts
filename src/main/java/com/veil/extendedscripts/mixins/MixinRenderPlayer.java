package com.veil.extendedscripts.mixins;

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
    public ModelBiped modelBipedMain;
    @Shadow
    public ModelBiped modelArmorChestplate;
    @Shadow
    public ModelBiped modelArmor;
    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    /**
     * @author Veil
     * @reason Could not be accomplished otherwise.
     */
    @Overwrite
    protected int shouldRenderPass(AbstractClientPlayer p_77032_1_, int slot, float p_77032_3_) {
        ItemStack itemstack = p_77032_1_.inventory.armorItemInSlot(3 - slot);

        net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel event = new net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel(p_77032_1_, ((RenderPlayer) (Object) this), 3 - slot, p_77032_3_, itemstack);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        if (event.result != -1) {
            return event.result;
        }

        if (itemstack != null) {
            Item item = itemstack.getItem();

            if (item instanceof ItemScripted) {
                IItemStack itemStack = NpcAPI.Instance().getIItemStack(itemstack);
                try {
                    this.bindTexture(((IItemCustomizable) itemStack).getArmorResource(slot));
                } catch (ReportedException e) {
                    if (e.getMessage().equals("Registering texture")) {
                        // Texture doesn't exist so lets not spam the console with errors
                        return -1;
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
                modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_77032_1_, itemstack, slot, modelbiped);
                this.setRenderPassModel(modelbiped);
                modelbiped.onGround = this.mainModel.onGround;
                modelbiped.isRiding = this.mainModel.isRiding;
                modelbiped.isChild = this.mainModel.isChild;

                int color = ((IItemCustomizable) itemStack).getArmorColor();
                if (color != -1) {
                    float red = (float)(color >> 16 & 255) / 255.0F;
                    float blue = (float)(color >> 8 & 255) / 255.0F;
                    float green = (float)(color & 255) / 255.0F;
                    GL11.glColor3f(red, blue, green);

                    if (itemstack.isItemEnchanted()) {
                        return 31;
                    }

                    return 16;
                }

                GL11.glColor3f(1.0F, 1.0F, 1.0F);

                if (itemstack.isItemEnchanted()) {
                    return 15;
                }

                return 1;
            } else if (item instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)item;
                this.bindTexture(RenderBiped.getArmorResource(p_77032_1_, itemstack, slot, null));
                ModelBiped modelbiped = slot == 2 ? this.modelArmor : this.modelArmorChestplate;
                modelbiped.bipedHead.showModel = slot == 0;
                modelbiped.bipedHeadwear.showModel = slot == 0;
                modelbiped.bipedBody.showModel = slot == 1 || slot == 2;
                modelbiped.bipedRightArm.showModel = slot == 1;
                modelbiped.bipedLeftArm.showModel = slot == 1;
                modelbiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
                modelbiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;
                modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_77032_1_, itemstack, slot, modelbiped);
                this.setRenderPassModel(modelbiped);
                modelbiped.onGround = this.mainModel.onGround;
                modelbiped.isRiding = this.mainModel.isRiding;
                modelbiped.isChild = this.mainModel.isChild;

                //Move outside if to allow for more then just CLOTH
                int j = itemarmor.getColor(itemstack);
                if (j != -1)
                {
                    float f1 = (float)(j >> 16 & 255) / 255.0F;
                    float f2 = (float)(j >> 8 & 255) / 255.0F;
                    float f3 = (float)(j & 255) / 255.0F;
                    GL11.glColor3f(f1, f2, f3);

                    if (itemstack.isItemEnchanted())
                    {
                        return 31;
                    }

                    return 16;
                }

                GL11.glColor3f(1.0F, 1.0F, 1.0F);

                if (itemstack.isItemEnchanted())
                {
                    return 15;
                }

                return 1;
            }
        }

        return -1;
    }
}
