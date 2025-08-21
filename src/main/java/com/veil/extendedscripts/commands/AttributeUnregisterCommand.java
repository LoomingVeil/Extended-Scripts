package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class AttributeUnregisterCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute unregister <key>"));
            return;
        }

        String key = args[0];
        boolean result = ExtendedAPI.Instance.unregisterAttribute(key);
        if (result) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully unregistered "+key+"."));
        } else {
            result = Arrays.asList(ExtendedAPI.Instance.getAttributeKeyList()).contains(key);

            if (result) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"You can only unregister custom attributes!"));
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find attribute "+key+"."));
                sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Use /kam attribute list <page> for a full list of attributes"));
            }
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return ExtendedAPI.Instance.getCustomAttributeKeyList();
        }

        return null;
    }
}
