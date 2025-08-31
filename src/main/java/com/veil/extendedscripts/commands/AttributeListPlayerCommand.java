package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import kamkeel.npcs.controllers.data.attribute.AttributeValueType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.ICustomAttribute;

public class AttributeListPlayerCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            if (playerSender == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"You must specify a player!"));
                return;
            }

            showAttributes(sender, playerSender, 1);
        } else if (args.length == 1) {
            int pageNum = -1;
            try {
                pageNum = Integer.valueOf(args[0]);
                if (pageNum <= 0) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Page number can not be less than 1!"));
                    return;
                }
            } catch (NumberFormatException e) { }

            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null && pageNum == -1) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            if (pageNum == -1) {
                showAttributes(sender, player, 1);
            } else if (player == null) {
                showAttributes(sender, playerSender, pageNum);
            }
        } else if (args.length == 2) {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            int pageNum;
            try {
                pageNum = Integer.valueOf(args[1]);
                if (pageNum <= 0) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Page number can not be less than 1!"));
                    return;
                }
            } catch (NumberFormatException e) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid page number."));
                return;
            }

            showAttributes(sender, player, pageNum);
        }
    }

    private void showAttributes(ICommandSender sender, EntityPlayer playerToView, int page) {
        IPlayer player = AbstractNpcAPI.Instance().getPlayer(playerToView.getDisplayName());
        ICustomAttribute[] attrs = player.getAttributes().getAttributes();

        int numPages = (int) Math.ceil(attrs.length / 10.0);

        if (page > numPages) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"There are only "+numPages+" pages."));
            return;
        }

        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+EnumChatFormatting.GREEN+"---------------------"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+EnumChatFormatting.GREEN+"Attributes (Page "+page+"/"+numPages+")"));
        for (int i = (page - 1) * 10; i < Math.min(attrs.length, page * 10); i++) {
            AttributeDefinition attr = (AttributeDefinition) attrs[i].getAttribute();
            float numValue =  attrs[i].getValue();
            String strValue = "";

            if (numValue > 0) strValue += "+";
            strValue += numValue;

            if (attr.getValueType() == AttributeValueType.PERCENT) {
                strValue += "%";
            }

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+EnumChatFormatting.GREEN+" "+(i+1)+". "+"§"+attr.getColorCode()+attr.getDisplayName()+EnumChatFormatting.GREEN+" "+strValue));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] playerNames = ExtendedAPI.Instance.getAllServerPlayerNames();
        if (args.length == 1) {
            return playerNames;
        }

        return null;
    }
}
