package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.constants.ColorCodes;
import com.veil.extendedscripts.constants.ExtendedAttributeSection;
import com.veil.extendedscripts.constants.ExtendedAttributeValueType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class AttributeRegisterCommand implements IVeilSubCommand {
    List<String> colors = new ArrayList<>();
    List<String> valueTypes = new ArrayList<>();
    List<String> sections = new ArrayList<>();
    public AttributeRegisterCommand() {
        colors.add("black");
        colors.add("dark_blue");
        colors.add("dark_green");
        colors.add("dark_aqua");
        colors.add("dark_red");
        colors.add("dark_purple");
        colors.add("gold");
        colors.add("gray");
        colors.add("dark_gray");
        colors.add("blue");
        colors.add("green");
        colors.add("aqua");
        colors.add("red");
        colors.add("light_purple");
        colors.add("yellow");
        colors.add("white");

        valueTypes.add("FLAT");
        valueTypes.add("PERCENT");
        valueTypes.add("MAGIC");

        sections.add("Base");
        sections.add("Modifier");
        sections.add("Stats");
        sections.add("Info");
        sections.add("Extra");
    }

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Usage: /veil attribute register <key> <displayName> <color> <valueType> <section>"));
            return;
        }

        if (args.length >= 5) {
            String[] existingKeys = ExtendedAPI.Instance.getAttributeKeyList();
            String key = args[0];
            if (!key.equals(key.toLowerCase())) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid formatting. Key should be all lowercase"));
                return;
            }

            for (int i = 0; i < existingKeys.length; i++) {
                if (existingKeys[i].equals(key)) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Attribute "+key+" already exists."));
                    return;
                }
            }

            String displayName = args[1].replace("_", " ");

            char color = ColorCodes.Instance.getValue(args[2]);
            if (color == 'x') {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Color "+args[2]+" doesn't exist. Use tab completion for valid colors."));
                return;
            }

            int valueType = ExtendedAttributeValueType.Instance.getValue(args[3]);
            if (valueType == -1) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Type "+args[3]+" doesn't exist. Use tab completion for valid value types."));
                return;
            }

            int section = ExtendedAttributeSection.Instance.getValue(args[4]);
            if (section == -1) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Section "+args[4]+" doesn't exist. Use tab completion for valid sections."));
                return;
            }

            ExtendedAPI.Instance.registerAttribute(key, displayName, color, valueType, section);
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+ EnumChatFormatting.GREEN+"Successfully registered "+displayName+"."));
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Missing "+(5 - args.length)+" arguments!"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Usage: /veil attribute register <key> <displayName> <color> <valueType> <section>"));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 3) {
            return colors.toArray(new String[0]);
        } else if (args.length == 4) {
            return valueTypes.toArray(new String[0]);
        } else if (args.length == 5) {
            return sections.toArray(new String[0]);
        }

        return null;
    }
}
