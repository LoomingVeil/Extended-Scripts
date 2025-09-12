package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.Config;
import com.veil.extendedscripts.ExtendedScripts;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // Called when this config is first loaded
        if (!Config.hasBeenLoaded) {
            Config.init(new File("config/", ExtendedScripts.MODID +".cfg")); // I can't believe this worked
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null; // return custom refmap if you have one, else null
    }


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!Config.enableEffectPageModification) {
            if (mixinClassName.equals("com.veil.extendedscripts.mixins.MixinGuiContainerCreative")) {
                return false;
            } else if (mixinClassName.equals("com.veil.extendedscripts.mixins.MixinInventoryEffectRenderer")) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // You can modify the set of target classes here if needed
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
