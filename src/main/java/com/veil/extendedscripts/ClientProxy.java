package com.veil.extendedscripts;

import com.veil.extendedscripts.projectile.CustomProjectileRender;
import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        FMLCommonHandler.instance().bus().register(new CommonEventHandler());
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());

        RenderingRegistry.registerEntityRenderingHandler(EntityCustomProjectile.class, new CustomProjectileRender(new ResourceLocation("textures/entity/arrow.png")));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        ExtendedScripts.openScriptingActionKey = new KeyBinding(
            "key.openScripting",
            Keyboard.KEY_BACKSLASH,
            "key.categories.customnpc"
        );

        ClientRegistry.registerKeyBinding(ExtendedScripts.openScriptingActionKey);
    }
}
