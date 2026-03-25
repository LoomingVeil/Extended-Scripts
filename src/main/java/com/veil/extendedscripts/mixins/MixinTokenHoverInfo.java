package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.scripting.ScriptGlobalDescriptor;
import com.veil.extendedscripts.scripting.ScriptGlobalRegistry;
import noppes.npcs.client.gui.util.script.interpreter.field.FieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.hover.TokenHoverInfo;
import noppes.npcs.client.gui.util.script.interpreter.token.Token;
import noppes.npcs.client.gui.util.script.interpreter.token.TokenType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Modifier;

import static com.veil.extendedscripts.scripting.ScriptGlobalRegistry.toTypeInfo;

@Mixin(value = TokenHoverInfo.class, remap = false)
public class MixinTokenHoverInfo {
    @Inject(method = "fromToken", at = @At(value = "HEAD"))
    private static void extendedscripts$dynamicGlobalHover(Token token, CallbackInfoReturnable<TokenHoverInfo> cir) {
        if (token == null) {
            return;
        }

        Token previous = token.prevOnLine();
        if (previous == null || !".".equals(previous.getText())) {
            return;
        }

        Token owner = previous.prevOnLine();
        if (owner == null) {
            return;
        }

        ScriptGlobalDescriptor descriptor = ScriptGlobalRegistry.findByName(owner.getText());
        if (descriptor == null) {
            return;
        }

        ScriptGlobalDescriptor.MemberEntry member = descriptor.resolveMember(token.getText());
        if (member == null) {
            return;
        }

        FieldInfo fieldInfo = FieldInfo.external(
            token.getText(),
            toTypeInfo(member.typeName),
            member.documentation,
            Modifier.PUBLIC | Modifier.STATIC
        );
        token.setType(TokenType.GLOBAL_FIELD);
        token.setFieldInfo(fieldInfo);
    }
}
