package com.veil.extendedscripts;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.INbt;
import noppes.npcs.api.item.IItemStack;
import scala.Int;
import tv.twitch.chat.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InspectItemCommand extends CommandBase {
    private static final String UNKNOWN_COLOR = EnumChatFormatting.RED.toString();
    private static final String BYTE_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String SHORT_COLOR = EnumChatFormatting.YELLOW.toString();
    private static final String INTEGER_COLOR = EnumChatFormatting.AQUA.toString();
    private static final String LONG_COLOR = EnumChatFormatting.DARK_BLUE.toString();
    private static final String FLOAT_COLOR = EnumChatFormatting.GREEN.toString();
    private static final String DOUBLE_COLOR = EnumChatFormatting.BLUE.toString();
    private static final String STRING_COLOR = EnumChatFormatting.DARK_GREEN.toString();

    private static final List<String> SECONDARY_ARGS = new ArrayList<>();

    static {
        SECONDARY_ARGS.add("hand");
        SECONDARY_ARGS.add("target");
        SECONDARY_ARGS.add("keys");
        SECONDARY_ARGS.add("tp");
        SECONDARY_ARGS.add("tphere");
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

        if (heldItem == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "You must be holding an item to use this command!"));
            return;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("hand")) {
                inspectItem(player.getHeldItem(), sender);
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
                    inspectBlock(inspectTag, targetWorld, sender);
                } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                    inspectEntity(inspectTag, targetWorld, sender);
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
                    sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Error: Invalid number of arguments"));
                    showCommandUsage(sender);
                }
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Error: Invalid arguments"));
                showCommandUsage(sender);
            }
        } else {
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

    public void inspectEntity(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender) {
        Entity foundEntity = getEntityFromInspectTag(inspectTag, targetWorld, sender);

        if (foundEntity == null) {
            return;
        }

        NBTTagCompound entityNBT = new NBTTagCompound();
        foundEntity.writeToNBT(entityNBT);
        INbt npcsNbt = AbstractNpcAPI.Instance().getINbt(entityNBT);


        if (hasNbt(npcsNbt)) sender.addChatMessage(ChatUtils.fillChatWithColor("Nbt for " + EntityList.getEntityString(foundEntity)));
        showNbt(sender, npcsNbt, "");
    }

    public Entity getEntityFromInspectTag(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender) {
        String UUIDString = inspectTag.getString("TargetUUID");
        UUID targetUUID;
        try {
            targetUUID = UUID.fromString(UUIDString); // Parse the UUID string
        } catch (IllegalArgumentException e) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Error: Invalid UUID format stored: " + UUIDString));
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
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Could not find entity! Entity may have been unloaded or killed."));
            return null;
        }

        return foundEntity;
    }

    public void inspectItem(ItemStack item, ICommandSender sender) {
        INbt itemNbt = AbstractNpcAPI.Instance().getIItemStack(item).getNbt();
        if (hasNbt(itemNbt)) sender.addChatMessage(ChatUtils.fillChatWithColor("Nbt for " + GameData.getItemRegistry().getNameForObject(item.getItem())));
        showNbt(sender, itemNbt, "");
    }

    public void inspectBlock(NBTTagCompound inspectTag, World targetWorld, ICommandSender sender) {
        int x = inspectTag.getInteger("TargetX");
        int y = inspectTag.getInteger("TargetY");
        int z = inspectTag.getInteger("TargetZ");
        Block block = targetWorld.getBlock(x, y, z);

        if (block == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Block could not be found!"));
            return;
        }

        TileEntity tileEntity = targetWorld.getTileEntity(x, y, z);
        if (tileEntity == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Nothing to see here. This block has no NBT."));
            return;
        }

        NBTTagCompound blockNBT = new NBTTagCompound();
        tileEntity.writeToNBT(blockNBT);

        if (hasNbt(AbstractNpcAPI.Instance().getINbt(blockNBT))) sender.addChatMessage(ChatUtils.fillChatWithColor("Nbt for " + GameData.getItemRegistry().getNameForObject(block.getItem(targetWorld, x, y, z))));
        showNbt(sender, AbstractNpcAPI.Instance().getINbt(blockNBT), "");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SECONDARY_ARGS.toArray(new String[0]));
        }
        return null;
    }

    public void showCommandUsage(ICommandSender sender) {
        sender.addChatMessage(ChatUtils.fillChatWithColor("Command Usage: "));
        sender.addChatMessage(ChatUtils.fillChatWithColor("1. /inspect hand"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("  - Inspects item in hand"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("2. /inspect target"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("  - Inspects an item bound to a pair of World Clippers"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("3. /inspect keys"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("  - Lists what color corresponds to each datatype"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("4. /inspect tp <x> <y> <z> <dim>"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("  - Teleports you to the target. Destination arguments are optional. '~' Can be used for relative (to the target) coordinates"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("5. /inspect tphere <x> <y> <z> <dim>"));
        sender.addChatMessage(ChatUtils.fillChatWithColor("  - Teleports the target to you. Destination arguments are optional. '~' Can be used for relative (to the player) coordinates"));
        // sender.addChatMessage(ChatUtils.fillChatWithColor("After the 1st parameter, parameters can be used to navigate Nbt. Take note they are case sensitive."));
        // sender.addChatMessage(ChatUtils.fillChatWithColor("Ex: /inspect hand display Name"));
        // sender.addChatMessage(ChatUtils.fillChatWithColor("Above will show just the custom name property of an item that has been renamed in an anvil."));
    }

    public boolean hasNbt(INbt nbt) {
        return !(nbt.getKeys().length == 0);
    }

    /*public Object navigateNbt(INbt nbt, String[] keys) {
        int type = nbt.getType(keys[0]);
        if (type == 1) { // Byte / Boolean
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getByte(keys[0]);
            }
        } else if (type == 2) { // Short
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getShort(keys[0]);
            }
        } else if (type == 3) { // Integer
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getByte(keys[0]);
            }
        } else if (type == 4) { // Long
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getLong(keys[0]);
            }
        } else if (type == 5) { // Float
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getFloat(keys[0]);
            }
        } else if (type == 6) { // Double
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getDouble(keys[0]);
            }
        } else if (type == 7) {

        } else if (type == 8) { // String
            if (keys.length > 1) {
                return "ERR: " + (keys.length - 1) + " left";
            } else if (!nbt.has(keys[0])) {
                return "ERR " + keys[0] + " not found";
            } else {
                return nbt.getString(keys[0]);
            }
        } else if (type == 9) { // Compound tag
            Object[] values = nbt.getList(keys[0], nbt.getListType(keys[0]));
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + " with " + values.length + " values:"));
            for (int j = 0; j < values.length; j++) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + "  Index " + j + ":"));
                if (values[j].getClass().getName().toString().equals("noppes.npcs.scripted.ScriptNbt")) {
                    showNbt(sender, (INbt)values[j], indent + "    ");
                } else {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(indent + "    " + values[j]));
                }
            }
        } else if (type == 10) { // Compound
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ":"));
            showNbt(sender, nbt.getCompound(keys[i]), indent + "  ");
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ":" + UNKNOWN_COLOR + " unknown type §8" + type));
        }

    }*/

    public void showNbt(ICommandSender sender, INbt nbt, String indent) {
        String[] keys = nbt.getKeys();
        if (indent.length() == 0 && keys.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(UNKNOWN_COLOR + "Nothing to see here. This item has no NBT."));
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            int type = nbt.getType(keys[i]);
            if (type == 1) { // Byte / Boolean
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + BYTE_COLOR + nbt.getByte(keys[i])));
            } else if (type == 2) { // Short
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + SHORT_COLOR + nbt.getShort(keys[i])));
            } else if (type == 3) { // Integer
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + INTEGER_COLOR + nbt.getInteger(keys[i])));
            } else if (type == 4) { // Long
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + LONG_COLOR + nbt.getLong(keys[i])));
            } else if (type == 5) { // Float
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + FLOAT_COLOR + nbt.getFloat(keys[i])));
            } else if (type == 6) { // Double
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + DOUBLE_COLOR + nbt.getDouble(keys[i])));
            } else if (type == 7) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + UNKNOWN_COLOR + "Idek what a byte array is"));
            } else if (type == 8) { // String
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ": " + STRING_COLOR + nbt.getString(keys[i])));
            } else if (type == 9) { // Compound tag
                Object[] values = nbt.getList(keys[i], nbt.getListType(keys[i]));
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + " with " + values.length + " values:"));
                for (int j = 0; j < values.length; j++) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(indent + "  Index " + j + ":"));
                    if (values[j].getClass().getName().toString().equals("noppes.npcs.scripted.ScriptNbt")) {
                        showNbt(sender, (INbt)values[j], indent + "    ");
                    } else {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(indent + "    " + values[j]));
                    }
                }
            } else if (type == 10) { // Compound
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ":"));
                showNbt(sender, nbt.getCompound(keys[i]), indent + "  ");
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(indent + keys[i] + ":" + UNKNOWN_COLOR + " unknown type §8" + type));
            }
        }
    }

}
