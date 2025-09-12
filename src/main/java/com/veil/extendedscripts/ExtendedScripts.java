package com.veil.extendedscripts;

import com.veil.extendedscripts.properties.ExtendedScriptEntityProperties;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import noppes.npcs.controllers.CustomEffectController;
import noppes.npcs.controllers.data.CustomEffect;
import noppes.npcs.controllers.data.EffectKey;
import noppes.npcs.controllers.data.PlayerEffect;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod(modid = ExtendedScripts.MODID, version = Tags.VERSION, name = "Veil's Extended Scripts", acceptedMinecraftVersions = "[1.7.10]", dependencies = "required-after:customnpcs@[1.10.1,)")
public class ExtendedScripts {
    public static final String MODID = "extendedscripts";
    public static final String VERSION = "1.0";
    public static final Integer SIGNATURE_COLOR = 0xEB1e42;
    public static final int GUI_VIRTUAL_CRAFTING_TABLE = 0;
    public static final int GUI_VIRTUAL_FURNACE = 1;
    public static final int GUI_PERSISTENT_VIRTUAL_FURNACE = 2;
    public static final int GUI_VIRTUAL_ANVIL = 3;
    public static Item scripter;
    public static Item scriptedItem;
    public static final WorldClippers worldClippers = new WorldClippers();
    @SidedProxy(clientSide = "com.veil.extendedscripts.ClientProxy", serverSide = "com.veil.extendedscripts.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ExtendedScripts.MODID)
    public static ExtendedScripts instance;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    public static ExtendedScriptEntityProperties getEntityProperties(Entity entity) {
        ExtendedScriptEntityProperties props = ExtendedScriptEntityProperties.get(entity);
        if (props == null) {
            ExtendedScriptEntityProperties.register(entity);
            props = ExtendedScriptEntityProperties.get(entity);
            if (props == null) {
                System.err.println("Failed to get or register ExtendedScriptEntityProperties for " + entity.getCommandSenderName());
                return null;
            }
        }
        return props;
    }

    public static ExtendedScriptPlayerProperties getPlayerProperties(EntityPlayer player) {
        ExtendedScriptPlayerProperties props = ExtendedScriptPlayerProperties.get(player);
        if (props == null) {
            ExtendedScriptPlayerProperties.register(player);
            props = ExtendedScriptPlayerProperties.get(player);
            if (props == null) {
                System.err.println("Failed to get or register ExtendedScriptEntityProperties for " + player.getCommandSenderName());
                return null;
            }
        }
        return props;
    }

    public static ExtendedWorldData getExtendedWorldData() {
        World overworld = MinecraftServer.getServer().worldServerForDimension(0);

        MapStorage storage = overworld.mapStorage;
        String DATA_ID = ExtendedWorldData.DATA_ID;

        ExtendedWorldData instance = (ExtendedWorldData) storage.loadData(ExtendedWorldData.class, DATA_ID);

        if (instance == null) {
            instance = new ExtendedWorldData(DATA_ID);
            storage.setData(DATA_ID, instance);
        }

        return instance;
    }

    public static File getModWorldDirectory() {
        World overworld = MinecraftServer.getServer().worldServerForDimension(0);
        File worldDirectory = overworld.getSaveHandler().getWorldDirectory();

        // Create a new File object for your mod's folder
        File modDirectory = new File(worldDirectory, MODID);

        // Check if the directory exists and create it if it doesn't
        if (!modDirectory.exists()) {
            boolean success = modDirectory.mkdirs();
            if (success) {
                System.out.println("Created save data directory: " + modDirectory.getAbsolutePath());
            } else {
                System.err.println("Failed to create save data directory: " + modDirectory.getAbsolutePath());
            }
        }

        return modDirectory;
    }

    public static List<CustomEffectBridge> getEffectsList(EntityPlayer player) {
        List<CustomEffectBridge> effects = new ArrayList<>();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            effects.add(new CustomEffectBridge(effect.getPotionID(), effect.getDuration(), effect.getAmplifier()));
        }

        ConcurrentHashMap<EffectKey, PlayerEffect> customEffects = CustomEffectController.getInstance().getPlayerEffects(player);
        HashMap<Integer, CustomEffect> registeredCustomEffects = CustomEffectController.getInstance().getCustomEffects();

        for (EffectKey key : customEffects.keySet()) {
            PlayerEffect effect = customEffects.get(key);
            effects.add(new CustomEffectBridge(effect, registeredCustomEffects.get(key.getId())));
        }

        return effects;
    }

    public static String toRoman(int num) {
        if (num < 1) {
            return null;
        }

        int[] values = {200, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"CC", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                result.append(symbols[i]);
                num -= values[i];
            }
        }

        return result.toString();
    }
}


