package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.scripting.DynamicScriptGlobalObject;
import com.veil.extendedscripts.scripting.ScriptGlobalDescriptor;
import com.veil.extendedscripts.scripting.ScriptGlobalRegistry;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.scripted.NpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScriptContainer.class, remap = false)
public class MixinScriptContainerGlobals {
    @Inject(method = "run(Ljava/lang/String;Ljava/lang/Object;)V", at = @At("HEAD"))
    private void extendedscripts$ensureRegisteredGlobals(String type, Object event, CallbackInfo ci) {
        boolean changed = false;
        for (ScriptGlobalDescriptor descriptor : ScriptGlobalRegistry.getDescriptors()) {
            if (descriptor == null || descriptor.getGlobalName() == null) {
                continue;
            }

            String globalName = descriptor.getGlobalName();
            if (!NpcAPI.engineObjects.containsKey(globalName)) {
                DynamicScriptGlobalObject runtimeObject = ScriptGlobalRegistry.getOrCreateRuntimeObject(descriptor);
                if (runtimeObject != null) {
                    NpcAPI.engineObjects.put(globalName, runtimeObject);
                    changed = true;
                }
            }
        }

        if (changed) {
            NpcAPI.engineObjectsVersion++;
        }
    }
}
