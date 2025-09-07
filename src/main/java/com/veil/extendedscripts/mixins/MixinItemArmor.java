package com.veil.extendedscripts.mixins;

import net.minecraft.item.ItemArmor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemArmor.class})
public class MixinItemArmor {
    /*@Shadow
    @Final
    @Mutable
    public int armorType;

    // 2. Inject into the constructor. Its name in bytecode is "<init>".
    //    We inject at "RETURN" to run our code after the original constructor finishes.
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructorFinished(ItemArmor.ArmorMaterial material, int renderIndex, int type, CallbackInfo ci) {
        // 3. Now you can modify the field's value.
        //    'this.armorType' refers to the field in the ItemArmor instance.
        //    This example changes the armorType from 1 (chestplate) to 2 (leggings).
        if (this.armorType == 1) {
            this.armorType = 2;
        }
    }*/
}
