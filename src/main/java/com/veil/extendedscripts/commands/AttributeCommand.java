package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.constants.ColorCodes;
import com.veil.extendedscripts.constants.ExtendedAttributeSection;
import com.veil.extendedscripts.constants.ExtendedAttributeValueType;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeCommand implements IVeilSubCommand {
    private final static String gray = "§"+ColorCodes.Instance.GRAY;
    private final static String dark_gray = "§"+ColorCodes.Instance.DARK_GRAY;
    private final static String red = "§"+ColorCodes.Instance.RED;
    private final static String yellow = "§"+ColorCodes.Instance.YELLOW;
    private final static String dark_red = "§"+ColorCodes.Instance.DARK_RED;
    private final static String modPrefix = "§"+ColorCodes.Instance.GOLD+"["+yellow+"ExtendedScripts"+"§"+ColorCodes.Instance.GOLD+"] ";

    public AttributeCommand() {
        subCommands.put("register", new AttributeRegisterCommand());
        subCommands.put("unregister", new AttributeUnregisterCommand());
        subCommands.put("reset", new AttributeResetCommand());
        subCommands.put("apply-player", new AttributeApplyPlayerCommand());
        subCommands.put("remove-player", new AttributeRemovePlayerCommand());
        subCommands.put("reset-player", new AttributeResetPlayerCommand());
    }

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+ EnumChatFormatting.GREEN+" ATTRIBUTE SubCommands "+dark_gray+"-------"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"register"+dark_gray+": "+gray+"Register a custom attribute for your world. For display name, _'s are replaced with spaces."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"unregister"+dark_gray+": "+gray+"Remove a custom attribute from your world."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"reset"+dark_gray+": "+gray+"Removes all attributes on an item."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"apply-player"+dark_gray+": "+gray+"Gives attributes to the player. These attributes are always active until removed."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"remove-player"+dark_gray+": "+gray+"Removes an attribute from the player. These attributes are always active until removed."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"reset-player"+dark_gray+": "+gray+"Removes all attributes from the player. These attributes are always active until removed."));
            return;
        }

        IVeilSubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Unknown subcommand "+args[1]));
            return;
        }

        subCommand.execute(sender, playerSender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().toArray(new String[0]);
        } else {
            IVeilSubCommand subCommand = subCommands.get(args[0]);
            if (subCommand != null) {
                return subCommand.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return null;
    }
}
