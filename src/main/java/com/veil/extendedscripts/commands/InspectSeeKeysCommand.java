package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class InspectSeeKeysCommand implements IVeilSubCommand {
    private static final String BYTE_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String SHORT_COLOR = EnumChatFormatting.YELLOW.toString();
    private static final String INTEGER_COLOR = EnumChatFormatting.AQUA.toString();
    private static final String LONG_COLOR = EnumChatFormatting.DARK_BLUE.toString();
    private static final String FLOAT_COLOR = EnumChatFormatting.GREEN.toString();
    private static final String DOUBLE_COLOR = EnumChatFormatting.BLUE.toString();
    private static final String STRING_COLOR = EnumChatFormatting.DARK_GREEN.toString();

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        sender.addChatMessage(ChatUtils.fillChatWithColor(BYTE_COLOR + "Byte Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(SHORT_COLOR + "Short Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(INTEGER_COLOR + "Integer Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(LONG_COLOR + "Long Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(FLOAT_COLOR + "Float Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(DOUBLE_COLOR + "Double Color"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(STRING_COLOR + "String Color"));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
