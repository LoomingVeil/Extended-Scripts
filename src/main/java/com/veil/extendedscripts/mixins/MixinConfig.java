package com.veil.extendedscripts.mixins;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // Called when this config is first loaded
        System.out.println("MyMixinPlugin loaded for: " + mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return null; // return custom refmap if you have one, else null
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // System.out.println("Mixin "+mixinClassName+"?");
        if (mixinClassName.equals("com.example.mixin.BadMixin")) {
            return false; // disables it
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // You can modify the set of target classes here if needed
    }

    @Override
    public List<String> getMixins() {
        return null; // null means "use whatever is in mixins.json"
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
