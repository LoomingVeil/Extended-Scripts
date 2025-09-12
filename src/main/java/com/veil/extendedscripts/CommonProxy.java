package com.veil.extendedscripts;

import com.veil.extendedscripts.commands.VeilCommand;
import com.veil.extendedscripts.commands.InspectCommand;
import com.veil.extendedscripts.constants.*;
import com.veil.extendedscripts.guis.VirtualGuiHandler;
import com.veil.extendedscripts.properties.EntityAttribute;
import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import com.veil.extendedscripts.properties.PlayerAttribute;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
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
        Config.init(event.getSuggestedConfigurationFile());

        NetworkRegistry.INSTANCE.registerGuiHandler(ExtendedScripts.instance, new VirtualGuiHandler());
        PacketHandler.registerMessages();

        GameRegistry.registerItem(ExtendedScripts.worldClippers, "world_clippers");

        FMLCommonHandler.instance().bus().register(new GlobalFileCopierHandler());
        MinecraftForge.EVENT_BUS.register(new GlobalFileCopierHandler());
    }

    public void init(FMLInitializationEvent event) {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityCustomProjectile.class, "CustomProjectile", id);
        EntityRegistry.registerModEntity(EntityCustomProjectile.class, "CustomProjectile", id, ExtendedScripts.instance, 128, 10, true);

        ExtendedScripts.scriptedItem = GameRegistry.findItem("customnpcs", "scripted_item");
        ExtendedScripts.scriptedItem = GameRegistry.findItem("customnpcs", "npcScripter");
        if (ExtendedScripts.scriptedItem == null) {
            System.err.println("Could not find scripted item!");
            return;
        }

        FMLCommonHandler.instance().bus().register(new ScriptedObjectEventHandler());
        MinecraftForge.EVENT_BUS.register(new ScriptedObjectEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        AttributeEventHandler.init();

        AbstractNpcAPI API = AbstractNpcAPI.Instance();
        API.addGlobalObject("extAPI", ExtendedAPI.Instance);
        API.addGlobalObject("AnimationType", AnimationType.Instance);
        API.addGlobalObject("Color", ColorCodes.Instance);
        API.addGlobalObject("EntityType", EntityType.Instance);
        API.addGlobalObject("Job", JobType.Instance);
        API.addGlobalObject("Key", Keys.Instance);
        API.addGlobalObject("Role", RoleType.Instance);
        API.addGlobalObject("MouseButton", MouseButton.Instance);
        API.addGlobalObject("Particle", ParticleType.Instance);
        API.addGlobalObject("ItemUseAction", ItemUseAction.Instance);
        API.addGlobalObject("AttributeSection", ExtendedAttributeSection.Instance);
        API.addGlobalObject("AttributeValueType", ExtendedAttributeValueType.Instance);
        API.addGlobalObject("BlockSide", BlockSide.Instance);
        API.addGlobalObject("ArmorSlot", ArmorType.Instance);
        API.addGlobalObject("ItemType", ItemType.Instance);
        API.addGlobalObject("Effect", EffectID.Instance);

        if (Loader.isModLoaded("customnpcs")) {
            ModContainer container = Loader.instance().getIndexedModList().get("customnpcs");
            if (container != null) {
                ArtifactVersion loadedVersion = container.getProcessedVersion();
                ArtifactVersion intendedVersion = new DefaultArtifactVersion("1.10.1");
                if (loadedVersion.compareTo(intendedVersion) >= 0) {
                    attributeInit();
                } else {
                    System.out.println("You are using an outdated version of CustomNpc+. For access to all features, update to at least version "+intendedVersion);
                }
            }
        }
    }

    /**
     * It was originally our intention to make Extended Scripts compatible with CustomNPC 1.9.3, but after trying, it did not go very well.
     * Still, if I find out exactly how to do it in the future, having all the attribute logic in one function is useful.
     */
    public void attributeInit() {
        FMLCommonHandler.instance().bus().register(new AttributeEventHandler());
        MinecraftForge.EVENT_BUS.register(new AttributeEventHandler());

        AbstractNpcAPI API = AbstractNpcAPI.Instance();
        API.addGlobalObject("AttributeSection", ExtendedAttributeSection.Instance);
        API.addGlobalObject("AttributeValueType", ExtendedAttributeValueType.Instance);

        AttributeController.registerAttribute(EntityAttribute.GRAVITY.asSnakeCase(), "Gravity", ColorCodes.Instance.DARK_PURPLE, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.DOWNWARD_GRAVITY.asSnakeCase(), "Downward Gravity", ColorCodes.Instance.DARK_PURPLE, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.UPWARD_GRAVITY.asSnakeCase(), "Upward Gravity", ColorCodes.Instance.DARK_PURPLE, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.UNDERWATER_GRAVITY.asSnakeCase(), "Underwater Gravity", ColorCodes.Instance.DARK_AQUA, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.UNDERWATER_DOWNWARD_GRAVITY.asSnakeCase(), "Downward Underwater Gravity", ColorCodes.Instance.DARK_AQUA, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.UNDERWATER_UPWARD_GRAVITY.asSnakeCase(), "Upward Underwater Gravity", ColorCodes.Instance.DARK_AQUA, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.JUMP_POWER_VERTICAL.asSnakeCase(), "Jump Boost", ColorCodes.Instance.GREEN, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.JUMP_POWER_HORIZONTAL.asSnakeCase(), "Jump Breadth", ColorCodes.Instance.DARK_GREEN, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(EntityAttribute.MAX_FALL_DISTANCE.asSnakeCase(), "Max Fall Distance", ColorCodes.Instance.WHITE, AttributeValueType.FLAT, AttributeDefinition.AttributeSection.BASE);
        AttributeController.registerAttribute(PlayerAttribute.FLIGHT_SPEED_HORIZONTAL.asSnakeCase(), "Flight Speed", ColorCodes.Instance.WHITE, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(PlayerAttribute.FLIGHT_SPEED_VERTICAL.asSnakeCase(), "Vertical Flight Speed", ColorCodes.Instance.WHITE, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(PlayerAttribute.SWIM_BOOST_WATER.asSnakeCase(), "Swim Boost", ColorCodes.Instance.AQUA, AttributeValueType.PERCENT, AttributeDefinition.AttributeSection.MODIFIER);
        AttributeController.registerAttribute(PlayerAttribute.ARMOR_VALUE.asSnakeCase(), "Armor Value", ColorCodes.Instance.GRAY, AttributeValueType.FLAT, AttributeDefinition.AttributeSection.BASE);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new InspectCommand());
        event.registerServerCommand(new VeilCommand());

        // Remove any attributes that have already been registered this game instance
        Map<String, AttributeDefinition> oldAttributes = ExtendedAPI.getLastWorldsAttributes();

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

        for (AttributeDefinition attribute : attributesToAdd) {
            ExtendedAPI.registerAttribute(attribute);
        }
    }

}
