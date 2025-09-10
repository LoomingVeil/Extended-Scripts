package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.IPotionEffect;
import net.minecraft.potion.Potion;

public class PotionEffect implements IPotionEffect {
    private String name;
    private int ID;
    private int duration;
    private int amplifier;
    private String icon;

    public PotionEffect(net.minecraft.potion.PotionEffect mcEffect) {
        this.name = mcEffect.getEffectName();
        this.ID = mcEffect.getPotionID();
        this.duration = mcEffect.getDuration();
        this.amplifier = mcEffect.getAmplifier();
    }

    public PotionEffect(int ID, int duration, int amplifier) throws Exception {
        setID(ID);
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) throws Exception {
        try {
            this.name = Potion.potionTypes[ID].getName();
            this.ID = ID;
        } catch (Exception e) {
            throw new Exception("Invalid potion ID!");
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }
}
