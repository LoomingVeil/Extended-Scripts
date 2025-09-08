package com.veil.extendedscripts.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.AbstractNpcAPI;

public class InspectSelfCommand implements IVeilSubCommand {

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        NBTTagCompound nbt = new NBTTagCompound();
        playerSender.writeToNBT(nbt);

        InspectCommand.showNbt(sender, AbstractNpcAPI.Instance().getINbt(nbt), "");
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new String[0];
    }
}
