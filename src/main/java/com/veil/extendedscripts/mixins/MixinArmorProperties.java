package com.veil.extendedscripts.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.veil.extendedscripts.VirtualArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.Arrays;

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
        System.out.println("Damage: "+damage);
        damage = damage * 25;
        System.out.println("Base Damage: "+(damage / 25)+" After magnification: "+damage);
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                IItemStack itemStack = NpcAPI.Instance().getIItemStack(inventory[i]);

                if (itemStack.hasAttribute("armor_value")) {
                    double armorValue = itemStack.getAttribute("armor_value");
                    if (inventory[i].getItem() instanceof ItemScripted) {
                        damage -= armorValue / 25D * damage;
                    } else if (inventory[i].getItem() instanceof ItemArmor) {
                        ItemArmor armor = (ItemArmor) inventory[i].getItem();
                        ISpecialArmor.ArmorProperties properties = new ISpecialArmor.ArmorProperties(0, armorValue / 25D, armor.getMaxDamage() + 1 - inventory[i].getItemDamage());
                        damage -= properties.AbsorbRatio / 25D * damage;
                    }

                }
            }
        }

        System.out.println("Returning "+(damage / 25));
        cir.setReturnValue(new Float(damage / 25F));
    }

    /*@ModifyArgs(
        method = "ApplyArmor",
        at = @At("HEAD"),
        remap = false
    )
    private static void modifyArgs(Args args) {
        if (!(args.get(0) instanceof EntityPlayer)) return;
        ItemStack[] inventory = args.get(1);
        int armorValue;

        System.out.println("Attacked!");
        IPlayer npcPlayer = NpcAPI.Instance().getPlayer(((EntityPlayer) args.get(0)).getDisplayName());
        if (npcPlayer.getAttributes().hasAttribute("armor_value")) {
            armorValue = (int) npcPlayer.getAttributes().getAttribute("armor_value").getValue();
        } else {
            return;
        }

        ItemStack[] newInventory = Arrays.copyOf(inventory, inventory.length + 1);

        /*ItemArmor.ArmorMaterial fakeMaterial = EnumHelper.addArmorMaterial(
            "FAKE_MATERIAL",
            42,
            new int[]{armorValue, armorValue, armorValue, armorValue},
            0
        );

        VirtualArmor fakeArmor = new VirtualArmor(armorValue);
        ItemStack customArmorStack = new ItemStack(fakeArmor);

        System.out.println("Adding an armor with value of "+((VirtualArmor) customArmorStack.getItem()).getProperties(null, null, null, 0, 0).AbsorbRatio);

        newInventory[inventory.length] = customArmorStack;

        args.set(1, newInventory);
    }*/
    /*@Redirect(
        method = "ApplyArmor",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"),
        remap = false
    )
    private static ItemStack redirectInventoryAccess(ItemStack[] inventory, int index) {
        if (index == -1) {
            return new ItemStack(new VirtualArmor(0)); // A dummy item that won't be destroyed by losing durability.
        }
        return inventory[index];
    }


    @Inject(
        method = "ApplyArmor",
        at = @At(value = "INVOKE", target = "java/util/ArrayList.size()I", ordinal = 0),
        locals = LocalCapture.CAPTURE_FAILHARD,
        remap = false
    )
    private static void addVirtualArmor(EntityLivingBase entity, ItemStack[] inventory, DamageSource source, double damage,
                                        CallbackInfoReturnable<Float> cir,
                                        ArrayList<ISpecialArmor.ArmorProperties> dmgVals) {

        if (entity instanceof EntityPlayer) {
            IPlayer npcPlayer = NpcAPI.Instance().getPlayer(((EntityPlayer) entity).getDisplayName());
            if (npcPlayer.getAttributes().hasAttribute("armor_value")) {
                int armorValue = (int) npcPlayer.getAttributes().getAttribute("armor_value").getValue();
                VirtualArmor fakeArmor = new VirtualArmor(armorValue);
                ItemStack customArmorStack = new ItemStack(fakeArmor);

                ISpecialArmor.ArmorProperties customProp = fakeArmor.getProperties(entity, customArmorStack, source, damage / 25D, 0);
                customProp.Slot = -1;
                dmgVals.add(customProp);
            }
        }
    }*/
}
