package com.veil.extendedscripts.items;

import com.veil.extendedscripts.extendedapi.IPotionEffect;
import com.veil.extendedscripts.extendedapi.item.IItemPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.scripted.item.ScriptItemStack;

import java.util.List;

public class ScriptItemPotion extends ScriptItemStack implements IItemPotion {
    protected ItemPotion potion;

    public ScriptItemPotion(ItemStack item) {
        super(item);
        this.potion = (ItemPotion) item.getItem();
    }

    @Override
    public int getType() {
        return 8;
    }

    public IPotionEffect[] getEffects() {
        List<PotionEffect> effects = potion.getEffects(item);
        IPotionEffect[] retEffects = new IPotionEffect[effects.size()];

        for (int i = 0; i < effects.size(); i++) {
            retEffects[i] = new com.veil.extendedscripts.PotionEffect(effects.get(i));
        }

        return retEffects;
    }

    public boolean hasEffect(int id) {
        if (!item.hasTagCompound()) {
            return false;
        }

        NBTTagCompound tag = item.getTagCompound();
        if (tag == null || !tag.hasKey("CustomPotionEffects", 9)) {
            return false;
        }

        NBTTagList effectList = tag.getTagList("CustomPotionEffects", 10);
        for (int i = 0; i < effectList.tagCount(); ++i) {
            NBTTagCompound effectTag = effectList.getCompoundTagAt(i);
            if (effectTag.hasKey("Id", 3) && effectTag.getInteger("Id") == id) {
                return true;
            }
        }
        return false;
    }

    public void addEffect(IPotionEffect effect) {
        if (hasEffect(effect.getID())) {
            return;
        }

        addEffectToPotion(effect);
    }

    public void setEffect(IPotionEffect effect) {
        if (hasEffect(effect.getID())) {
            removeEffect(effect.getID());
        }

        addEffectToPotion(effect);
    }

    private void addEffectToPotion(IPotionEffect effect) {
        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            item.setTagCompound(tag);
        }

        NBTTagList effectList;
        if (tag.hasKey("CustomPotionEffects", 9)) { // 9 is the ID for a List tag
            effectList = tag.getTagList("CustomPotionEffects", 10);
        } else {
            effectList = new NBTTagList();
            tag.setTag("CustomPotionEffects", effectList);
        }

        NBTTagCompound effectTag = new NBTTagCompound();

        effectTag.setInteger("Id", effect.getID());
        effectTag.setInteger("Duration", effect.getDuration());
        effectTag.setInteger("Amplifier", effect.getAmplifier());

        effectList.appendTag(effectTag);
        tag.setTag("CustomPotionEffects", effectList);
    }

    public void removeEffect(int id) {
        if (!item.hasTagCompound()) {
            return;
        }

        NBTTagCompound tag = item.getTagCompound();
        if (tag == null || !tag.hasKey("CustomPotionEffects", 9)) {
            return;
        }

        NBTTagList effectList = tag.getTagList("CustomPotionEffects", 10);
        int indexToRemove = -1;

        for (int i = 0; i < effectList.tagCount(); ++i) {
            NBTTagCompound effectTag = effectList.getCompoundTagAt(i);
            if (effectTag.hasKey("Id", 3) && effectTag.getInteger("Id") == id) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            effectList.removeTag(indexToRemove);
            System.out.println("Removed effect with ID " + id + " from the potion.");
        } else {
            System.out.println("Effect with ID " + id + " was not found on the potion.");
        }

        if (effectList.tagCount() == 0) {
            tag.removeTag("CustomPotionEffects");
        }
    }
}
