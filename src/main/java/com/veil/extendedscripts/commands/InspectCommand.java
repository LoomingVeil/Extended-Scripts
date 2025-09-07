package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.item.WorldClippers;
import com.veil.extendedscripts.constants.ColorCodes;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.INbt;

import java.util.*;

public class InspectCommand extends CommandBase {
    private static final String UNKNOWN_COLOR = EnumChatFormatting.RED.toString();
    private static final String BYTE_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String SHORT_COLOR = EnumChatFormatting.YELLOW.toString();
    private static final String INTEGER_COLOR = EnumChatFormatting.AQUA.toString();
    private static final String LONG_COLOR = EnumChatFormatting.DARK_BLUE.toString();
    private static final String FLOAT_COLOR = EnumChatFormatting.GREEN.toString();
    private static final String DOUBLE_COLOR = EnumChatFormatting.BLUE.toString();
    private static final String STRING_COLOR = EnumChatFormatting.DARK_GREEN.toString();
    private final static String gray = "§"+ ColorCodes.Instance.GRAY;
    private final static String dark_gray = "§"+ColorCodes.Instance.DARK_GRAY;
    private final static String red = "§"+ColorCodes.Instance.RED;
    private final static String yellow = "§"+ColorCodes.Instance.YELLOW;
    private final static String dark_red = "§"+ColorCodes.Instance.DARK_RED;
    private final static String white = "§"+ColorCodes.Instance.WHITE;
    private final static String modPrefix = "§"+ColorCodes.Instance.GOLD+"["+yellow+"ExtendedScripts"+"§"+ColorCodes.Instance.GOLD+"] ";
    private final static int maxStringLength = 100;
    private final static Map<Integer, String> colors = new HashMap<>();

    private static final List<String> SECONDARY_ARGS = new ArrayList<>();

    static {
        SECONDARY_ARGS.add("hand");
        SECONDARY_ARGS.add("target");
        SECONDARY_ARGS.add("self");
        SECONDARY_ARGS.add("keys");
        SECONDARY_ARGS.add("tp");
        SECONDARY_ARGS.add("tphere");

        colors.put(1, BYTE_COLOR);
        colors.put(2, SHORT_COLOR);
        colors.put(3, INTEGER_COLOR);
        colors.put(4, LONG_COLOR);
        colors.put(5, FLOAT_COLOR);
        colors.put(6, DOUBLE_COLOR);
        colors.put(8, STRING_COLOR);
    }

    @Override
    public String getCommandName() {
        return "inspect";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/inspect hand or /inspect target ";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(EnumChatFormatting.RED + "This command can only be used by a player."));
            return;
        }

        EntityPlayer player = getPlayer(sender, sender.getCommandSenderName());
        ItemStack heldItem = player.getHeldItem();

        if (player.worldObj.isRemote) {
            return;
        }

        if (args.length == 0) {
            showCommandUsage(sender);
            return;
        }

        if (heldItem == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "You must be holding an item to use this command!"));
            return;
        }

        if (args[0].equalsIgnoreCase("hand")) {
            inspectItem(player.getHeldItem(), sender, Arrays.copyOfRange(args, 1, args.length));
        }  else if (args[0].equalsIgnoreCase("target")) {
            if (heldItem.getItem() != ExtendedScripts.worldClippers) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
                return;
            }

            if (!WorldClippers.hasInspectTag(heldItem)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "No target selected! Right click a block or entity."));
                return;
            }
            NBTTagCompound inspectTag = WorldClippers.getInspectTag(heldItem);

            WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(inspectTag.getInteger("TargetDimension"));
            if (targetWorld == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Could not find world! Perhaps it is unloaded."));
                return;
            }

            if (inspectTag.getByte("TargetType") == 1) { // Block
                inspectBlock(inspectTag, targetWorld, sender, Arrays.copyOfRange(args, 2, args.length));
            } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                inspectEntity(inspectTag, targetWorld, sender, Arrays.copyOfRange(args, 1, args.length));
            }
        } else if (args[0].equalsIgnoreCase("keys")) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(BYTE_COLOR + "Byte Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(SHORT_COLOR + "Short Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(INTEGER_COLOR + "Integer Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(LONG_COLOR + "Long Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(FLOAT_COLOR + "Float Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(DOUBLE_COLOR + "Double Color"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(STRING_COLOR + "String Color"));
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (heldItem.getItem() != ExtendedScripts.worldClippers) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
                return;
            }

            if (args.length == 1 || args.length == 4 || args.length == 5) {
                if (!WorldClippers.hasInspectTag(heldItem)) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "No target selected! Right click a block or entity."));
                    return;
                }
                NBTTagCompound inspectTag = WorldClippers.getInspectTag(heldItem);

                WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(inspectTag.getInteger("TargetDimension"));
                if (targetWorld == null) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Could not find world! Perhaps it is unloaded."));
                    return;
                }
                if (args.length == 1) {
                    if (inspectTag.getByte("TargetType") == 1) { // Block
                        int x = inspectTag.getInteger("TargetX");
                        int y = inspectTag.getInteger("TargetY");
                        int z = inspectTag.getInteger("TargetZ");
                        Block block = targetWorld.getBlock(x, y, z);

                        if (block == null) {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Block could not be found!"));
                            return;
                        }

                        player.setPositionAndUpdate(x + 0.5, y, z + 0.5);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported to target!"));
                    } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                        Entity foundEntity = getEntityFromInspectTag(inspectTag, targetWorld, sender);
                        player.setPositionAndUpdate(foundEntity.posX, foundEntity.posY, foundEntity.posZ);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported to target!"));
                    }
                }
                if (args.length == 4) {
                    if (inspectTag.getByte("TargetType") == 1) { // Block
                        int x = inspectTag.getInteger("TargetX");
                        int y = inspectTag.getInteger("TargetY");
                        int z = inspectTag.getInteger("TargetZ");
                        Block block = targetWorld.getBlock(x, y, z);

                        if (block == null) {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Block could not be found!"));
                            return;
                        }

                        double[] dest = getCoordinatesFromArgument(
                            new String[] {args[1], args[2], args[3]},
                            new double[] {x + 0.5, y, z + 0.5},
                            sender
                        );

                        player.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                    } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                        Entity foundEntity = getEntityFromInspectTag(inspectTag, targetWorld, sender);

                        double[] dest = getCoordinatesFromArgument(
                            new String[] {args[1], args[2], args[3]},
                            new double[] {foundEntity.posX, foundEntity.posY, foundEntity.posZ},
                            sender
                        );

                        player.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                    }
                } else if (args.length == 5) {
                    if (heldItem.getItem() != ExtendedScripts.worldClippers) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
                        return;
                    }

                    if (inspectTag.getByte("TargetType") == 1) { // Block
                        int x = inspectTag.getInteger("TargetX");
                        int y = inspectTag.getInteger("TargetY");
                        int z = inspectTag.getInteger("TargetZ");
                        Block block = targetWorld.getBlock(x, y, z);

                        if (block == null) {
                            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Block could not be found!"));
                            return;
                        }

                        double[] dest = getCoordinatesFromArgument(
                            new String[] {args[1], args[2], args[3], args[4]},
                            new double[] {x + 0.5, y, z + 0.5},
                            sender
                        );

                        player.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                        player.travelToDimension((int)dest[3]);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                    } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                        Entity foundEntity = getEntityFromInspectTag(inspectTag, targetWorld, sender);

                        double[] dest = getCoordinatesFromArgument(
                            new String[] {args[1], args[2], args[3], args[4]},
                            new double[] {foundEntity.posX, foundEntity.posY, foundEntity.posZ},
                            sender
                        );

                        player.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                        player.travelToDimension((int)dest[3]);
                        sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                    }
                }
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid number of arguments!"));
                showCommandUsage(sender);
            }
        } else if (args[0].equalsIgnoreCase("self")) {
            player = getPlayer(sender, sender.getCommandSenderName());
            NBTTagCompound nbt = new NBTTagCompound();
            player.writeToNBT(nbt);

            showNbt(sender, AbstractNpcAPI.Instance().getINbt(nbt), "");
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid arguments!"));
            showCommandUsage(sender);
        }
    }

    public double[] getCoordinatesFromArgument(String[] coordinateArgs, double[] origin, ICommandSender sender) {
        double[] ret = new double[coordinateArgs.length];
        String[] argNames = new String[] {
            "x coordinate", "y coordinate", "z coordinate", "dimension id"
        };

        for (int i = 0; i < ret.length; i++) {
            if (i < 3) {
                ret[i] = origin[i];
            }
            if (i == 3) { // When i = 3, it is the dimension. No way relative dimensions is a good idea.
                try {
                    ret[i] = Integer.parseInt(coordinateArgs[i]);
                } catch (NumberFormatException e) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Invalid "+argNames[i]));
                    return null;
                }
            } else if (coordinateArgs[i].contains("~")) {
                if (!coordinateArgs[i].equals("~")) {
                    try {
                        ret[i] += Integer.parseInt(coordinateArgs[i].replace("~", ""));
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Invalid "+argNames[i]));
                        return null;
                    }
                }
            } else {
                try {
                    ret[i] = Integer.parseInt(coordinateArgs[i]);
                } catch (NumberFormatException e) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Invalid "+argNames[i]));
                    return null;
                }
            }
        }
        return ret;
    }

    public Entity getEntityFromInspectTag(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender) {
        String UUIDString = inspectTag.getString("TargetUUID");
        UUID targetUUID;
        try {
            targetUUID = UUID.fromString(UUIDString); // Parse the UUID string
        } catch (IllegalArgumentException e) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid UUID format stored: " + UUIDString));
            return null;
        }

        Entity foundEntity = null;
        for (Object obj : targetWorld.loadedEntityList) {
            if (obj instanceof Entity) {
                Entity entity = (Entity) obj;
                if (entity.getUniqueID().equals(targetUUID)) {
                    foundEntity = entity;
                    break;
                }
            }
        }

        if (foundEntity == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find entity! Entity may have been unloaded or killed."));
            return null;
        }

        return foundEntity;
    }

    public void inspectEntity(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender, String[] args) {
        Entity foundEntity = getEntityFromInspectTag(inspectTag, targetWorld, sender);

        if (foundEntity == null) {
            return;
        }

        NBTTagCompound entityNBT = new NBTTagCompound();
        foundEntity.writeToNBT(entityNBT);
        INbt npcNbt = AbstractNpcAPI.Instance().getINbt(entityNBT);

        if (args.length > 0 && args[0].equals("KEYS")) {
            handleShowingNbt(npcNbt, sender, Arrays.copyOfRange(args, 1, args.length), EntityList.getEntityString(foundEntity), true);
        } else {
            handleShowingNbt(npcNbt, sender, args, EntityList.getEntityString(foundEntity), false);
        }
    }

    public void inspectItem(ItemStack item, ICommandSender sender, String[] args) {
        INbt itemNbt = AbstractNpcAPI.Instance().getIItemStack(item).getNbt();
        String display = GameData.getItemRegistry().getNameForObject(item.getItem());
        if (args.length > 0 && args[0].equals("KEYS")) {
            handleShowingNbt(itemNbt, sender, Arrays.copyOfRange(args, 1, args.length), display, true);
        } else {
            handleShowingNbt(itemNbt, sender, args, display, false);
        }
    }

    public void inspectBlock(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender, String[] args) {
        int x = inspectTag.getInteger("TargetX");
        int y = inspectTag.getInteger("TargetY");
        int z = inspectTag.getInteger("TargetZ");
        Block block = targetWorld.getBlock(x, y, z);

        if (block == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Block could not be found!"));
            return;
        }

        TileEntity tileEntity = targetWorld.getTileEntity(x, y, z);
        if (tileEntity == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Nothing to see here. This block has no NBT."));
            return;
        }

        NBTTagCompound blockNBT = new NBTTagCompound();
        tileEntity.writeToNBT(blockNBT);
        INbt npcNbt = AbstractNpcAPI.Instance().getINbt(blockNBT);

        if (args.length > 0 && args[0].equals("KEYS")) {
            handleShowingNbt(npcNbt, sender, Arrays.copyOfRange(args, 1, args.length), GameData.getItemRegistry().getNameForObject(block.getItem(targetWorld, x, y, z)), true);
        } else {
            handleShowingNbt(npcNbt, sender, args, GameData.getItemRegistry().getNameForObject(block.getItem(targetWorld, x, y, z)), false);
        }
    }

    public void handleShowingNbt(INbt baseNbt, ICommandSender sender, String[] args, String display, boolean onlyShowKeys) {
        if (baseNbt == null || baseNbt.getKeys().length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "This item has no NBT."));
            return;
        }

        // System.out.println("args: " + Arrays.asList(args) + " onlyShowKeys: "+onlyShowKeys);

        // If no arguments are provided, show the entire NBT tree.
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor("NBT for " + display + ":"));
            if (onlyShowKeys) {
                showKeys(sender, baseNbt);
            } else {
                showNbt(sender, baseNbt, "");
            }
            return;
        }

        // Traverse down the tree to the parent of the final tag.
        INbt currentNbt = baseNbt;
        for (int i = 0; i < args.length - 1; i++) {
            String key = args[i];
            if (!currentNbt.has(key) || currentNbt.getType(key) != 10) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(red + "Error: Invalid NBT path. Tag '" + yellow + key + red +"' is not a valid compound tag."));
                return;
            }
            currentNbt = currentNbt.getCompound(key);
        }

        // Get the final tag to display from the last argument.
        String finalKey = args[args.length - 1];
        if (!currentNbt.has(finalKey)) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red + "Error: Invalid NBT path. Final tag '" + yellow + finalKey + red + "' not found."));
            return;
        }

        if (onlyShowKeys) {
            sender.addChatMessage(ChatUtils.fillChatWithColor("NBT keys for " + yellow + String.join(".", args) + white + ":"));
            if (currentNbt.getType(finalKey) == 10) {
                INbt targetNbt = currentNbt.getCompound(finalKey);
                showKeys(sender, targetNbt);
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(red + "Tag '" + yellow + finalKey + red + "' is a value and has no keys to display."));
            }
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor("NBT for " + yellow + String.join(".", args) + white + ":"));
            displayTagValue(sender, currentNbt, finalKey, "");
        }
    }

    public void showKeys(ICommandSender sender, INbt parentNbt) {
        for (String key : parentNbt.getKeys()) {
            int type = parentNbt.getType(key);
            String valueStr = "";

            if (type > 0 && type <= 8) {
                valueStr = colors.getOrDefault(type, UNKNOWN_COLOR) + key;
            } else if (type == 9) {
                int listType = parentNbt.getListType(key);
                Object[] values = parentNbt.getList(key, listType);
                valueStr = key + ": (List of " + values.length + ")";
            } else if (type == 10) {
                valueStr = key;
            } else {
                valueStr = UNKNOWN_COLOR + key;
            }

            sender.addChatMessage(ChatUtils.fillChatWithColor(valueStr));
        }
    }

    public void displayTagValue(ICommandSender sender, INbt parentNbt, String key, String indent) {
        int type = parentNbt.getType(key);

        if (type == 10) { // Compound Tag
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + key + ":"));
            showNbt(sender, parentNbt.getCompound(key), indent + "  ");
        } else if (type == 9) { // List Tag
            int listType = parentNbt.getListType(key);
            Object[] values = parentNbt.getList(key, listType);
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + key + " (List of " + values.length + "):"));
            for (int j = 0; j < values.length; j++) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + "  Index " + j + ":"));
                if (values[j] instanceof INbt) {
                    showNbt(sender, (INbt)values[j], indent + "    ");
                } else {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(indent + colors.getOrDefault(listType, UNKNOWN_COLOR) + "    " + values[j]));
                }
            }
        } else {
            String valueStr = "";
            if (type == 1) valueStr = BYTE_COLOR + parentNbt.getByte(key);
            else if (type == 2) valueStr = SHORT_COLOR + parentNbt.getShort(key);
            else if (type == 3) valueStr = INTEGER_COLOR + parentNbt.getInteger(key);
            else if (type == 4) valueStr = LONG_COLOR + parentNbt.getLong(key);
            else if (type == 5) valueStr = FLOAT_COLOR + parentNbt.getFloat(key);
            else if (type == 6) valueStr = DOUBLE_COLOR + parentNbt.getDouble(key);
            else if (type == 7) valueStr = UNKNOWN_COLOR + "[Byte Array]";
            else if (type == 8) {
                String value = parentNbt.getString(key);
                int length = value.length();
                if (length > maxStringLength) value = value.substring(0, maxStringLength - 1) + "... " + white + " [Length "+length+"]";
                valueStr = STRING_COLOR + value;
            } else {
                valueStr = UNKNOWN_COLOR + "Unknown Type (" + type + ")";
            }
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + key + ": " + valueStr));
        }
    }

    public void showNbt(ICommandSender sender, INbt nbt, String indent) {
        String[] keys = nbt.getKeys();
        if (indent.length() == 0 && keys.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "This item has no NBT."));
            return;
        }
        for (String key : keys) {
            displayTagValue(sender, nbt, key, indent);
        }
    }

    public boolean hasNbt(INbt nbt) {
        return !(nbt.getKeys().length == 0);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SECONDARY_ARGS.toArray(new String[0]));
        }
        return null;
    }

    public void showCommandUsage(ICommandSender sender) {
        sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+ EnumChatFormatting.GREEN+" Inspect Command "+dark_gray+"-------"));
        sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"inspect hand"+dark_gray+": "+gray+"Inspects the item in your hand."));
        sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"inspect target"+dark_gray+": "+gray+"Inspects an item bound to a pair of World Clippers."));
        sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"inspect tp <x> <y> <z> <dim>"+dark_gray+": "+gray+"Teleports you to the target. Destination arguments are optional. '~' Can be used for relative (to the target) coordinates."));
        // sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"inspect tphere <x> <y> <z> <dim>"+dark_gray+": "+gray+"Teleports the target to you. Destination arguments are optional. '~' Can be used for relative (to the player) coordinates."));
        sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"inspect keys"+dark_gray+": "+gray+"Lists what color corresponds to each data type."));

        sender.addChatMessage(ChatUtils.fillChatWithColor("After the 1st parameter, parameters can be used to navigate Nbt. Take note they are case sensitive."));
        sender.addChatMessage(ChatUtils.fillChatWithColor("Ex: /inspect hand display Name"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("Above will show just the custom name property of an item that has been renamed in an anvil."));
    }
}
