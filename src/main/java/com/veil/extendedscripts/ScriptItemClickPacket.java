package com.veil.extendedscripts;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.packets.data.ChatAlertPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.config.ConfigScript;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.items.ItemScripted;

public class ScriptItemClickPacket implements IMessage {

    public ScriptItemClickPacket() {}

    public ScriptItemClickPacket(ItemStack hand) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<ScriptItemClickPacket, IMessage> {
        @Override
        public IMessage onMessage(ScriptItemClickPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            ItemStack item = player.getHeldItem();

            if (item == null || player.worldObj.isRemote) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatUtils.fillChatWithColor("§cYou must be holding a scripted item and have the proper permissions for this!")
                );
                return null;
            }

            if (!(item.getItem() instanceof ItemScripted)) {
                return null;
            }

            if (!ConfigScript.canScript(player, CustomNpcsPermissions.TOOL_SCRIPTED_ITEM)) {
                ChatAlertPacket.sendChatAlert(player, "availability.permission");
            } else {
                NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptItem, null, 0, 0, 0);
            }

            return null;
        }
    }
}
