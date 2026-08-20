package com.veil.extendedscripts.commands;

import com.veil.extendedscripts.ChatUtils;
import com.veil.extendedscripts.Config;
import com.veil.extendedscripts.ExtendedAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.INbt;
import noppes.npcs.api.entity.IPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeSlotCommand implements IVeilSubCommand {
    private final Map<String, IVeilSubCommand> subCommands = new HashMap<>();

    public AttributeSlotCommand() {
        subCommands.put("add", new AddSlotCommand());
        subCommands.put("remove", new RemoveSlotCommand());
        subCommands.put("verify", new VerifySlotCommand());
    }

    @Override
    public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+ yellow+" SLOT SubCommands "+dark_gray+"-------"));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"add <path>"+dark_gray+": "+gray+"Adds an NBT path to the extra attribute slots config."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"remove <path>"+dark_gray+": "+gray+"Removes an NBT path from the extra attribute slots config."));
            sender.addChatMessage(ChatUtils.fillChatWithColor(gray+"> "+yellow+"verify [player] [path]"+dark_gray+": "+gray+"Verifies if items exist at the given path(s). If no path, verifies all in config."));
            return;
        }

        IVeilSubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Unknown subcommand "+args[0]));
            return;
        }

        subCommand.execute(sender, playerSender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().toArray(new String[0]);
        } else {
            IVeilSubCommand subCommand = subCommands.get(args[0]);
            if (subCommand != null) {
                return subCommand.addTabCompletionOptions(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return null;
    }

    private static class AddSlotCommand implements IVeilSubCommand {
        @Override
        public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
            if (args.length == 0) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /attribute slot add <path>"));
                return;
            }

            String path = args[0];
            List<String> currentSlots = new ArrayList<>(Arrays.asList(Config.extraAttributeSlots.getStringList()));

            if (currentSlots.contains(path)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Path already exists in config."));
                return;
            }

            currentSlots.add(path);
            Config.extraAttributeSlots.set(currentSlots.toArray(new String[0]));
            Config.config.save();

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+yellow+"Added path: "+gray+path));
        }

        @Override
        public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
            return null;
        }
    }

    private static class RemoveSlotCommand implements IVeilSubCommand {
        @Override
        public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
            if (args.length == 0) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /attribute slot remove <path>"));
                return;
            }

            String path = args[0];
            List<String> currentSlots = new ArrayList<>(Arrays.asList(Config.extraAttributeSlots.getStringList()));

            if (!currentSlots.contains(path)) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"Path not found in config."));
                return;
            }

            currentSlots.remove(path);
            Config.extraAttributeSlots.set(currentSlots.toArray(new String[0]));
            Config.config.save();

            sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+yellow+"Removed path: "+gray+path));
        }

        @Override
        public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
            if (args.length == 1) {
                return Config.extraAttributeSlots.getStringList();
            }
            return null;
        }
    }

    private static class VerifySlotCommand implements IVeilSubCommand {
        @Override
        public void execute(ICommandSender sender, EntityPlayer playerSender, String[] args) {
            EntityPlayer targetPlayer = playerSender;
            String[] pathsToVerify;

            if (args.length == 0) {
                // No arguments - verify all paths in config for the sender
                pathsToVerify = Config.extraAttributeSlots.getStringList();
            } else if (args.length == 1) {
                // Could be player or path - check if it's a player name
                EntityPlayer potentialPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);
                if (potentialPlayer != null) {
                    targetPlayer = potentialPlayer;
                    pathsToVerify = Config.extraAttributeSlots.getStringList();
                } else {
                    // It's a path
                    pathsToVerify = new String[]{args[0]};
                }
            } else if (args.length == 2) {
                // Both player and path specified
                targetPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);
                if (targetPlayer == null) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Player not found: "+args[0]));
                    return;
                }
                pathsToVerify = new String[]{args[1]};
            } else {
                sender.addChatMessage(ChatUtils.fillChatWithColor(red+"Usage: /attribute slot verify [player] [path]"));
                return;
            }

            if (pathsToVerify.length == 0 || (pathsToVerify.length == 1 && pathsToVerify[0].isEmpty())) {
                sender.addChatMessage(ChatUtils.fillChatWithColor(modPrefix+dark_red+"Error: "+red+"No paths configured to verify."));
                return;
            }

            sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+ yellow+" Verifying for "+targetPlayer.getCommandSenderName()+" "+dark_gray+"-------"));

            int successCount = 0;
            int failureCount = 0;

            for (String path : pathsToVerify) {
                try {
                    ItemStack item = getItemFromPath(path, targetPlayer);
                    if (item == null) {
                        sender.addChatMessage(ChatUtils.fillChatWithColor(red+"✗ "+gray+path+dark_red+": No item found"));
                        failureCount++;
                    } else {
                        String itemName = item.getDisplayName();
                        if (itemName == null || itemName.isEmpty()) {
                            itemName = item.getItem().getUnlocalizedName();
                        }
                        sender.addChatMessage(ChatUtils.fillChatWithColor(EnumChatFormatting.GREEN+"✓ "+gray+path+yellow+": Found "+itemName));
                        successCount++;
                    }
                } catch (Exception e) {
                    sender.addChatMessage(ChatUtils.fillChatWithColor(red+"✗ "+gray+path+dark_red+": "+e.getMessage()));
                    failureCount++;
                }
            }

            sender.addChatMessage(ChatUtils.fillChatWithColor(dark_gray+"------- "+ yellow+" Results: "+successCount+" success, "+failureCount+" failed "+dark_gray+"-------"));
        }

        @Override
        public String[] addTabCompletionOptions(ICommandSender sender, String[] args) {
            if (args.length == 1) {
                // Could be player or path - return both
                String[] players = ExtendedAPI.Instance.getAllServerPlayerNames();
                String[] paths = Config.extraAttributeSlots.getStringList();
                String[] combined = new String[players.length + paths.length];
                System.arraycopy(players, 0, combined, 0, players.length);
                System.arraycopy(paths, 0, combined, players.length, paths.length);
                return combined;
            } else if (args.length == 2) {
                // If first arg was a player, suggest paths
                EntityPlayer potentialPlayer = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);
                if (potentialPlayer != null) {
                    return Config.extraAttributeSlots.getStringList();
                }
                // Otherwise suggest players
                return ExtendedAPI.Instance.getAllServerPlayerNames();
            }
            return null;
        }

        private ItemStack getItemFromPath(String path, EntityPlayer player) throws Exception {
            IPlayer iPlayer = AbstractNpcAPI.Instance().getPlayer(player.getCommandSenderName());
            INbt playerNbt = iPlayer.getAllNbt();

            if (path.contains(":")) {
                String[] parts = path.split(":");
                String nbtPath = parts[0];
                int slotIndex = Integer.parseInt(parts[1]);

                Object returnedData = ExtendedAPI.traverseNbt(playerNbt, nbtPath.split("\\."));
                if (returnedData.getClass().isArray()) {
                    for (Object slot : (Object[]) returnedData) {
                        INbt nbt = (INbt) slot;
                        List<String> keys = Arrays.asList(nbt.getKeys());
                        if (keys.contains("Slot") && nbt.getInteger("Slot") == slotIndex) {
                            NBTTagCompound mcNbt = nbt.getMCNBT();
                            ItemStack item = ItemStack.loadItemStackFromNBT(mcNbt);
                            if (item != null) {
                                return item;
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("Path " + nbtPath + " is not an array");
                }
            } else {
                INbt slotNbt = (INbt) ExtendedAPI.traverseNbt(playerNbt, path.split("\\."));
                NBTTagCompound mcNbt = slotNbt.getMCNBT();
                ItemStack item = ItemStack.loadItemStackFromNBT(mcNbt);
                if (item != null) {
                    return item;
                }
            }
            return null;
        }
    }
}
