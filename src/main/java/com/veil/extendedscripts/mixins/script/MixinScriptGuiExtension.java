package com.veil.extendedscripts.mixins.script;

import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.extendedapi.gui.ICustomGui;
import noppes.npcs.scripted.gui.ScriptGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(value = ScriptGui.class)
public class MixinScriptGuiExtension implements ICustomGui {
    @Shadow
    private HashMap<Integer, ICustomGuiComponent> components;

    @Unique
    public int nextComponentId() {
        int possibleId = 0;
        List<Integer> ids = new ArrayList<>(components.keySet());

        while (true) {
            if (!ids.contains(possibleId)) {
                return possibleId;
            }
            possibleId++;
        }
    }
}
