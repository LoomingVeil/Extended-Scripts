package com.veil.extendedscripts.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public class InspectHandCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        InspectCommand.inspectItem(playerSender.getHeldItem(), sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
