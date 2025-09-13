package com.veil.extendedscripts;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import org.lwjgl.Sys;

public class Config {
    public static Configuration config;
    public static boolean hasBeenLoaded = false;
    public static boolean enableEffectPages;
    public static boolean enableEffectPageModification;
    public static boolean showPotionEffectLevelsAsNumbers;
    public static void init(File configFile) {
        if (!hasBeenLoaded) {
            if (config == null) {
                config = new Configuration(configFile);
            }

            loadConfig();
            hasBeenLoaded = true;
        }
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
        showPotionEffectLevelsAsNumbers = config.getBoolean(
            "showPotionEffectLevelsAsNumbers",
            Configuration.CATEGORY_GENERAL,
            false,
            "When true, displays potions effect levels as numbers instead of roman numerals."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}
