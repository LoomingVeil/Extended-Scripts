package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedAPI;
import com.veil.extendedscripts.extendedapi.IFieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.field.FieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.hover.TokenHoverInfo;
import noppes.npcs.client.gui.util.script.interpreter.js_parser.JSFieldInfo;
import noppes.npcs.client.gui.util.script.interpreter.token.Token;
import noppes.npcs.client.gui.util.script.interpreter.token.TokenType;
import noppes.npcs.client.gui.util.script.interpreter.type.TypeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TokenHoverInfo.class, remap = false)
public class MixinTokenHoverInfo {
    @Inject(method = "fromToken", at = @At(value = "HEAD"), cancellable = true)
    private static void mixinFromToken(Token token, CallbackInfoReturnable<TokenHoverInfo> cir) {
        System.out.println(token+" "+token.getText());
        try {
            System.out.println("2nd Previous: " + token.prevOnLine().prevOnLine().getText());
            if (token.prevOnLine().prevOnLine().getText().equals("CustomEffect")) {
                System.out.println("A certain case");
                token.setType(TokenType.GLOBAL_FIELD);
                FieldInfo fieldInfo = FieldInfo.fromJSField(new JSFieldInfo("Test", "number", true), TypeInfo.NUMBER);
                IFieldInfo mixinFieldInfo = (IFieldInfo) (Object) fieldInfo;
                mixinFieldInfo.setDocumentation("Some testing text\nAnother line");
                mixinFieldInfo.setResolved(true);
                token.setFieldInfo(fieldInfo);
            }
        } catch (Exception e) {
            System.out.println("Failed to view 2nd previous token");
        }
    }
}
