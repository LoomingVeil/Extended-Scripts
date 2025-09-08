package com.veil.extendedscripts.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={InventoryPlayer.class})
public class MixinInventoryPlayer {
    @Shadow
    public EntityPlayer player;

    @Inject(method = "getTotalArmorValue", at = @At("RETURN"), cancellable = true)
    private void onGetTotalArmorValue(CallbackInfoReturnable<Integer> cir) {
        int base = cir.getReturnValue();

        IPlayer npcPlayer = NpcAPI.Instance().getPlayer(player.getDisplayName());
        if (npcPlayer.getAttributes().hasAttribute("armor_value")) {
            base += (int) npcPlayer.getAttributes().getAttribute("armor_value").getValue();
        }

        System.out.println("Returning "+base);
        cir.setReturnValue(base);
    }
}
