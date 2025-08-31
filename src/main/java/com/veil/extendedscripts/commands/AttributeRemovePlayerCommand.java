package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class AttributeRemovePlayerCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute remove-player <player> <key>"));
            return;
        }

        if (args.length == 1) {
            if (playerSender == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"You must specify a player!"));
                return;
            }

            String key = args[0];
            if (!ExtendedAPI.Instance.attributeExists(key)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Attribute does not exist!"));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(playerSender);
            props.removeCoreAttribute(key);

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully removed attribute "+key+" from "+playerSender.getDisplayName()+"."));
        } else {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            String key = args[1];
            if (!ExtendedAPI.Instance.attributeExists(key)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Attribute does not exist!"));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
            props.removeCoreAttribute(key);

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully removed attribute "+key+" from "+player.getDisplayName()+"."));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] playerNames = ExtendedAPI.Instance.getAllServerPlayerNames();
        if (args.length == 1) { // Name or key
            return playerNames;
        } else if (args.length == 2) {
            if (Arrays.asList(playerNames).contains(args[0])) { // Key
                return ExtendedAPI.Instance.getAttributeKeyList();
            } // Otherwise values which can not be TAB complete
        }

        return null;
    }
}
