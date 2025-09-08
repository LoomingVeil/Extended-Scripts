package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.WorldClippers;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class InspectTpCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        ItemStack heldItem = playerSender.getHeldItem();

        if (heldItem.getItem() != ExtendedScripts.worldClippers) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
            return;
        }

        if (args.length == 1 || args.length == 4 || args.length == 5) {
            if (!WorldClippers.hasInspectTag(heldItem)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "No target selected! Right click a block or entity."));
                return;
            }
            NBTTagCompound inspectTag = WorldClippers.getInspectTag(heldItem);

            WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(inspectTag.getInteger("TargetDimension"));
            if (targetWorld == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "Could not find world! Perhaps it is unloaded."));
                return;
            }

            if (args.length == 1) {
                if (inspectTag.getByte("TargetType") == 1) { // Block
                    int x = inspectTag.getInteger("TargetX");
                    int y = inspectTag.getInteger("TargetY");
                    int z = inspectTag.getInteger("TargetZ");
                    Block block = targetWorld.getBlock(x, y, z);

                    if (block == null) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "Block could not be found!"));
                        return;
                    }

                    playerSender.setPositionAndUpdate(x + 0.5, y, z + 0.5);
                    sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported to target!"));
                } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                    Entity foundEntity = InspectCommand.getEntityFromInspectTag(inspectTag, targetWorld, sender);
                    if (foundEntity == null) return;

                    playerSender.setPositionAndUpdate(foundEntity.posX, foundEntity.posY, foundEntity.posZ);
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
                        sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "Block could not be found!"));
                        return;
                    }

                    double[] dest = InspectCommand.getCoordinatesFromArgument(
                        new String[] {args[1], args[2], args[3]},
                        new double[] {x + 0.5, y, z + 0.5},
                        sender
                    );

                    playerSender.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                    sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                    Entity foundEntity = InspectCommand.getEntityFromInspectTag(inspectTag, targetWorld, sender);
                    if (foundEntity == null) return;

                    double[] dest = InspectCommand.getCoordinatesFromArgument(
                        new String[] {args[1], args[2], args[3]},
                        new double[] {foundEntity.posX, foundEntity.posY, foundEntity.posZ},
                        sender
                    );

                    playerSender.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                    sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                }
            } else if (args.length == 5) {
                if (!(heldItem.getItem() instanceof WorldClippers)) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
                    return;
                }

                if (inspectTag.getByte("TargetType") == 1) { // Block
                    int x = inspectTag.getInteger("TargetX");
                    int y = inspectTag.getInteger("TargetY");
                    int z = inspectTag.getInteger("TargetZ");
                    Block block = targetWorld.getBlock(x, y, z);

                    if (block == null) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "Block could not be found!"));
                        return;
                    }

                    double[] dest = InspectCommand.getCoordinatesFromArgument(
                        new String[] {args[1], args[2], args[3], args[4]},
                        new double[] {x + 0.5, y, z + 0.5},
                        sender
                    );

                    playerSender.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                    playerSender.travelToDimension((int)dest[3]);
                    sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                } else if (inspectTag.getByte("TargetType") == 2) { // Entity
                    Entity foundEntity = InspectCommand.getEntityFromInspectTag(inspectTag, targetWorld, sender);
                    if (foundEntity == null) return;

                    double[] dest = InspectCommand.getCoordinatesFromArgument(
                        new String[] {args[1], args[2], args[3], args[4]},
                        new double[] {foundEntity.posX, foundEntity.posY, foundEntity.posZ},
                        sender
                    );

                    playerSender.setPositionAndUpdate(dest[0], dest[1], dest[2]);
                    playerSender.travelToDimension((int)dest[3]);
                    sender.addChatMessage(ChatUtils.fillChatWithColor("§aTeleported relative to target!"));
                }
            }
        } else {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid number of arguments!"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /inspect tp <x> <y> <z> <dim>"));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
