package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.IExtendedAttributeController;
import kamkeel.npcs.controllers.AttributeController;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;
import java.util.Map;



@Mixin(value={AttributeController.class})
@Implements(@Interface(iface = IExtendedAttributeController.class, prefix="extendedMixin$"))
public class MixinAttributeControllerExtension implements IExtendedAttributeController {
    @Shadow
    private static final Map<String, AttributeDefinition> definitions = new HashMap();

    @Unique
    public boolean unregisterAttribute(String key) {
        return definitions.remove(key) != null;
    }

}
