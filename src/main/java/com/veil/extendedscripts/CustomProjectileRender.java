package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.CustomProjectileRenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CustomProjectileRender extends Render {

    public CustomProjectileRender(ResourceLocation texture) { }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityCustomProjectile projectile = (EntityCustomProjectile) entity;
        if (projectile.getRenderProperties().getRenderType() == CustomProjectileRenderType.Instance.SIMPLE) {
            CustomProjectileRenderProperties renderProperties = projectile.getRenderProperties();
            this.bindEntityTexture(entity);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
            Tessellator tessellator = Tessellator.instance;

            float scale = 0.05625F * renderProperties.getScale();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float arrowShakeTicks = projectile.arrowShake - partialTicks;

            if (arrowShakeTicks > 0.0F) {
                float f12 = -MathHelper.sin(arrowShakeTicks * 3.0F) * arrowShakeTicks;
                GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
            }

            GL11.glRotatef(renderProperties.getRollOffset(), 1.0F, 0.0F, 0.0F);
            GL11.glScalef(scale, scale, scale);
            GL11.glTranslatef(renderProperties.getForwardOffset(), 0.0F, 0.0F);
            GL11.glNormal3f(scale, 0.0F, 0.0F);

            int numPlanes = renderProperties.getNumSimpleRenderPlanes();
            for (int i = 0; i < 2 * numPlanes; i++) {
                GL11.glRotatef((180F * i) + 180F / numPlanes, 1.0F, 0.0F, 0.0F);

                // Front face
                GL11.glRotatef(renderProperties.getRotationOffset(), 0.0F, 0.0F, 1.0F);
                GL11.glNormal3f(0.0F, 0.0F, scale);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(-8.0D, -8D, 0.0D, 0.0D, 0.0D); // Top-Left UV
                tessellator.addVertexWithUV( 8.0D, -8D, 0.0D, 1.0D, 0.0D); // Top-Right UV
                tessellator.addVertexWithUV( 8.0D,  8D, 0.0D, 1.0D, 1.0D); // Bottom-Right UV
                tessellator.addVertexWithUV(-8.0D,  8D, 0.0D, 0.0D, 1.0D); // Bottom-Left UV
                tessellator.draw();
                GL11.glRotatef(-renderProperties.getRotationOffset(), 0.0F, 0.0F, 1.0F);

                // Back face (mirrored UVs so it looks correct when flipped)
                GL11.glRotatef(renderProperties.getRotationOffset(), 0.0F, 0.0F, 1.0F);
                GL11.glNormal3f(0.0F, 0.0F, scale);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(-8.0D,  8D, 0.0D, 0.0D, 1.0D); // Bottom-Left mirrored
                tessellator.addVertexWithUV( 8.0D,  8D, 0.0D, 1.0D, 1.0D); // Bottom-Right mirrored
                tessellator.addVertexWithUV( 8.0D, -8D, 0.0D, 1.0D, 0.0D); // Top-Right mirrored
                tessellator.addVertexWithUV(-8.0D, -8D, 0.0D, 0.0D, 0.0D); // Top-Left mirrored
                tessellator.draw();
                GL11.glRotatef(-renderProperties.getRotationOffset(), 0.0F, 0.0F, 1.0F);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        } else if (projectile.getRenderProperties().getRenderType() == CustomProjectileRenderType.Instance.MODEL) {
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            this.bindEntityTexture(projectile);
            // model.render(projectile, 0, 0, 0, 0, 0, 0.0625F);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((EntityCustomProjectile) entity).getRenderProperties().getTexture();
    }
}
