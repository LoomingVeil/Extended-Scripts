package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.ExtendedEnumGuiType;
import com.veil.extendedscripts.item.IScriptedItemVariant;
import com.veil.extendedscripts.item.ItemCustomVariantRenderer;
import com.veil.extendedscripts.projectile.CustomProjectileRender;
import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.client.gui.script.GuiScriptItem;
import noppes.npcs.entity.EntityNPCInterface;
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


    @Override
    public void openGui(EntityNPCInterface npc, int gui, int x, int y, int z) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Container container = this.getContainer(gui, minecraft.thePlayer, x, y, z, npc);
        GuiScreen guiscreen = this.getGui(npc, gui, container, x, y, z);
        if (guiscreen != null) {
            minecraft.displayGuiScreen(guiscreen);
        }
    }

    /**
     * Maybe there will be some containers in the future
     */
    public Container getContainer(int gui, EntityPlayer player, int x, int y, int z, EntityNPCInterface npc) {
        return null;
    }

    public GuiScreen getGui(EntityNPCInterface npc, int gui, Container container, int x, int y, int z) {
        ExtendedEnumGuiType guiType = ExtendedEnumGuiType.values()[gui];

        if (guiType == ExtendedEnumGuiType.ScriptArmor) {
            return new GuiScriptItem();
        }

        return null;
    }

    public static void registerItem(IScriptedItemVariant item) {
        if (item instanceof IScriptedItemVariant) {
            MinecraftForgeClient.registerItemRenderer((Item) item, new ItemCustomVariantRenderer());
        }
    }
}
