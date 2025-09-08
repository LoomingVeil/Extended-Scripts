package com.veil.extendedscripts.mixins;

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

        try {
            IPlayer npcPlayer = NpcAPI.Instance().getPlayer(player.getDisplayName());
            if (npcPlayer.getAttributes().hasAttribute("armor_value")) {
                base += (int) npcPlayer.getAttributes().getAttribute("armor_value").getValue();
            }
        } catch (Exception ignored) { }

        cir.setReturnValue(base);
    }
}
