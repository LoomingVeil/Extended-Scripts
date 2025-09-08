package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.WorldClippers;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.Arrays;

public class InspectTargetCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        ItemStack heldItem = playerSender.getHeldItem();

        if (!(heldItem.getItem() instanceof WorldClippers)) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(InspectCommand.UNKNOWN_COLOR + "You must be holding a pair of World Clippers to use this command!"));
            return;
        }

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

        if (inspectTag.getByte("TargetType") == 1) { // Block
            InspectCommand.inspectBlock(inspectTag, targetWorld, sender, Arrays.copyOfRange(args, 2, args.length));
        } else if (inspectTag.getByte("TargetType") == 2) { // Entity
            InspectCommand.inspectEntity(inspectTag, targetWorld, sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
