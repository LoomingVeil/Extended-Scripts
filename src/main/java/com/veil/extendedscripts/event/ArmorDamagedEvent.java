package com.veil.extendedscripts.event;

import com.veil.extendedscripts.extendedapi.event.IArmorDamagedEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.ScriptDamageSource;
import noppes.npcs.scripted.entity.ScriptEntity;
import noppes.npcs.scripted.event.ItemEvent;

/**
 * Called when an equipped scripted item that has the armor_value attribute and is equipped in the armor or hand.
 */
public class ArmorDamagedEvent extends ItemEvent implements IArmorDamagedEvent {
    public int itemDamage;
    public IDamageSource damageSource;
    public IEntity equippedOn;
    public int slot;

    public ArmorDamagedEvent() {
        super(null);
    }

    public ArmorDamagedEvent(IItemCustomizable item, int damage, DamageSource damageSource, EntityLivingBase equippedOn, int slot) {
        super(item);
        this.itemDamage = damage;
        this.damageSource = new ScriptDamageSource(damageSource);
        this.equippedOn = NpcAPI.Instance().getIEntity(equippedOn);
        this.slot = slot > 3 ? -1 : slot; // -1 for hand else 0-3 for armor
    }

    @Override
    public String getHookName() {
        return "armorDamaged";
    }

    /**
     * Gets the amount of durability damage this attack would do to a normal item.
     */
    public int getItemDamage() {
        return itemDamage;
    }

    public IDamageSource getDamageSource() {
        return damageSource;
    }

    /**
     * Get what entity is wearing this item
     */
    public IEntity getEquippedOn() {
        return equippedOn;
    }

    /**
     * 0-3 helmet -> boots. -1 for hand.
     */
    public int getSlot() {
        return slot;
    }
}
