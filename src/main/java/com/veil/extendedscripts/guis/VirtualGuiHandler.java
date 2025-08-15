package com.veil.extendedscripts.guis;

import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.ExtendedScripts;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.world.World;
import net.minecraft.client.gui.inventory.GuiFurnace; // For client-side furnace GUI


public class VirtualGuiHandler implements IGuiHandler {
    // Gets the server-side Container for the GUI
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // ID corresponds to the GUI_ID you pass in openGui()
        if (ID == ExtendedScripts.GUI_VIRTUAL_CRAFTING_TABLE) {
            return new VirtualCraftingTableContainer(player.inventory, world);
        } else if (ID == ExtendedScripts.GUI_VIRTUAL_FURNACE) {
            ExtendedScriptPlayerProperties properties = ExtendedScriptPlayerProperties.get(player);
            VirtualFurnace furnace = properties.getVirtualFurnace();
            furnace.setPersistence(false);

            properties.resetVirtualFurnace();

            return new VirtualFurnaceContainer(player.inventory, properties.getVirtualFurnace());
        } else if (ID == ExtendedScripts.GUI_PERSISTENT_VIRTUAL_FURNACE) {
            ExtendedScriptPlayerProperties properties = ExtendedScriptPlayerProperties.get(player);
            VirtualFurnace furnace = properties.getVirtualFurnace();
            furnace.setPersistence(true);

            return new VirtualFurnaceContainer(player.inventory, properties.getVirtualFurnace());
        } else if (ID == ExtendedScripts.GUI_VIRTUAL_ANVIL) {
            return new ContainerRepair(player.inventory, world, x, y, z, player);
        }
        return null;
    }

    // Gets the client-side GuiScreen for the GUI
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // ID corresponds to the GUI_ID you pass in openGui()
        if (ID == ExtendedScripts.GUI_VIRTUAL_CRAFTING_TABLE) {
            return new VirtualCraftingTableGui(player.inventory, world);
        } else if (ID == ExtendedScripts.GUI_VIRTUAL_FURNACE || ID == ExtendedScripts.GUI_PERSISTENT_VIRTUAL_FURNACE) {
            return new GuiFurnace(player.inventory, new VirtualFurnace());
        } else if (ID == ExtendedScripts.GUI_VIRTUAL_ANVIL) {
            return new GuiRepair(player.inventory, world, x, y, z);
        }

        return null;
    }
}
