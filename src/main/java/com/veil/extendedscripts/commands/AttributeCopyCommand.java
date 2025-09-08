package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;

import java.util.HashMap;
import java.util.Map;

public class AttributeCopyCommand implements IVeilSubCommand {

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

        IItemStack heldItem = NpcAPI.Instance().getIItemStack(mcItem);
        String[] keys = heldItem.getCustomAttributeKeys();
        Map<String, Float> savedAttributes = new HashMap<>();

        for (String key : keys) {
            savedAttributes.put(key, heldItem.getCustomAttribute(key));
        }

        ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(playerSender);
        properties.attributeClipboard = savedAttributes;

        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Attributes copied."));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
