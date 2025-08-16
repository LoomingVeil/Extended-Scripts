package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.*;
import com.veil.extendedscripts.guis.VirtualGuiHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import kamkeel.npcs.controllers.data.attribute.AttributeValueType;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.api.AbstractNpcAPI;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ExtendedScripts.instance, new VirtualGuiHandler());
        PacketHandler.registerMessages();

        GameRegistry.registerItem(ExtendedScripts.worldClippers, "world_clippers");
    }

    public void init(FMLInitializationEvent event) {
        ExtendedScripts.scriptedItem = GameRegistry.findItem("customnpcs", "scripted_item");
        if (ExtendedScripts.scriptedItem == null) {
            System.err.println("Could not find scripted item!");
        }

        FMLCommonHandler.instance().bus().register(new ScriptedObjectEventHandler());
        MinecraftForge.EVENT_BUS.register(new ScriptedObjectEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        AbstractNpcAPI.Instance().addGlobalObject("extAPI", ExtendedAPI.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("AnimationType", AnimationType.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("Color", ColorCodes.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("EntityType", EntityType.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("AttributeSection", ExtendedAttributeSection.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("AttributeValueType", ExtendedAttributeValueType.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("JobType", JobType.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("Key", Keys.Instance);
        AbstractNpcAPI.Instance().addGlobalObject("RoleType", JobType.Instance);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new InspectItemCommand());
        event.registerServerCommand(new ExtendedScriptsUtilitiesCommand());

        // Remove any attributes that have already been registered this game instance
        Map<String, AttributeDefinition> oldAttributes = ExtendedAPI.getLastWorldsAttributes();
        System.out.println("There are "+oldAttributes.size() + " attributes that need to be removed");

        for (String attributeKey : oldAttributes.keySet()) {
            ExtendedAPI.unregisterAttributeFromWorld(attributeKey);
        }

        ExtendedWorldData data = ExtendedScripts.getExtendedWorldData();

        if (data.getExtendedAttributes() == null) {
            System.out.println("Extended attributes are null. This is an error!");
            return;
        }

        Collection<AttributeDefinition> extendedAttributes = data.getExtendedAttributes();
        Collection<AttributeDefinition> currentAttributes = AttributeController.getAllAttributes();
        Set<AttributeDefinition> attributesToAdd = new HashSet<>();

        for (AttributeDefinition attribute : extendedAttributes) {
            if (!currentAttributes.contains(attribute) && !currentAttributes.contains(attribute)) {
                attributesToAdd.add(attribute);
            }
        }

        int count = 0;
        for (AttributeDefinition attribute : attributesToAdd) {
            ExtendedAPI.registerAttribute(attribute);
            count += 1;
        }
        System.out.println("Added "+count+" Attributes and Attributed to Add is of length "+attributesToAdd.size());
    }

}
