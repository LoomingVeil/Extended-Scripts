package com.veil.extendedscripts;

import kamkeel.npcs.util.ColorUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.Timer;
import java.util.TimerTask;

import static kamkeel.npcs.util.ColorUtil.assembleComponent;

public class ChatUtils {
    public static void sendDelayedChatMessage(ICommandSender sender, ChatComponentText message, long delayMillis) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sender.addChatMessage(message);
            }
        }, delayMillis);
    }

    public static String fillStringWithColor(String input) {
        String newString = "";
        boolean skipNext = false;
        String currentColor = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '§') {
                currentColor = "§" + input.charAt(i + 1);
                skipNext = true;
            } else if (!skipNext) {
                newString += currentColor + input.charAt(i);
            } else {
                skipNext = false;
            }
        }
        return newString;
    }

    public static IChatComponent fillChatWithColor(String input) {
        // Uses CustomNPC+'s code
        return ColorUtil.assembleComponent(input);
    }
}
