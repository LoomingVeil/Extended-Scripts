package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.extendedapi.IFieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.field.FieldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * Experimental class currently disabled
 */
@Mixin(value = FieldInfo.class)
public class MixinFieldInfo implements IFieldInfo {
    @Shadow
    private String documentation;

    @Shadow
    private boolean resolved;

    @Unique
    public void setDocumentation(String newDocumentation) {
        documentation = newDocumentation;
    }

    @Unique
    public void setResolved(boolean newResolveState) {
        resolved = newResolveState;
    }
}
