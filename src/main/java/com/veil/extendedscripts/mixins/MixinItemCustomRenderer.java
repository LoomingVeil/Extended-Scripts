package com.veil.extendedscripts.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.client.renderer.items.ItemCustomRenderer;
import noppes.npcs.scripted.NpcAPI;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemCustomRenderer.class, remap = false)
public abstract class MixinItemCustomRenderer {

    @Shadow
    private int item3dRenderTicks;

    @Shadow
    public abstract void renderItem3d(IItemCustomizable scriptCustomItem, EntityLivingBase entityLivingBase, ItemStack itemStack);

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void mixinRenderItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object[] data, CallbackInfo ci) {
        IItemStack iItemStack = NpcAPI.Instance().getIItemStack(itemStack);
        if (!(iItemStack instanceof IItemCustomizable)) return;

        IItemCustomizable scriptCustomItem = (IItemCustomizable)iItemStack;
        if (scriptCustomItem.isNormalItem() || type != IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) return;

        noppes.npcs.extendedapi.item.IItemCustomizable extendedScriptCustomItem = (noppes.npcs.extendedapi.item.IItemCustomizable) scriptCustomItem;
        if (!extendedScriptCustomItem.usesFirstPersonOverrides()) return;

        GL11.glPushMatrix();

        GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
        GL11.glRotatef(-315.0F, 0.0F, 0.0F, 1.0F);

        GL11.glTranslatef(extendedScriptCustomItem.getFirstPersonTranslateX(), extendedScriptCustomItem.getFirstPersonTranslateY(), extendedScriptCustomItem.getFirstPersonTranslateZ());
        GL11.glRotatef(extendedScriptCustomItem.getFirstPersonRotationX(), 1, 0, 0);
        GL11.glRotatef(extendedScriptCustomItem.getFirstPersonRotationY(), 0, 1, 0);
        GL11.glRotatef(extendedScriptCustomItem.getFirstPersonRotationZ(), 0, 0, 1);

        GL11.glRotatef(scriptCustomItem.getRotationXRate() * item3dRenderTicks % 360, 1, 0, 0);
        GL11.glRotatef(scriptCustomItem.getRotationYRate() * item3dRenderTicks % 360, 0, 1, 0);
        GL11.glRotatef(scriptCustomItem.getRotationZRate() * item3dRenderTicks % 360, 0, 0, 1);

        GL11.glScalef(extendedScriptCustomItem.getFirstPersonScaleX(), extendedScriptCustomItem.getFirstPersonScaleY(), extendedScriptCustomItem.getFirstPersonScaleZ());

        int color = scriptCustomItem.getColor();
        float itemRed = (color >> 16 & 255) / 255f;
        float itemGreen = (color >> 8 & 255) / 255f;
        float itemBlue = (color & 255) / 255f;
        GL11.glColor4f(itemRed, itemGreen, itemBlue, 1.0F);

        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.09375F, 0.0625F, 0.0F);

        EntityLivingBase entityLivingBase = (EntityLivingBase) data[1];
        renderItem3d(scriptCustomItem, entityLivingBase, itemStack);

        GL11.glPopMatrix();

        ci.cancel();
    }
}
