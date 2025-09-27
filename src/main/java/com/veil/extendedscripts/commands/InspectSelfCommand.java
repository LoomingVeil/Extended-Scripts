package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.AbstractNpcAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InspectSelfCommand implements IVeilSubCommand {

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        NBTTagCompound nbt = new NBTTagCompound();
        playerSender.writeToNBT(nbt);

        args = Arrays.copyOfRange(args, 1, args.length);

        List<String> newArgs = new ArrayList<>();
        boolean skipNext = false;
        boolean onlyShowKeys = false;
        int page = 1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-k")) onlyShowKeys = true;
            else if (args[i].equals("-p")) {
                if (i + 1 < args.length) {
                    try {
                        page = Integer.parseInt(args[i + 1]);
                        if (page <= 0) throw new NumberFormatException();
                        skipNext = true;
                    } catch (Exception e) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Page number must be an integer"));
                        return;
                    }
                } else {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Page number must be specified"));
                    return;
                }
            } else if (!skipNext) {
                newArgs.add(args[i]);
            } else {
                skipNext = false;
            }
        }

        InspectCommand.handleShowingNbt(AbstractNpcAPI.Instance().getINbt(nbt), sender, newArgs.toArray(new String[0]), playerSender.getDisplayName(), onlyShowKeys, page);
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
