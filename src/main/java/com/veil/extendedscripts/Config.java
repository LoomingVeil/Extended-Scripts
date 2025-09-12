package com.veil.extendedscripts;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
    public static Configuration config;
    public static boolean enableEffectPages;
    public static boolean enableEffectPageModification;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
        }

        loadConfig();
    }

    public static void loadConfig() {
        config.load();

        enableEffectPageModification = config.getBoolean(
            "enableEffectPageModification",
            Configuration.CATEGORY_GENERAL,
            true,
            "You may need to disable this if you have another mod that modifies the in inventory effect screen. Disabling this will also disable showing CustomNPC+ custom effects with your potion effects"
        );
        enableEffectPages = config.getBoolean("enableEffectPages", Configuration.CATEGORY_GENERAL, true, "Enables pages for effects when you have more than five at once.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}
