package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.ColorCodes;
import com.veil.extendedscripts.constants.ExtendedAttributeSection;
import com.veil.extendedscripts.constants.ExtendedAttributeValueType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class ExtendedScriptsUtilitiesCommand extends CommandBase {
    private final static String gray = "§"+ColorCodes.Instance.GRAY;
    private final static String dark_gray = "§"+ColorCodes.Instance.DARK_GRAY;
    private final static String red = "§"+ColorCodes.Instance.RED;
    private final static String yellow = "§"+ColorCodes.Instance.YELLOW;
    private final static String dark_red = "§"+ColorCodes.Instance.DARK_RED;
    private final static String modPrefix = "§"+ColorCodes.Instance.GOLD+"["+yellow+"ExtendedScripts"+"§"+ColorCodes.Instance.GOLD+"] ";
    private static final List<String> PRIMARY_ARGS = new ArrayList<>();
    private static final List<String> SECONDARY_ARGS = new ArrayList<>();
    private static final List<String> QUINARY_ARGS = new ArrayList<>();
    private static final List<String> SENARY_ARGS = new ArrayList<>();
    private static final List<String> SEPTENARY_ARGS = new ArrayList<>();

    static {
        PRIMARY_ARGS.add("attribute");

        SECONDARY_ARGS.add("add");
        SECONDARY_ARGS.add("remove");

        QUINARY_ARGS.add("black");
        QUINARY_ARGS.add("dark_blue");
        QUINARY_ARGS.add("dark_green");
        QUINARY_ARGS.add("dark_aqua");
        QUINARY_ARGS.add("dark_red");
        QUINARY_ARGS.add("dark_purple");
        QUINARY_ARGS.add("gold");
        QUINARY_ARGS.add("gray");
        QUINARY_ARGS.add("dark_gray");
        QUINARY_ARGS.add("blue");
        QUINARY_ARGS.add("green");
        QUINARY_ARGS.add("aqua");
        QUINARY_ARGS.add("red");
        QUINARY_ARGS.add("light_purple");
        QUINARY_ARGS.add("yellow");
        QUINARY_ARGS.add("white");

        SENARY_ARGS.add("FLAT");
        SENARY_ARGS.add("PERCENT");
        SENARY_ARGS.add("MAGIC");

        SEPTENARY_ARGS.add("Base");
        SEPTENARY_ARGS.add("Modifier");
        SEPTENARY_ARGS.add("Stats");
        SEPTENARY_ARGS.add("Info");
        SEPTENARY_ARGS.add("Extra");
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
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"attribute"+dark_gray+": "+gray+"Add and remove custom attributes"));
        } else if (args[0].equals("attribute")) {
            if (args.length == 1) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+EnumChatFormatting.GREEN+" ATTRIBUTE SubCommands "+dark_gray+"-------"));
                sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"add"+dark_gray+": "+gray+"Register a custom attribute for your world. For display name, _'s are replaced with spaces."));
                sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"remove"+dark_gray+": "+gray+"Remove a custom attribute from your world."));
            } else if (args.length == 2) {
                if (args[1].equals("add")) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute add <key> <displayName> <color> <valueType> <section>"));
                } else if (args[1].equals("remove")) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /veil attribute remove <key>"));
                } else {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Unknown subcommand "+args[1]));
                }
            } else {
                if (args[1].equals("add")) {
                    if (args.length >= 7) {
                        String[] existingKeys = ExtendedAPI.Instance.getAttributeKeyList();
                        String key = args[2];
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

                        String displayName = args[3].replace("_", " ");

                        char color = ColorCodes.Instance.getValue(args[4]);
                        if (color == 'x') {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Color "+args[4]+" doesn't exist. Use tab completion for valid colors."));
                            return;
                        }

                        int valueType = ExtendedAttributeValueType.Instance.getValue(args[5]);
                        if (valueType == -1) {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Type "+args[5]+" doesn't exist. Use tab completion for valid value types."));
                            return;
                        }

                        int section = ExtendedAttributeSection.Instance.getValue(args[6]);
                        if (section == -1) {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Section "+args[6]+" doesn't exist. Use tab completion for valid sections."));
                            return;
                        }

                        ExtendedAPI.Instance.registerAttribute(key, displayName, color, valueType, section);
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+EnumChatFormatting.GREEN+"Successfully registered "+displayName+"."));
                    } else {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Missing "+(7 - args.length)+" arguments!"));
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Usage: /veil attribute add <key> <displayName> <color> <valueType> <section>"));
                    }
                } else if (args[1].equals("remove")) {
                    // Always greater than or equal to 3 arguments
                    String key = args[2];
                    boolean result = ExtendedAPI.Instance.unregisterAttribute(key);
                    if (result) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+EnumChatFormatting.GREEN+"Successfully unregistered "+key+"."));
                    } else {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find attribute "+key+"."));
                        sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Use /kam attribute list <page> for a full list of attributes"));
                    }
                }
            }
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Unknown command "+args[0]));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 0) return null;
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, PRIMARY_ARGS.toArray(new String[0]));
        } else if (args.length == 2 && PRIMARY_ARGS.contains(args[0])) {
            return getListOfStringsMatchingLastWord(args, SECONDARY_ARGS.toArray(new String[0]));
        } else if (args.length == 3 && SECONDARY_ARGS.contains(args[1]) && args[1].equals("remove")) {
            return getListOfStringsMatchingLastWord(args, ExtendedAPI.Instance.getCustomAttributeKeyList());
        } else if (args.length == 5 && args[1].equals("add")) {
            return getListOfStringsMatchingLastWord(args, QUINARY_ARGS.toArray(new String[0]));
        } else if (args.length == 6 && args[1].equals("add")) {
            return getListOfStringsMatchingLastWord(args, SENARY_ARGS.toArray(new String[0]));
        } else if (args.length == 7 && args[1].equals("add")) {
            return getListOfStringsMatchingLastWord(args, SEPTENARY_ARGS.toArray(new String[0]));
        }

        return null;
    }
}
