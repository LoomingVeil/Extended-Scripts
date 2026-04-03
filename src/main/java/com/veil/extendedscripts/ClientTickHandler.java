package com.veil.extendedscripts;

import com.veil.extendedscripts.event.ResolutionChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerDataScript;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemScripted;

public class ClientTickHandler {
    private int prevWidth = 0;
    private int prevHeight = 0;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase == TickEvent.Phase.END && mc.thePlayer != null && mc.theWorld != null && mc.currentScreen != null) {
            ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            if (prevWidth != scaledRes.getScaledWidth() || prevHeight != scaledRes.getScaledHeight()) {
                ScreenResolution oldResolution = new ScreenResolution();
                oldResolution.setSize(prevWidth, prevHeight);
                ScreenResolution newResolution = new ScreenResolution();
                newResolution.setSize(scaledRes.getScaledWidth(), scaledRes.getScaledHeight());

                try {
                    ResolutionChangedEvent attributeRecalcEvent = new ResolutionChangedEvent(AbstractNpcAPI.Instance().getPlayer(mc.thePlayer.getCommandSenderName()), oldResolution, newResolution);

                    PlayerDataScript handler = ScriptController.Instance.getPlayerScripts(attributeRecalcEvent.getPlayer());
                    handler.callScript(attributeRecalcEvent.getHookName(), attributeRecalcEvent);
                    AbstractNpcAPI.Instance().events().post(attributeRecalcEvent);

                    prevWidth = scaledRes.getScaledWidth();
                    prevHeight = scaledRes.getScaledHeight();

                    PacketHandler.INSTANCE.sendToServer(new ScreenResolutionPacket(prevWidth, prevHeight, scaledRes.getScaleFactor()));
                } catch (NullPointerException ignored) { }
            }

            if (ClientProxy.openScriptingActionKey.isPressed()) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                ItemStack item = player.getHeldItem();

                if (item == null) {
                    return;
                }

                if (item.getItem() instanceof ItemNpcScripter) {
                    item.getItem().onItemRightClick(item, player.worldObj, player);
                    return;
                } else if (item.getItem() instanceof ItemScripted) {
                    PacketHandler.INSTANCE.sendToServer(new ScriptItemClickPacket());
                    return;
                }

                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatUtils.fillChatWithColor("§cYou must be holding a scripted item and have the proper permissions for this!")
                );
            }
        }
    }
}
