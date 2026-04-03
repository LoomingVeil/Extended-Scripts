package com.veil.extendedscripts.mixins;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = GuiNPCInterface.class, remap = false)
public abstract class MixinGuiNPCInterfaceKeyboardNav extends GuiScreen {
    @Shadow
    protected HashMap<Integer, GuiNpcButton> buttons;
    @Shadow
    protected HashMap<Integer, GuiMenuTopButton> topbuttons;
    @Shadow
    protected HashMap<Integer, GuiNpcTextField> textfields;
    @Unique
    private Object extendedscripts$focusedElement;
    @Unique
    private boolean extendedscripts$keyboardFocusMode;
    @Unique
    private GuiButton extendedscripts$lastHighlightedButton;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void extendedscripts$resetFocusOnInit(CallbackInfo ci) {
        extendedscripts$focusedElement = null;
        extendedscripts$keyboardFocusMode = false;
        extendedscripts$lastHighlightedButton = null;
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void extendedscripts$keyboardButtonNavigation(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == Keyboard.KEY_TAB) {
            extendedscripts$keyboardFocusMode = true;
            extendedscripts$cycleButtonFocus(!GuiScreen.isShiftKeyDown());
            ci.cancel();
            return;
        }

        if (keyCode != Keyboard.KEY_SPACE) {
            return;
        }

        Object focusedButton = extendedscripts$getFocusedButton();
        if (focusedButton instanceof GuiButton) {
            GuiButton button = (GuiButton) focusedButton;
            if (!button.enabled || !button.visible) {
                return;
            }

            button.func_146113_a(this.mc.getSoundHandler());
            this.actionPerformed(button);
            ci.cancel();
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void extendedscripts$disableKeyboardFocusOnMouseClick(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        extendedscripts$keyboardFocusMode = false;
        extendedscripts$lastHighlightedButton = null;
        extendedscripts$focusedElement = null;
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void extendedscripts$drawKeyboardFocusedButton(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (!extendedscripts$keyboardFocusMode) {
            return;
        }

        Object focusedElement = extendedscripts$getFocusedButton();
        if (focusedElement == null) {
            extendedscripts$lastHighlightedButton = null;
            return;
        }

        if (focusedElement instanceof GuiButton) {
            GuiButton button = (GuiButton) focusedElement;
            if (!button.visible) {
                return;
            }
            extendedscripts$lastHighlightedButton = button;
            button.drawButton(this.mc, button.xPosition + 1, button.yPosition + 1);
        } else {
            extendedscripts$lastHighlightedButton = null;
        }
    }

    @Unique
    private void extendedscripts$cycleButtonFocus(boolean forward) {
        List<Object> focusable = extendedscripts$getFocusableButtons();
        if (focusable.isEmpty()) {
            extendedscripts$focusedElement = null;
            return;
        }

        int currentIndex = focusable.indexOf(extendedscripts$focusedElement);
        int size = focusable.size();
        int nextIndex;

        if (currentIndex < 0) {
            nextIndex = forward ? 0 : size - 1;
        } else if (forward) {
            nextIndex = (currentIndex + 1) % size;
        } else {
            nextIndex = (currentIndex - 1 + size) % size;
        }

        extendedscripts$focusedElement = focusable.get(nextIndex);
        extendedscripts$applyFocusedElementState();
    }

    @Unique
    private Object extendedscripts$getFocusedButton() {
        List<Object> focusable = extendedscripts$getFocusableButtons();
        if (focusable.isEmpty()) {
            extendedscripts$focusedElement = null;
            return null;
        }
        if (extendedscripts$focusedElement == null || !focusable.contains(extendedscripts$focusedElement)) {
            return null;
        }
        return extendedscripts$focusedElement;
    }

    @Unique
    private List<Object> extendedscripts$getFocusableButtons() {
        List<Object> focusable = new ArrayList<Object>();
        if (this.buttons == null) {
            return focusable;
        }

        for (Object element : this.buttons.values()) {
            if (!(element instanceof GuiButton)) {
                continue;
            }
            GuiButton button = (GuiButton) element;

            if (button.visible && button.enabled) {
                focusable.add(button);
            }
        }

        if (this.textfields != null) {
            for (Object element : this.textfields.values()) {
                if (!(element instanceof GuiNpcTextField)) {
                    continue;
                }
                GuiNpcTextField field = (GuiNpcTextField) element;

                if (field.enabled) {
                    focusable.add(field);
                }
            }
        }

        focusable.sort(new Comparator<Object>() {
            @Override
            public int compare(Object a, Object b) {
                if (a instanceof GuiButton && b instanceof GuiButton) {
                    GuiButton butA = (GuiButton) a;
                    GuiButton butB = (GuiButton) b;
                    int result = butA.yPosition - butB.yPosition;
                    if (result != 0) return result;
                    return butA.xPosition - butB.xPosition;
                } else if (a instanceof GuiNpcTextField && b instanceof GuiNpcTextField) {
                    GuiNpcTextField fieldA = (GuiNpcTextField) a;
                    GuiNpcTextField fieldB = (GuiNpcTextField) b;
                    int result = fieldA.yPosition - fieldB.yPosition;
                    if (result != 0) return result;
                    return fieldA.xPosition - fieldB.xPosition;
                } else if (a instanceof GuiNpcTextField && b instanceof GuiButton) {
                    GuiNpcTextField fieldA = (GuiNpcTextField) a;
                    GuiButton butB = (GuiButton) b;
                    int result = fieldA.yPosition - butB.yPosition;
                    if (result != 0) return result;
                    return fieldA.xPosition - butB.xPosition;
                } else if (a instanceof GuiButton && b instanceof GuiNpcTextField) {
                    GuiButton butA = (GuiButton) a;
                    GuiNpcTextField fieldB = (GuiNpcTextField) b;
                    int result = butA.yPosition - fieldB.yPosition;
                    if (result != 0) return result;
                    return butA.xPosition - fieldB.xPosition;
                }
                return 0;
            }
        });

        return focusable;
    }

    @Unique
    private void extendedscripts$applyFocusedElementState() {
        if (this.textfields == null || this.textfields.isEmpty()) {
            return;
        }

        for (GuiNpcTextField field : this.textfields.values()) {
            if (field == null) {
                continue;
            }
            field.setCursorPositionEnd();
            field.setFocused(field == extendedscripts$focusedElement);
        }

        if (extendedscripts$focusedElement instanceof GuiNpcTextField) {
            extendedscripts$selectAllText((GuiNpcTextField) extendedscripts$focusedElement);
        }
    }

    @Unique
    private void extendedscripts$selectAllText(GuiNpcTextField field) {
        String text = field.getText();
        if (text == null) {
            return;
        }
        field.setCursorPositionEnd();
        field.setSelectionPos(0);
    }
}
