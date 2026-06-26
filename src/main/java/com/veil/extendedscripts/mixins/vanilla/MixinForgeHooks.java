package com.veil.extendedscripts.mixins.vanilla;

import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.properties.PlayerAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ForgeHooks.class})
public class MixinForgeHooks {
    @Inject(method = "getTotalArmorValue", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onGetTotalArmorValue(EntityPlayer player, CallbackInfoReturnable<Integer> cir) {
        int base = cir.getReturnValue();

        base += ExtendedAPI.getAttribute(player, PlayerAttribute.ARMOR_VALUE);

        cir.setReturnValue(base);
    }
}
