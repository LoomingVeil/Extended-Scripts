package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.scripting.ScriptGlobalDescriptor;
import com.veil.extendedscripts.scripting.ScriptGlobalRegistry;
import noppes.npcs.client.gui.util.script.autocomplete.AutocompleteItem;
import noppes.npcs.client.gui.util.script.autocomplete.AutocompleteProvider;
import noppes.npcs.client.gui.util.script.autocomplete.JSAutocompleteProvider;
import noppes.npcs.client.gui.util.script.autocomplete.JavaAutocompleteProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = JSAutocompleteProvider.class, remap = false)
public class MixinJSAutocompletePro extends JavaAutocompleteProvider {
    @Inject(method = "addLanguageUniqueSuggestions", at = @At("TAIL"))
    protected void extendedscripts$addDynamicGlobals(AutocompleteProvider.Context context, List<AutocompleteItem> items, CallbackInfo ci) {
        for (ScriptGlobalDescriptor descriptor : ScriptGlobalRegistry.getDescriptors()) {
            if (descriptor == null || descriptor.getGlobalName() == null) {
                continue;
            }
            if (containsItemNamed(items, descriptor.getGlobalName())) {
                continue;
            }

            items.add(new AutocompleteItem.Builder()
                .name(descriptor.getGlobalName())
                .insertText(descriptor.getGlobalName())
                .kind(AutocompleteItem.Kind.FIELD)
                .typeLabel("object")
                .build());
        }
    }

    @Inject(method = "addMemberSuggestions", at = @At(value = "HEAD"), cancellable = true)
    protected void extendedscripts$addDynamicGlobalMembers(AutocompleteProvider.Context context, List<AutocompleteItem> items, CallbackInfo ci) {
        if (context == null) {
            return;
        }

        String receiverExpr = context.receiverExpression;
        if (receiverExpr == null) {
            return;
        }
        String receiverName = receiverExpr.trim();
        ScriptGlobalDescriptor descriptor = ScriptGlobalRegistry.findByName(receiverName);
        if (descriptor == null) {
            return;
        }

        for (ScriptGlobalDescriptor.MemberEntry entry : descriptor.getMembers()) {
            if (entry == null) {
                continue;
            }
            items.add(new AutocompleteItem.Builder()
                .name(entry.key)
                .insertText(entry.key)
                .kind(AutocompleteItem.Kind.FIELD)
                .typeLabel(entry.typeName != null ? entry.typeName : "object")
                .signature(entry.key + ": " + (entry.typeName != null ? entry.typeName : "object"))
                .documentation(entry.documentation)
                .build());
        }

        ci.cancel();
    }

    private static boolean containsItemNamed(List<AutocompleteItem> items, String name) {
        if (items == null) {
            return false;
        }
        for (AutocompleteItem item : items) {
            if (item != null && name.equals(item.getName())) {
                return true;
            }
        }
        return false;
    }
}
