package com.veil.extendedscripts;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import noppes.npcs.api.item.IItemStack;

public class VirtualArmor extends Item implements ISpecialArmor {
    int armorPoints;

    public VirtualArmor(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(0, armorPoints / 25D, Integer.MAX_VALUE);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }
}
