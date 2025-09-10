package com.veil.extendedscripts;

import net.minecraft.potion.PotionEffect;
import noppes.npcs.controllers.data.CustomEffect;
import noppes.npcs.controllers.data.PlayerEffect;

/**
 * A class that can store both the data of a {@link net.minecraft.potion.PotionEffect} or {@link noppes.npcs.controllers.data.CustomEffect}
 */
public class CustomEffectBridge extends PotionEffect {
    private boolean isCustom = false;
    private CustomEffect customEffect;


    public CustomEffectBridge(int id, int duration, int amplifier) {
        super(id, duration, amplifier);
    }

    public CustomEffectBridge(PlayerEffect playerEffect, CustomEffect effect) {
        super(playerEffect.getId(), playerEffect.getDuration() * 20, playerEffect.getLevel());
        this.isCustom = true;
        this.customEffect = effect;
    }

    public CustomEffect getCustomEffect() {
        return customEffect;
    }

    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public String getEffectName() {
        if (isCustom) {
            return customEffect.getName();
        }
        return super.getEffectName();
    }
}
