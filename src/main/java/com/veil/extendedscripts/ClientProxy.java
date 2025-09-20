package com.veil.extendedscripts;

import com.veil.extendedscripts.projectile.CustomProjectileRender;
import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    public static KeyBinding openScriptingActionKey;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        FMLCommonHandler.instance().bus().register(new CommonEventHandler());
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
        // MinecraftForge.EVENT_BUS.register(new ClientTickHandler());

        RenderingRegistry.registerEntityRenderingHandler(EntityCustomProjectile.class, new CustomProjectileRender(new ResourceLocation("textures/entity/arrow.png")));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        openScriptingActionKey = new KeyBinding(
            "key.openScripting",
            Keyboard.KEY_BACKSLASH,
            "key.categories.customnpc"
        );

        ClientRegistry.registerKeyBinding(openScriptingActionKey);
    }
}
