package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.ExtendedEnumGuiType;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.client.NoppesUtil;

/**
 * This code is a near replica of CustomNpc+'s GuiOpenPacket. However, this class accepts ints as well as Enums
 */
public final class GuiOpenPacket implements IMessage {
    public static final String packetName = "Data|OpenGui";
    private int type;
    private int x;
    private int y;
    private int z;

    public GuiOpenPacket() {}

    public GuiOpenPacket(ExtendedEnumGuiType type, int x, int y, int z) {
        this.type = type.ordinal();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GuiOpenPacket(int type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void openGUI(EntityPlayerMP playerMP, ExtendedEnumGuiType type, int x, int y, int z) {
        GuiOpenPacket packet = new GuiOpenPacket(type, x, y, z);
        PacketHandler.INSTANCE.sendTo(packet, playerMP);
    }

    public static void openGUI(EntityPlayerMP playerMP, int type, int x, int y, int z) {
        GuiOpenPacket packet = new GuiOpenPacket(type, x, y, z);
        PacketHandler.INSTANCE.sendTo(packet, playerMP);
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.type);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public static class GuiOpenPacketHandler implements IMessageHandler<GuiOpenPacket, IMessage> {
        @Override
        public IMessage onMessage(final GuiOpenPacket message, final MessageContext ctx) {
            // This method is called on the receiving side (CLIENT in this case)
            Minecraft.getMinecraft().func_152344_a(new Runnable() {
                @Override
                public void run() {
                    ExtendedScripts.proxy.openGui(NoppesUtil.getLastNpc(), message.getType(), message.getX(), message.getY(), message.getZ());
                }
            });
            return null;
        }
    }
}
