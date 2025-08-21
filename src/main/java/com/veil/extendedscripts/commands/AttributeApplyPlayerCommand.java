package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.AbstractNpcAPI;

import java.util.Arrays;
import java.util.List;

public class AttributeApplyPlayerCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute apply-player <player> <key> <value>"));
            return;
        }

        if (args.length == 2) {
            String key = args[0];
            if (!ExtendedAPI.Instance.attributeExists(key)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Attribute does not exist!"));
                return;
            }

            float value;
            try {
                value = Float.parseFloat(args[1]);
            } catch (Exception e) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid attribute value!"));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(playerSender);
            props.setCoreAttribute(key, value);

            String sign = "";
            if (value > 0) {
                sign = "+";
            }
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully given "+playerSender.getDisplayName()+" "+sign+value+" "+key+"."));
        } else if (args.length >= 3) {
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

            float value;
            try {
                value = Float.parseFloat(args[2]);
            } catch (Exception e) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid attribute value!"));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
            props.setCoreAttribute(key, value);

            String sign = "";
            if (value > 0) {
                sign = "+";
            }
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully given "+player.getDisplayName()+" "+sign+value+" "+key+"."));
        } else {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Missing "+(2 - args.length)+" arguments!"));
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Missing "+(3 - args.length)+" arguments!"));
            }
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute apply-player <player> <key> <value>"));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] playerNames = ExtendedAPI.Instance.getAllServerPlayerNames();
        if (args.length == 1) { // Name or key
            return playerNames;
        } else if (args.length == 2) {
            if (Arrays.asList(playerNames).contains(args[0])) { // Key or value
                return ExtendedAPI.Instance.getAttributeKeyList();
            } // Otherwise values which can not be TAB complete
        }

        return null;
    }
}
