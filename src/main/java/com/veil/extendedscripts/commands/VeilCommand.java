package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.constants.ColorCodes;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

public class VeilCommand extends CommandBase {
    private final static String gray = "§"+ColorCodes.Instance.GRAY;
    private final static String dark_gray = "§"+ColorCodes.Instance.DARK_GRAY;
    private final static String red = "§"+ColorCodes.Instance.RED;
    private final static String yellow = "§"+ColorCodes.Instance.YELLOW;
    private final static String dark_red = "§"+ColorCodes.Instance.DARK_RED;
    private final static String modPrefix = "§"+ColorCodes.Instance.GOLD+"["+yellow+"ExtendedScripts"+"§"+ColorCodes.Instance.GOLD+"] ";
    private final Map<String, IVeilSubCommand> subCommands = new HashMap<>();

    public VeilCommand() {
        subCommands.put("attribute", new AttributeCommand());
        subCommands.put("clear-chat", new ClearChatCommand());
    }

    @Override
    public String getCommandName() {
        return "veil";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/veil";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+dark_red+" Veil Commands "+dark_gray+"-------"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"attribute"+dark_gray+": "+gray+"Add, remove, and manage attributes on players."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"clear-chat"+dark_gray+": "+gray+"Clear your chat of any messes you've made."));
            return;
        }

        IVeilSubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Unknown subcommand "+args[1]));
            return;
        }
        EntityPlayer player = null;
        if (sender instanceof EntityPlayer) {
            player = getPlayer(sender, sender.getCommandSenderName());
        }

        subCommand.execute(sender, player, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            return null;
        } else if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, subCommands.keySet().toArray(new String[0]));
        } else {
            IVeilSubCommand subCommand = subCommands.get(args[0]);
            if (subCommand != null) {
                String[] possibilities = subCommand.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length));
                if (possibilities == null) return null;
                return getListOfStringsMatchingLastWord(args, possibilities);
            }
        }

        return null;
    }
}
