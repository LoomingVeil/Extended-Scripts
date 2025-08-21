package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.constants.ColorCodes;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IVeilSubCommand {
    Map<String, IVeilSubCommand> subCommands = new HashMap<>();
    String gray = "§"+ ColorCodes.Instance.GRAY;
    String dark_gray = "§"+ColorCodes.Instance.DARK_GRAY;
    String red = "§"+ColorCodes.Instance.RED;
    String yellow = "§"+ColorCodes.Instance.YELLOW;
    String dark_red = "§"+ColorCodes.Instance.DARK_RED;
    String modPrefix = "§"+ColorCodes.Instance.GOLD+"["+yellow+"ExtendedScripts"+"§"+ColorCodes.Instance.GOLD+"] ";
    void execute(ICommandSender sender, EntityPlayer playerSender, String[] args);
    String[] addTabCompletionOptions(ICommandSender sender, String[] args);
}
