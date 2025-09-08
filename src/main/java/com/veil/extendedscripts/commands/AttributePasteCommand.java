package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;

import java.util.*;

public class AttributePasteCommand implements IVeilSubCommand {
    private static List<String> settings = new ArrayList<>();

    static {
        settings.add("overwrite-all");
        settings.add("overwrite-existing");
        settings.add("add-non-existing");
    }

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

        if (args.length == 0) {
            IItemStack heldItem = NpcAPI.Instance().getIItemStack(mcItem);
            ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(playerSender);
            Map<String, Float> savedAttributes = properties.attributeClipboard;

            if (savedAttributes == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"No saved attributes could be found."));
                return;
            }

            for (String key : savedAttributes.keySet()) {
                heldItem.setCustomAttribute(key, savedAttributes.get(key));
            }
        } else {
            IItemStack heldItem = NpcAPI.Instance().getIItemStack(mcItem);
            ExtendedScriptPlayerProperties properties = ExtendedScripts.getPlayerProperties(playerSender);
            Map<String, Float> savedAttributes = properties.attributeClipboard;

            if (savedAttributes == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"No saved attributes could be found."));
                return;
            }

            List<String> existingKeys = Arrays.asList(heldItem.getCustomAttributeKeys());
            if (args[0].equals(settings.get(0))) { // Overwrite all
                heldItem.getNbt().remove("RPGCore");

                for (String key : savedAttributes.keySet()) {
                    heldItem.setCustomAttribute(key, savedAttributes.get(key));
                }
            } else if (args[0].equals(settings.get(1))) { // Overwrite existing
                for (String key : savedAttributes.keySet()) {
                    if (existingKeys.contains(key)) {
                        heldItem.setCustomAttribute(key, savedAttributes.get(key));
                    }
                }
            } else if (args[0].equals(settings.get(2))) { // Add non-existing
                for (String key : savedAttributes.keySet()) {
                    if (!existingKeys.contains(key)) {
                        heldItem.setCustomAttribute(key, savedAttributes.get(key));
                    }
                }
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid setting."));
                return;
            }

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Attributes applied to item."));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return settings.toArray(new String[]{});
    }
}
