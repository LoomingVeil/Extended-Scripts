package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.*;
import com.veil.extendedscripts.extendedapi.AbstractExtendedAPI;
import com.veil.extendedscripts.extendedapi.IPotionEffect;
import com.veil.extendedscripts.extendedapi.IShapeMaker;
import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import com.veil.extendedscripts.properties.EntityAttribute;
import com.veil.extendedscripts.projectile.EntityCustomProjectile;
import com.veil.extendedscripts.properties.PlayerAttribute;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import kamkeel.npcs.controllers.data.attribute.AttributeValueType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IAttributeDefinition;

import java.io.IOException;
import java.util.*;

/**
 * This object stores functions available to all scripting handlers through the "extAPI" keyword.
 */
public class ExtendedAPI implements AbstractExtendedAPI {
    public static final ExtendedAPI Instance = new ExtendedAPI();
    private static final Map<String, AttributeDefinition> lastWorldsAttributes = new HashMap<>();

    public static Map<String, AttributeDefinition> getLastWorldsAttributes() {
        return lastWorldsAttributes;
    }

    public IShapeMaker getShapeMaker() {
        return (IShapeMaker) ShapeMaker.Instance;
    }

    private boolean doesTextureExist(String path) {
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        try {
            resourceManager.getResource(new ResourceLocation(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets an array of the names of the registered entities in the game.
     * These names can be used in functions like {@link #createIEntity(String, IWorld)}.
     */
    public String[] getEntityNameList() {
        return EntityList.stringToClassMapping.keySet().toArray(new String[0]);
    }

    /**
     * Creates an instance of an IEntity that can be spawned in the world with IWorld.spawnEntityInWorld(entity)
     * @param entityName To see all valid names, call {@link #getEntityNameList()}
     * @return An IEntity or null if entityName is invalid
     */
    public IEntity createIEntity(String entityName, IWorld world) {
        Entity entity = EntityList.createEntityByName(entityName, world.getMCWorld());
        return AbstractNpcAPI.Instance().getIEntity(entity);
    }

    public ICustomProjectile createCustomProjectile(String texture, IEntityLivingBase shooter) {
        EntityCustomProjectile projectile = new EntityCustomProjectile(shooter.getWorld().getMCWorld(), shooter.getMCEntity());
        projectile.getRenderProperties().setTexture(texture);
        return projectile;
    }

    public ICustomProjectile createCustomProjectile(IEntityLivingBase shooter) {
        return new EntityCustomProjectile(shooter.getWorld().getMCWorld(), shooter.getMCEntity());
    }

    /**
     * Converts a hex code to an integer color that can be used for ScriptedItem's setColor method.
     * @param hex A length six hex code (#'s are removed automatically)
     * @return An integer color or -1 if wrong length and -2 if string is not a hex code.
     */
    public int hexToNpcColor(String hex) {
        hex = hex.replace("#", "");
        if (hex.length() != 6) {
            return -1;
        }

        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    public String npcColorToHex(int npcColor) {
        int maxHexValue = 0xFFFFFF; // 16777215
        if (npcColor > maxHexValue) {
            npcColor = npcColor % (maxHexValue + 1);
        }

        String hex = Integer.toHexString(npcColor);

        while (hex.length() < 6) {
            hex = "0" + hex;
        }

        return hex.toUpperCase();
    }

    public String[] getAllServerPlayerNames() {
        List<String> names = new ArrayList<>();

        // Get all online players from the server instance
        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (obj instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) obj;
                names.add(player.getDisplayName());
            }
        }

        return names.toArray(new String[0]);
    }

    /**
     * Registers a custom attribute that can both be applied via script or /kam attribute. Attributes are registered per world.
     * @param key This, by convention, is always lowercase and words are separated by _'s. This name is used within your scripts to denote your attribute.
     * @param displayName This name will show up on your item.
     * @param colorCode Takes one of Minecraft's 16 colors 0-9 and a-f. See {@link ColorCodes}.
     * @param attributeType 0. Flat, 1. Percent, 2. Magic. {@link ExtendedAttributeValueType}
     * @param section 0. Base, 1. Modifier, 2. Stats, 3. Info, 4. Extra. The higher the number, the further down the section is. See {@link ExtendedAttributeSection}
     */
    public void registerAttribute(String key, String displayName, char colorCode, int attributeType, int section) {
        AttributeValueType valueType = ExtendedAttributeValueType.toKamAttribute(attributeType);
        AttributeDefinition.AttributeSection sectionType = ExtendedAttributeSection.toKamSection(section);

        AttributeDefinition newAttribute = AttributeController.registerAttribute(key, displayName, colorCode, valueType, sectionType);
        ExtendedScripts.getExtendedWorldData().addExtendedAttribute(newAttribute);
        lastWorldsAttributes.put(key, newAttribute);
    }

    public static void registerAttribute(AttributeDefinition newAttribute) {
        if (AttributeController.getAttribute(newAttribute.getKey()) == null) {
            AttributeController.registerAttribute(newAttribute);
            ExtendedScripts.getExtendedWorldData().addExtendedAttribute(newAttribute);
            lastWorldsAttributes.put(newAttribute.getKey(), newAttribute);
        }
    }

    public static float getAttribute(EntityPlayer player, EntityAttribute key) {
        IPlayer npcPlayer = AbstractNpcAPI.Instance().getPlayer(player.getDisplayName());
        return npcPlayer.getAttributes().getAttributeValue(key.asSnakeCase());
    }

    public static float getAttribute(EntityPlayer player, PlayerAttribute key) {
        IPlayer npcPlayer = AbstractNpcAPI.Instance().getPlayer(player.getDisplayName());
        return npcPlayer.getAttributes().getAttributeValue(key.asSnakeCase());
    }

    /**
     * Unregisters a custom attribute. Unregistering an attribute also removes it from all items.
     */
    public boolean unregisterAttribute(String key) {
        boolean result = false;
        if (lastWorldsAttributes.containsKey(key)) {
            AttributeDefinition removedAttribute = lastWorldsAttributes.get(key);

            result = ((IExtendedAttributeController) AttributeController.Instance).unregisterAttribute(key);
            ExtendedScripts.getExtendedWorldData().removeExtendedAttribute(removedAttribute);
            lastWorldsAttributes.remove(key);
        }
        return result;
    }

    public boolean attributeExists(String key) {
        return Arrays.asList(getAttributeKeyList()).contains(key);
    }

    /**
     * This is just for unloading registered attributes between worlds, not for removing it entirely.
     */
    public static void unregisterAttributeFromWorld(String key) {
        ((IExtendedAttributeController) AttributeController.Instance).unregisterAttribute(key);
    }


    public String[] getAttributeKeyList() {
        AttributeDefinition[] registeredAttributes = AttributeController.getAllAttributes().toArray(new AttributeDefinition[0]);
        String[] keys = new String[registeredAttributes.length];
        for (int i = 0; i < registeredAttributes.length; i++) {
            keys[i] = registeredAttributes[i].getKey();
        }
        return keys;
    }

    public String[] getCustomAttributeKeyList() {
        return lastWorldsAttributes.keySet().toArray(new String[0]);
    }

    public IAttributeDefinition getAttributeDefinition(String key) {
        return AttributeController.getAttribute(key);
    }

    public IPotionEffect getIPotionEffect(int id, int duration, int amplifier) throws Exception {
        return new PotionEffect(id, duration, amplifier);
    }

    public IPotionEffect getIPotionEffect(int id) throws Exception {
        return new PotionEffect(id, 30 * 20, 0);
    }
}
