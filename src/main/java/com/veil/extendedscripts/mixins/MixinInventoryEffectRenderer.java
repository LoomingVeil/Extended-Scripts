package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.Config;
import com.veil.extendedscripts.CustomEffectBridge;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.commands.IInventoryEffectRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ClientCacheHandler;
import noppes.npcs.client.renderer.ImageData;
import noppes.npcs.controllers.data.CustomEffect;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(value={InventoryEffectRenderer.class})
public abstract class MixinInventoryEffectRenderer extends GuiContainer implements IInventoryEffectRenderer {
    private static int pixelsBetweenEffects = 33;
    private static int effectPage = 1;
    private int maxPages = 1;
    private GuiButton prevButton;
    private GuiButton nextButton;
    private boolean showPages = true;

    @Shadow
    private boolean field_147045_u;

    public MixinInventoryEffectRenderer(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    /**
     * @author Veil
     * @reason This could not be done
     */
    @Overwrite
    public void initGui() {
        super.initGui();

        showPages = Config.enableEffectPages;

        if (!ExtendedScripts.getEffectsList(this.mc.thePlayer).isEmpty()) {
            this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            this.field_147045_u = true;
        }
    }


    /**
     * @author Veil
     * @reason This could not be done with other types of Mixins
     */
    @Overwrite
    private void func_147044_g() {
        int startLeft = this.guiLeft - 124;
        int startTop = this.guiTop;
        boolean flag = true;
        List<CustomEffectBridge> activeEffects = ExtendedScripts.getEffectsList(this.mc.thePlayer);

        if (!activeEffects.isEmpty()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            maxPages = (int) Math.ceil(activeEffects.size() / 5.0F);
            effectPage = Math.min(maxPages, effectPage); // maxPages may change if enough effects timeout.

            if (activeEffects.size() > 5) {
                if (showPages) {
                    boolean hasPrev = false;
                    boolean hasNext = false;
                    for (GuiButton button : buttonList) {
                        if (button.id == 201) {
                            hasPrev = true;
                        } else if (button.id == 202) {
                            hasNext = true;
                        }
                    }

                    if (!hasPrev) {
                        prevButton = new GuiButton(201, 0, 0, 20, 20, "<");
                        prevButton.xPosition = startLeft;
                        prevButton.yPosition = startTop - 20;
                        buttonList.add(prevButton);
                    }

                    if (!hasNext) {
                        nextButton = new GuiButton(202, 0, 0, 20, 20, ">");
                        nextButton.xPosition = startLeft + 100;
                        nextButton.yPosition = startTop - 20;
                        buttonList.add(nextButton);
                    }

                    String page = String.format("%d / %d", effectPage, maxPages);
                    // fontRendererObj.drawString(page, startLeft + (xSize / 2) - (width / 2), startTop - 20, -1);
                    fontRendererObj.drawString(page, startLeft + 50, startTop - 10, -1);
                } else {
                    pixelsBetweenEffects = 133 / (activeEffects.size() - 1);
                }
            } else {
                List<GuiButton> toRemove = new ArrayList<>();
                for (GuiButton button : buttonList) {
                    if (button.id == 201) {
                        toRemove.add(button);
                    } else if (button.id == 202) {
                        toRemove.add(button);
                    }
                }

                buttonList.removeAll(toRemove);
            }

            if (showPages) {
                for (int i = (effectPage - 1) * 5; i < effectPage * 5; i++) {
                    if (i >= activeEffects.size()) {
                        break;
                    }
                    CustomEffectBridge potionEffect = activeEffects.get(i);
                    displayEffect(potionEffect, startLeft, startTop);
                    startTop += pixelsBetweenEffects;
                }
            } else {
                for (Iterator iterator = activeEffects.iterator(); iterator.hasNext(); startTop += pixelsBetweenEffects) {
                    displayEffect((CustomEffectBridge) iterator.next(), startLeft, startTop);
                }
            }
        } else {
            List<GuiButton> toRemove = new ArrayList<>();
            for (GuiButton button : buttonList) {
                if (button.id == 201) {
                    toRemove.add(button);
                } else if (button.id == 202) {
                    toRemove.add(button);
                }
            }

            buttonList.removeAll(toRemove);
        }
    }

    public void displayEffect(CustomEffectBridge potionEffect, int startLeft, int startTop) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147001_a);
        this.drawTexturedModalRect(startLeft, startTop, 0, 166, 140, 32); // Draw background

        if (!potionEffect.isCustom()) {
            Potion potion = Potion.potionTypes[potionEffect.getPotionID()];

            if (potion.hasStatusIcon()) {
                int iconIndex = potion.getStatusIconIndex();
                this.drawTexturedModalRect(startLeft + 6, startTop + 7, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
            }

            potion.renderInventoryEffect(startLeft, startTop, potionEffect, mc);
            if (!potion.shouldRenderInvText(potionEffect)) return;
        } else {
            drawCustomIcon(potionEffect.getCustomEffect(), startLeft + 6, startTop + 7, 16);
        }

        String label = I18n.format(potionEffect.getEffectName());

        if (potionEffect.getAmplifier() == 1) {
            label = label + " " + I18n.format("enchantment.level.2");
        } else if (potionEffect.getAmplifier() == 2) {
            label = label + " " + I18n.format("enchantment.level.3");
        } else if (potionEffect.getAmplifier() == 3) {
            label = label + " " + I18n.format("enchantment.level.4");
        }

        this.fontRendererObj.drawStringWithShadow(label, startLeft + 10 + 18, startTop + 6, 16777215);
        String duration = Potion.getDurationString(potionEffect);
        this.fontRendererObj.drawStringWithShadow(duration, startLeft + 10 + 18, startTop + 6 + 10, 8355711);
    }

    /**
     * This code is extracted from CustomNPC+'s GuiEffectBar class
     */
    private void drawCustomIcon(CustomEffect effect, int drawX, int drawY, int iconRenderSize) {
        ImageData imageData = ClientCacheHandler.getImageData(effect.getIcon());

        if (imageData != null && imageData.imageLoaded()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            imageData.bindTexture();
            int iconU = effect.iconX;
            int iconV = effect.iconY;
            int iconWidth = effect.getWidth();
            int iconHeight = effect.getHeight();
            int texWidth = imageData.getTotalWidth();

            // Use iconYOffset to vertically center the icon
            func_152125_a(drawX, drawY, iconU, iconV, iconWidth, iconHeight, iconRenderSize, iconRenderSize, texWidth, texWidth);
        }
    }

    public void actionPerformed(GuiButton button) {
        onActionPerformed(button);
    }

    public void onActionPerformed(GuiButton button) {
        if (button.id == 201) { // Previous button
            System.out.println("Previous of " + effectPage);
            // First, decrease the page number, then ensure it's not less than 1.
            effectPage = Math.max(1, effectPage - 1);
            System.out.println("New page: " + effectPage);
        } else if (button.id == 202) { // Next button
            System.out.println("Next of " + effectPage);
            // First, increase the page number, then ensure it's not more than maxPages.
            effectPage = Math.min(maxPages, effectPage + 1);
            System.out.println("New page: " + effectPage);
        }
    }
}
