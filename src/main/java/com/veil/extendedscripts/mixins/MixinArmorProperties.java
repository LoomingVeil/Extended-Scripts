package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.event.ArmorDamagedEvent;
import com.veil.extendedscripts.properties.PlayerAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.item.IItemCustom;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.INpcScriptHandler;
import noppes.npcs.controllers.data.PlayerDataScript;
import noppes.npcs.extendedapi.item.IItemCustomizable;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ISpecialArmor.ArmorProperties.class}, remap = false)
public class MixinArmorProperties {
    @Inject(method = "ApplyArmor", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private static void modifyDamage(
        EntityLivingBase entity,
        ItemStack[] inventory,
        DamageSource source,
        double damage,
        CallbackInfoReturnable<Float> cir
    ) {
        // Damage starts out as the damage * 25 for some reason.
        inventory = new ItemStack[] {
            inventory[0], inventory[1],
            inventory[2], inventory[3],
            entity.getHeldItem()
        };

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                IItemStack itemStack = NpcAPI.Instance().getIItemStack(inventory[i]);
                if (itemStack.hasCustomAttribute(PlayerAttribute.ARMOR_VALUE.asSnakeCase())) {
                    double armorValue = itemStack.getCustomAttribute(PlayerAttribute.ARMOR_VALUE.asSnakeCase());
                    if (inventory[i].getItem() instanceof ItemScripted) {
                        damage -= armorValue / 25D * damage;
                        IItemCustomizable extendedCustomItem = (IItemCustomizable) itemStack;
                        IItemCustom customItem = (IItemCustom) itemStack;
                        double absorb = damage * (armorValue / 25D);
                        int itemDamage = (int)(absorb / 25D < 1 ? 1 : absorb / 25D);

                        ArmorDamagedEvent event = new ArmorDamagedEvent(customItem, itemDamage, source, entity, i);

                        INpcScriptHandler handler = (INpcScriptHandler) customItem.getScriptHandler();
                        handler.callScript(event.getHookName(), event);
                        NpcAPI.EVENT_BUS.post(event);
                    } else if (inventory[i].getItem() instanceof ItemArmor) {
                        try {
                            ItemArmor armor = (ItemArmor) inventory[i].getItem();
                            ISpecialArmor.ArmorProperties properties = new ISpecialArmor.ArmorProperties(0, armorValue / 25D, armor.getMaxDamage() + 1 - inventory[i].getItemDamage());
                            damage -= properties.AbsorbRatio / 25D * damage;
                        } catch (ClassCastException ignored) { }
                        // If there is another error, I would like to know about it.
                    }
                }
            }
        }

        cir.setReturnValue(new Float(damage / 25F));
    }
}
