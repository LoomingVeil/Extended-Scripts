package com.veil.extendedscripts.mixins;

import noppes.npcs.api.overlay.ICustomOverlayComponent;
import noppes.npcs.extendedapi.overlay.ICustomOverlay;
import noppes.npcs.scripted.overlay.ScriptOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ScriptOverlay.class)
public class MixinScriptOverlayExtension implements ICustomOverlay {
    @Shadow
    List<ICustomOverlayComponent> components;

    @Unique
    public int nextComponentId() {
        int possibleId = 0;
        List<Integer> ids = new ArrayList<>();
        for (ICustomOverlayComponent component : components) {
            ids.add(component.getID());
        }

        while (true) {
            if (!ids.contains(possibleId)) {
                return possibleId;
            }
            possibleId++;
        }
    }
}
