package com.veil.extendedscripts.mixins;

import noppes.npcs.extendedapi.overlay.IOverlayLabel;
import noppes.npcs.scripted.overlay.ScriptOverlayLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(value = ScriptOverlayLabel.class)
public abstract class MixinScriptOverlayLabelExtension implements IOverlayLabel {
    @Shadow
    public abstract String getText();

    private static HashMap<Character, Integer> specialLetterLengths = new HashMap<>();
    static {
        specialLetterLengths.put('I', 3);
        specialLetterLengths.put('i', 1);
        specialLetterLengths.put('l', 2);
        specialLetterLengths.put('t', 3);
        specialLetterLengths.put('.', 1);
        specialLetterLengths.put(',', 1);
        specialLetterLengths.put(';', 1);
        specialLetterLengths.put(':', 1);
        specialLetterLengths.put('\'', 1);
        specialLetterLengths.put('!', 1);
        specialLetterLengths.put('\"', 3);
        specialLetterLengths.put('(', 4);
        specialLetterLengths.put(')', 4);
        specialLetterLengths.put('@', 6);
    }

    /**
     * Gets the width of the string in pixels. This can be useful for centering text.
     * Having certain non-standard special characters may produce inaccurate results.
     */
    public int getStringWidth() {
        int length = 0;
        String text = getText();
        for (int i = 0; i < text.length(); i++) {
            length += specialLetterLengths.getOrDefault(text.charAt(i), 5);
        }
        length += text.length() - 1;

        return length;
    }
}
