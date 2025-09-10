package com.veil.extendedscripts.extendedapi.item;

import com.veil.extendedscripts.extendedapi.IPotionEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

import java.util.List;

/**
 * Use this class to make custom potions.
 * To make an IPotionEffect see {@link com.veil.extendedscripts.extendedapi.AbstractExtendedAPI#getIPotionEffect(int, int, int)}
 */
public interface IItemPotion {
    IPotionEffect[] getEffects();

    boolean hasEffect(int id);

    /**
     * Adds an effect to the potion if it does not already exist.
     */
    void addEffect(IPotionEffect effect);

    /**
     * Adds an effect to the potion and overrides it if it already exists.
     */
    void setEffect(IPotionEffect effect);

    void removeEffect(int id);
}
