package com.veil.extendedscripts.mixins;

import noppes.npcs.client.gui.util.script.autocomplete.AutocompleteItem;
import noppes.npcs.client.gui.util.script.autocomplete.AutocompleteProvider;
import noppes.npcs.client.gui.util.script.autocomplete.JSAutocompleteProvider;
import noppes.npcs.client.gui.util.script.autocomplete.JavaAutocompleteProvider;
import noppes.npcs.client.gui.util.script.interpreter.ScriptDocument;
import noppes.npcs.client.gui.util.script.interpreter.js_parser.JSFieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.type.TypeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Experimental class currently disabled
 */
@Mixin(value = JSAutocompleteProvider.class, remap = false)
public class MixinJSAutocompletePro extends JavaAutocompleteProvider {
    @Inject(method = "addMemberSuggestions", at = @At(value = "HEAD"), cancellable = true)
    protected void mixinAddMemberSuggestions(AutocompleteProvider.Context context, List<AutocompleteItem> items, CallbackInfo ci) {
        String receiverExpr = context.receiverExpression;
        System.out.println("Expression: "+receiverExpr);
        // We need to add an Autocomplete item.
        if (receiverExpr.equals("CustomEffect")) {
            int resolvePos = getMemberAccessResolvePosition(context);
            // Use ScriptDocument's resolveExpressionType - handles both Java and JS
            TypeInfo receiverType = document.resolveExpressionType(receiverExpr, resolvePos);
            System.out.println("Receiver Type: "+receiverType.getDisplayName());
            var autocompleteItem = AutocompleteItem.fromJSField(new JSFieldInfo("Test", "number", true));
            items.add(autocompleteItem);
        }

    }
}
