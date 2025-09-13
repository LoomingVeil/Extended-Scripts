package com.veil.extendedscripts.mixins;

import bspkrs.client.util.HUDUtils;
import bspkrs.statuseffecthud.StatusEffectHUD;
import com.veil.extendedscripts.CustomEffectBridge;
import com.veil.extendedscripts.ExtendedScripts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.*;

@Mixin(value=StatusEffectHUD.class)
public class MixinStatusEffectHUD {
    @Unique
    private static final ResourceLocation invResource = new ResourceLocation("textures/gui/container/inventory.png");
    @Shadow
    protected static float zLevel = -150.0F;
    @Shadow
    public static String alignMode = "middleright";
    @Shadow
    public static boolean enableBackground = false;
    @Shadow
    public static boolean enableEffectName = true;
    @Shadow
    public static String effectNameColor = "f";
    @Shadow
    public static String durationColor = "f";
    @Shadow
    public static boolean enableIconBlink = true;
    @Shadow
    public static int durationBlinkSeconds = 10;
    @Shadow
    private static Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap();

    @Invoker("getY")
    private static int getY(int rowCount, int height) {
        throw new AbstractMethodError("Mixin did not apply");
    }

    @Invoker("getX")
    private static int getX(int width) {
        throw new AbstractMethodError("Mixin did not apply");
    }

    @Invoker("shouldRender")
    private static boolean shouldRender(PotionEffect pe, int ticksLeft, int thresholdSeconds) {
        throw new AbstractMethodError("Mixin did not apply");
    }

    /**
     * @author Veil
     * @reason This could not be accomplished with other types of mixins.
     * Fortunately, I believe it to be very unlikely any other mods would Mixin this mod.
     */
    @Overwrite(remap = false)
    private static void displayStatusEffects(Minecraft mc) {
        System.out.println("Test");
        List<CustomEffectBridge> activeEffects = ExtendedScripts.getEffectsList(mc.thePlayer);
        if (!activeEffects.isEmpty()) {
            int yOffset = enableBackground ? 33 : (enableEffectName ? 20 : 18);
            if (activeEffects.size() > 5 && enableBackground) {
                yOffset = 132 / (activeEffects.size() - 1);
            }

            int yBase = getY(activeEffects.size(), yOffset);

            for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect.hasNext(); yBase += yOffset) {
                PotionEffect potionEffect = (PotionEffect) iteratorPotionEffect.next();
                if (!potionMaxDurationMap.containsKey(potionEffect) || (Integer) potionMaxDurationMap.get(potionEffect) < potionEffect.getDuration()) {
                    potionMaxDurationMap.put(potionEffect, potionEffect.getDuration());
                }

                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(invResource);
                int xBase = getX(enableBackground ? 120 : 22 + mc.fontRenderer.getStringWidth("0:00"));
                String potionName = "";
                if (enableEffectName) {
                    potionName = I18n.format(potion.getName());
                    if (potionEffect.getAmplifier() > 0) {
                        System.out.println("Effect level: "+potionEffect.getAmplifier()+" -> "+ExtendedScripts.toRoman(potionEffect.getAmplifier() + 1));
                        potionName = potionName + ExtendedScripts.toRoman(potionEffect.getAmplifier() + 1);
                    }

                    xBase = getX(enableBackground ? 120 : 22 + mc.fontRenderer.getStringWidth(potionName));
                }

                String effectDuration = Potion.getDurationString(potionEffect);
                if (enableBackground) {
                    HUDUtils.drawTexturedModalRect(xBase, yBase, 0, 166, 140, 32, zLevel);
                }

                int stringWidth;
                if (!alignMode.toLowerCase().contains("right")) {
                    if (potion.hasStatusIcon()) {
                        stringWidth = potion.getStatusIconIndex();
                        HUDUtils.drawTexturedModalRect(xBase + (enableBackground ? 6 : 0), yBase + (enableBackground ? 7 : 0), 0 + stringWidth % 8 * 18, 198 + stringWidth / 8 * 18, 18, 18, zLevel);
                    }

                    mc.fontRenderer.drawStringWithShadow("§" + effectNameColor + potionName + "§r", xBase + (enableBackground ? 10 : 4) + 18, yBase + (enableBackground ? 6 : 0), 16777215);
                    if (shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds)) {
                        mc.fontRenderer.drawStringWithShadow("§" + durationColor + effectDuration + "§r", xBase + (enableBackground ? 10 : 4) + 18, yBase + (enableBackground ? 6 : 0) + (enableEffectName ? 10 : 5), 16777215);
                    }
                } else {
                    xBase = getX(0);
                    if (potion.hasStatusIcon()) {
                        stringWidth = potion.getStatusIconIndex();
                        if (!enableIconBlink || enableIconBlink && shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds)) {
                            HUDUtils.drawTexturedModalRect(xBase + (enableBackground ? -24 : -18), yBase + (enableBackground ? 7 : 0), 0 + stringWidth % 8 * 18, 198 + stringWidth / 8 * 18, 18, 18, zLevel);
                        }
                    }

                    stringWidth = mc.fontRenderer.getStringWidth(potionName);
                    mc.fontRenderer.drawStringWithShadow("§" + effectNameColor + potionName + "§r", xBase + (enableBackground ? -10 : -4) - 18 - stringWidth, yBase + (enableBackground ? 6 : 0), 16777215);
                    stringWidth = mc.fontRenderer.getStringWidth(effectDuration);
                    if (shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds)) {
                        mc.fontRenderer.drawStringWithShadow("§" + durationColor + effectDuration + "§r", xBase + (enableBackground ? -10 : -4) - 18 - stringWidth, yBase + (enableBackground ? 6 : 0) + (enableEffectName ? 10 : 5), 16777215);
                    }
                }
            }

            List<PotionEffect> toRemove = new LinkedList();
            Iterator i$ = potionMaxDurationMap.keySet().iterator();

            PotionEffect pe;
            while (i$.hasNext()) {
                pe = (PotionEffect) i$.next();
                if (!activeEffects.contains(pe)) {
                    toRemove.add(pe);
                }
            }

            i$ = toRemove.iterator();

            while (i$.hasNext()) {
                pe = (PotionEffect) i$.next();
                potionMaxDurationMap.remove(pe);
            }
        }
    }
}
