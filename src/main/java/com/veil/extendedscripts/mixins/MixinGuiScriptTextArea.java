package com.veil.extendedscripts.mixins;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.util.GuiScriptTextArea;
import noppes.npcs.client.gui.util.TextContainer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static net.minecraft.client.gui.GuiScreen.isCtrlKeyDown;

@Mixin(value={GuiScriptTextArea.class})
public abstract class MixinGuiScriptTextArea {
    @Shadow
    public boolean active;
    @Shadow
    public String field_146216_j; // text
    @Shadow
    private TextContainer container;
    @Shadow
    private int startSelection;
    @Shadow
    private int endSelection;
    @Shadow
    private int cursorPosition;
    /*@Shadow
    public List<GuiScriptTextArea.UndoData> undoList = new ArrayList();
    @Shadow
    public List<GuiScriptTextArea.UndoData> redoList = new ArrayList(); */
    @Shadow
    public boolean undoing;



    @Shadow
    public abstract boolean isEnabled();

    @Shadow
    protected abstract boolean isKeyComboCtrlA(int keyID);

    @Shadow
    protected abstract void setCursor(int i, boolean select);

    @Shadow
    protected abstract int cursorUp();

    @Shadow
    protected abstract int cursorDown();

    @Shadow
    public abstract String getSelectionAfterText();

    @Shadow
    public abstract String getSelectionBeforeText();

    @Shadow
    protected abstract boolean isKeyComboCtrlBackspace(int keyID);

    @Shadow
    public abstract void func_146180_a(String text);

    @Shadow
    protected abstract boolean isKeyComboCtrlX(int keyID);

    @Shadow
    protected abstract boolean isKeyComboCtrlC(int keyID);

    @Shadow
    protected abstract boolean isKeyComboCtrlV(int keyID);

    @Shadow
    protected abstract void addText(String s);

    /**
     * @author Veil
     * @reason To fix ctrl + backspace not working the same as it does in a typical IDE.
     */
   /* @Overwrite
    public boolean func_146201_a(char c, int i) {
        if (!active)
            return false;

        if (this.isKeyComboCtrlA(i)) {
            startSelection = cursorPosition = 0;
            endSelection = field_146216_j.length();
            return true;
        }

        if (!this.isEnabled())
            return false;

        String original = field_146216_j;
        if (i == Keyboard.KEY_LEFT) {
            int j = 1;
            if (isCtrlKeyDown()) {
                Matcher m = container.regexWord.matcher(field_146216_j.substring(0, cursorPosition));
                while (m.find()) {
                    if (m.start() == m.end())
                        continue;
                    j = cursorPosition - m.start();
                }
            }
            this.setCursor(cursorPosition - j, GuiScreen.isShiftKeyDown());
            return true;
        }
        if (i == Keyboard.KEY_RIGHT) {
            int j = 1;
            if (isCtrlKeyDown()) {
                Matcher m = container.regexWord.matcher(field_146216_j.substring(cursorPosition));
                if (m.find() && m.start() > 0 || m.find()) {
                    j = m.start();
                }
            }
            setCursor(cursorPosition + j, GuiScreen.isShiftKeyDown());
            return true;
        }
        if (i == Keyboard.KEY_UP) {
            setCursor(this.cursorUp(), GuiScreen.isShiftKeyDown());
            return true;
        }
        if (i == Keyboard.KEY_DOWN) {
            setCursor(this.cursorDown(), GuiScreen.isShiftKeyDown());
            return true;
        }
        if (i == Keyboard.KEY_DELETE) {
            String s = this.getSelectionAfterText();
            if (!s.isEmpty() && startSelection == endSelection)
                s = s.substring(1);
            this.func_146180_a(this.getSelectionBeforeText() + s); // func_146180_a = setText()
            endSelection = cursorPosition = startSelection;
            return true;
        }
        if (this.isKeyComboCtrlBackspace(i)) {
            String s = getSelectionBeforeText();
            if (startSelection > 0 && startSelection == endSelection) {
                int nearestCondition = cursorPosition;
                int g;
                boolean cursorInWhitespace = Character.isWhitespace(s.charAt(cursorPosition - 1));
                if (cursorInWhitespace) {
                    // Find the nearest word if we are starting in whitespace
                    for (g = cursorPosition - 1; g >= 0; g--) {
                        char currentChar = s.charAt(g);
                        if (!Character.isWhitespace(currentChar)) {
                            nearestCondition = g;
                            break;
                        }
                        if (g == 0) {
                            nearestCondition = 0;
                        }
                    }
                } else {
                    // Find the nearest blank space or new line
                    for (g = cursorPosition - 1; g >= 0; g--) {
                        char currentChar = s.charAt(g);
                        if (Character.isWhitespace(currentChar) || currentChar == '\n') {
                            nearestCondition = g;
                            break;
                        }
                        if (g == 0) {
                            nearestCondition = 0;
                        }
                    }
                }

                // Remove all text to the left up to the nearest boundary
                s = s.substring(0, nearestCondition);
                startSelection -= (cursorPosition - nearestCondition);
            }
            this.func_146180_a(s + getSelectionAfterText());
            endSelection = cursorPosition = startSelection;
            return true;
        }
        if (i == Keyboard.KEY_BACK) {
            String s = getSelectionBeforeText();
            if (startSelection > 0 && startSelection == endSelection) {
                s = s.substring(0, s.length() - 1);
                startSelection--;
            }
            this.func_146180_a(s + getSelectionAfterText());
            endSelection = cursorPosition = startSelection;
            return true;
        }
        if (this.isKeyComboCtrlX(i)) {
            if (startSelection != endSelection) {
                NoppesStringUtils.setClipboardContents(field_146216_j.substring(startSelection, endSelection));
                String s = getSelectionBeforeText();
                this.func_146180_a(s + getSelectionAfterText());
                endSelection = startSelection = cursorPosition = s.length();

            }
            return true;
        }
        if (this.isKeyComboCtrlC(i)) {
            if (startSelection != endSelection) {
                NoppesStringUtils.setClipboardContents(field_146216_j.substring(startSelection, endSelection));
            }
            return true;
        }
        if (this.isKeyComboCtrlV(i)) {
            this.addText(NoppesStringUtils.getClipboardContents());
            return true;
        }
        if (i == Keyboard.KEY_Z && isCtrlKeyDown()) {
            GuiScriptTextArea target = (GuiScriptTextArea)(Object)this;

            if (undoList.isEmpty())
                return false;
            undoing = true;
            redoList.add(new GuiScriptTextArea.UndoData(this.text, this.cursorPosition));
            GuiScriptTextArea.UndoData data = undoList.remove(undoList.size() - 1);
            setText(data.text);
            endSelection = startSelection = cursorPosition = data.cursorPosition;
            undoing = false;
            return true;
        }
        if (i == Keyboard.KEY_Y && isCtrlKeyDown()) {
            if (redoList.isEmpty())
                return false;
            undoing = true;
            undoList.add(new GuiScriptTextArea.UndoData(this.field_146216_j, this.cursorPosition));
            GuiScriptTextArea.UndoData data = redoList.remove(redoList.size() - 1);
            setText(data.text);
            endSelection = startSelection = cursorPosition = data.cursorPosition;
            undoing = false;
            return true;
        }
        if (i == Keyboard.KEY_TAB) {
            addText("    ");
        }
        if (i == Keyboard.KEY_RETURN) {
            addText(Character.toString('\n') + getIndentCurrentLine());
        }
        if (ChatAllowedCharacters.isAllowedCharacter(c)) {
            addText(Character.toString(c));
        }

        return true;
    }*/
}
