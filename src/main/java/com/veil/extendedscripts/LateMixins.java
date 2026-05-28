package com.veil.extendedscripts;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class LateMixins implements ILateMixinLoader {
    public static final MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment().getSide();

    @Override
    public String getMixinConfig() {
        return "mixins.extendedscripts.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if (side == MixinEnvironment.Side.CLIENT) {
            mixins.add("MixinNpcGuiScript");
            mixins.add("MixinGuiScriptInterface");
//            mixins.add("MixinGuiNPCInterfaceKeyboardNav");
        }
        return mixins;
    }
}
