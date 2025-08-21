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

public class AttributeResetPlayerCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            if (playerSender == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"This command can only be used by a player."));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(playerSender);
            props.resetCoreAttributes();

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully reset attributes for "+playerSender.getDisplayName()));
        } else {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
            props.resetCoreAttributes();

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully reset attributes for "+player.getDisplayName()));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
