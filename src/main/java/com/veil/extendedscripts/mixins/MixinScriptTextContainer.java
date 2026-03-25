package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.scripting.ScriptGlobalDescriptor;
import com.veil.extendedscripts.scripting.ScriptGlobalRegistry;
import noppes.npcs.client.gui.util.script.interpreter.ScriptTextContainer;
import noppes.npcs.client.gui.util.script.interpreter.field.FieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.type.TypeInfo;
import noppes.npcs.client.gui.util.script.interpreter.type.TypeResolver;
import noppes.npcs.client.gui.util.script.interpreter.type.synthetic.SyntheticType;
import noppes.npcs.client.gui.util.script.interpreter.type.synthetic.SyntheticTypeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.veil.extendedscripts.scripting.ScriptGlobalRegistry.toTypeInfo;

@Mixin(value = ScriptTextContainer.class, remap = false)
public class MixinScriptTextContainer {
    private static Field syntheticFieldsField;

    @Inject(method = "formatCodeText", at = @At("HEAD"))
    private void extendedscripts$registerDynamicSyntheticTypes(CallbackInfo ci) {
        for (ScriptGlobalDescriptor descriptor : ScriptGlobalRegistry.getDescriptors()) {
            if (descriptor == null || descriptor.getGlobalName() == null) {
                continue;
            }

            SyntheticTypeBuilder builder = new SyntheticTypeBuilder(descriptor.getGlobalName());

            List<ScriptGlobalDescriptor.MemberEntry> members = descriptor.getMembers();
            for (ScriptGlobalDescriptor.MemberEntry entry : members) {
                if (entry == null) {
                    continue;
                }
                builder.addStaticField(
                    entry.key,
                    entry.typeName != null ? entry.typeName : "object",
                    entry.documentation
                );
            }

            SyntheticType syntheticType = builder.build();
            TypeInfo globalTypeInfo = syntheticType.getTypeInfo();
            clearSyntheticFields(globalTypeInfo, members);
            for (ScriptGlobalDescriptor.MemberEntry entry : members) {
                if (entry == null) {
                    continue;
                }
                addStaticSyntheticField(globalTypeInfo, entry.key, entry.typeName);
            }

            TypeResolver.getInstance().registerSyntheticType(descriptor.getGlobalName(), syntheticType);
        }
    }

    @SuppressWarnings("unchecked")
    private static void clearSyntheticFields(TypeInfo globalTypeInfo, List<ScriptGlobalDescriptor.MemberEntry> members) {
        try {
            Field field = getSyntheticFieldsField();
            List<FieldInfo> syntheticFields = (List<FieldInfo>) field.get(globalTypeInfo);
            Set<String> memberNames = new HashSet<String>();
            for (ScriptGlobalDescriptor.MemberEntry member : members) {
                if (member != null && member.key != null) {
                    memberNames.add(member.key);
                }
            }
            syntheticFields.removeIf(f -> f != null && f.getName() != null && memberNames.contains(f.getName()));
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    private static void addStaticSyntheticField(TypeInfo globalTypeInfo, String fieldName, String typeName) {
        try {
            Field field = getSyntheticFieldsField();
            List<FieldInfo> syntheticFields = (List<FieldInfo>) field.get(globalTypeInfo);
            int modifiers = Modifier.PUBLIC | Modifier.STATIC;
            syntheticFields.add(FieldInfo.external(fieldName, toTypeInfo(typeName), null, modifiers));
        } catch (Exception ignored) {
            globalTypeInfo.addSyntheticField(fieldName, toTypeInfo(typeName));
        }
    }

    private static Field getSyntheticFieldsField() throws NoSuchFieldException {
        if (syntheticFieldsField == null) {
            syntheticFieldsField = TypeInfo.class.getDeclaredField("syntheticFields");
            syntheticFieldsField.setAccessible(true);
        }
        return syntheticFieldsField;
    }
}
