package com.veil.extendedscripts;

import com.veil.extendedscripts.item.ExtendedScriptCustomItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import kamkeel.npcs.network.AbstractPacket;
import kamkeel.npcs.network.PacketChannel;
import kamkeel.npcs.network.PacketClient;
import kamkeel.npcs.network.PacketHandler;
import kamkeel.npcs.network.enums.EnumRequestPacket;
import kamkeel.npcs.network.packets.data.large.GuiDataPacket;
import kamkeel.npcs.util.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.config.ConfigScript;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.item.ScriptCustomItem;
import noppes.npcs.scripted.item.ScriptCustomizableItem;

import java.io.IOException;

public class ExtendedItemScriptPacket extends AbstractPacket {
    public static String packetName = "Request|ExtendedItemScript";
    private ExtendedItemScriptPacket.Action type;
    private NBTTagCompound compound;

    public ExtendedItemScriptPacket() {
    }

    public ExtendedItemScriptPacket(ExtendedItemScriptPacket.Action type, NBTTagCompound compound) {
        this.type = type;
        this.compound = compound;
    }

    public Enum getType() {
        return EnumRequestPacket.ItemScript;
    }

    public PacketChannel getChannel() {
        return PacketHandler.REQUEST_PACKET;
    }

    public CustomNpcsPermissions.Permission getPermission() {
        return CustomNpcsPermissions.SCRIPT_ITEM;
    }

    @SideOnly(Side.CLIENT)
    public void sendData(ByteBuf out) throws IOException {
        out.writeInt(this.type.ordinal());
        if (this.type == ExtendedItemScriptPacket.Action.SAVE) {
            ByteBufUtils.writeNBT(out, this.compound);
        }

    }

    public void receiveData(ByteBuf in, EntityPlayer player) throws IOException {
        if (!(player instanceof EntityPlayerMP))
            return;

        if (!ConfigScript.canScript(player, CustomNpcsPermissions.SCRIPT))
            return;

        ExtendedItemScriptPacket.Action requestedAction = ExtendedItemScriptPacket.Action.values()[in.readInt()];
        if (requestedAction == ExtendedItemScriptPacket.Action.GET) {
            NBTTagCompound compound;
            IItemStack itemStack = NpcAPI.Instance().getIItemStack(player.getHeldItem());
            if (itemStack instanceof ExtendedScriptCustomItem) {
                ExtendedScriptCustomItem iw = (ExtendedScriptCustomItem) itemStack;
                iw.loadScriptData();
                compound = iw.getMCNbt();
            } else { // Normal scripted items come through here for whatever reason
                ScriptCustomItem iw = (ScriptCustomItem) itemStack;
                iw.loadScriptData();
                compound = iw.getMCNbt();
            }

            compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
            GuiDataPacket.sendGuiData((EntityPlayerMP) player, compound);
        } else {
            if (!player.capabilities.isCreativeMode) {
                return;
            }

            NBTTagCompound compound = ByteBufUtils.readNBT(in);
            IItemStack itemStack = NpcAPI.Instance().getIItemStack(player.getHeldItem());
            if (itemStack instanceof ExtendedScriptCustomItem) {
                ExtendedScriptCustomItem wrapper = (ExtendedScriptCustomItem) NpcAPI.Instance().getIItemStack(player.getHeldItem());
                wrapper.setMCNbt(compound);
                wrapper.saveScriptData();
                wrapper.loaded = false;
                wrapper.errored.clear();
                wrapper.lastInited = -1;
            } else {
                ScriptCustomItem wrapper = (ScriptCustomItem) itemStack;
                wrapper.setMCNbt(compound);
                wrapper.saveScriptData();
                wrapper.loaded = false;
                wrapper.errored.clear();
                wrapper.lastInited = -1;
            }

            ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
        }
    }

    public static void Save(NBTTagCompound compound) {
        PacketClient.sendClient(new ExtendedItemScriptPacket(ExtendedItemScriptPacket.Action.SAVE, compound));
    }

    public static void Get() {
        PacketClient.sendClient(new ExtendedItemScriptPacket(ExtendedItemScriptPacket.Action.GET, new NBTTagCompound()));
    }

    private static enum Action {
        GET,
        SAVE;

        private Action() {
        }
    }
}
