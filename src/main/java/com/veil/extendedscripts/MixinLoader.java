package com.veil.extendedscripts;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name("VeilMixinCore")
@IFMLLoadingPlugin.SortingIndex(1001)
public class MixinLoader implements IFMLLoadingPlugin {

    static {
        System.setProperty("mixin.hotSwap", "false");
        System.setProperty("mixin.env.disableRefMap", "true");

        org.spongepowered.asm.launch.MixinBootstrap.init();
        org.spongepowered.asm.mixin.Mixins.addConfiguration("mixins.extendedscripts.json");
    }

    @Override public String[] getASMTransformerClass() { return new String[0]; }
    @Override public String getModContainerClass() { return null; }
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(Map<String, Object> data) {}
    @Override public String getAccessTransformerClass() { return null; }
}