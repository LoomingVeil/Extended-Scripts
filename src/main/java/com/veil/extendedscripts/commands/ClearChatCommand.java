package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.ExtendedAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IPlayer;

public class ClearChatCommand implements IVeilSubCommand {
    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (playerSender == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"This command can only be used by a player."));
            return;
        }

        ChatComponentText empty = new ChatComponentText("");
        if (args.length == 0) {
            for (int i = 0; i < 100; i++) {
                sender.addChatMessage(empty);
            }
        } else if (args.length == 1) {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null) {
                if (playerName.equals("ALL")) {
                    IPlayer[] allPlayers = AbstractNpcAPI.Instance().getAllServerPlayers();
                    for (IPlayer npcPlayer : allPlayers) {
                        for (int i = 0; i < 100; i++) {
                            ((EntityPlayer) npcPlayer.getMCEntity()).addChatMessage(empty);
                        }
                    }
                    return;
                }
            }

            int numClears = -1;
            try {
                numClears = Integer.valueOf(args[0]);
                if (numClears <= 0) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"The number of clear lines can not be less than 1!"));
                    return;
                }
            } catch (NumberFormatException ignored) { }

            if (numClears == -1 && player == null) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            if (numClears != -1) {
                for (int i = 0; i < numClears; i++) {
                    sender.addChatMessage(empty);
                }
            } else {
                for (int i = 0; i < 100; i++) {
                    player.addChatMessage(empty);
                }
            }
        } else if (args.length == 2) {
            String playerName = args[0];
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
            if (player == null && !playerName.equals("ALL")) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Could not find player!"));
                return;
            }

            int numClears;
            try {
                numClears = Integer.valueOf(args[1]);
                if (numClears <= 0) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"The number of clear lines can not be less than 1!"));
                    return;
                }
            } catch (NumberFormatException e) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Invalid number of clear lines."));
                return;
            }

            if (playerName.equals("ALL")) {
                IPlayer[] allPlayers = AbstractNpcAPI.Instance().getAllServerPlayers();
                for (IPlayer npcPlayer : allPlayers) {
                    for (int i = 0; i < numClears; i++) {
                        ((EntityPlayer) npcPlayer.getMCEntity()).addChatMessage(empty);
                    }
                }
            } else {
                for (int i = 0; i < numClears; i++) {
                    player.addChatMessage(empty);
                }
            }
        }
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            String[] playerNames = ExtendedAPI.Instance.getAllServerPlayerNames();
            String[] playerNamesPlusOne = new String[playerNames.length + 1];
            System.arraycopy(playerNames, 0, playerNamesPlusOne, 1, playerNames.length);
            playerNamesPlusOne[0] = "ALL";

            return playerNamesPlusOne;
        }

        return null;
    }
}
