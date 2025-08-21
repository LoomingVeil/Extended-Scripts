package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;

import java.util.List;

public class AttributeResetCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (playerSender == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"This command can only be used by a player."));
            return;
        }

        ItemStack mcItem = playerSender.getHeldItem();
        if (mcItem == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"You are not holding an item."));
            return;
        }
        IPlayer player = AbstractNpcAPI.Instance().getPlayer(playerSender.getDisplayName());
        IItemStack heldItem = player.getHeldItem();

        heldItem.getNbt().remove("RPGCore");
        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully removed all attributes."));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
