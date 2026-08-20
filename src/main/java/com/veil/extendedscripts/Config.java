package com.veil.extendedscripts;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import org.lwjgl.Sys;

public class Config {
    public static Configuration config;
    public static boolean hasBeenLoaded = false;
    public static boolean enableEffectPages;
    public static boolean enableEffectPageModification;
    public static boolean showPotionEffectLevelsAsNumbers;
    public static boolean enableScriptedArmorTextures;
    public static String[] extraAttributeSlots;
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
        enableScriptedArmorTextures = config.getBoolean(
            "enableScriptedArmorTextures", Configuration.CATEGORY_GENERAL, true,
            "When true, scripted items that is also armor can render custom textures. However, this may cause crashes with certain mods such as Angelica"
        );
        showPotionEffectLevelsAsNumbers = config.getBoolean(
            "showPotionEffectLevelsAsNumbers",
            Configuration.CATEGORY_GENERAL,
            false,
            "When true, displays potions effect levels as arabic numerals instead of roman numerals."
        );
        extraAttributeSlots = config.getStringList("extraAttributeSlots", Configuration.CATEGORY_GENERAL, new String[] {""},
            "Here you can add nbt paths that point to item slots in order to add slots which that will be checked when calculating attributes\n"
                + "Usage: <tag1>.<tag2>.<tag...>:<container_index> The container index is optional. Example: 'Inventory:0' accesses the first slot in the inventory\n"
                + "These paths are constructed by placing '.' between each NBT tag key and ':' between the path and the container index if needed.\n"
                + " You may find the /inspect useful for mapping out the path you want.\n"
                + "Be careful as targeting a slot that already has its attributes counted like an armor slot will cause it to be counted twice"
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}
