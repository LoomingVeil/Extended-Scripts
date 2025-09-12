package com.veil.extendedscripts.commands;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;

public interface IInventoryEffectRenderer {
    void onActionPerformed(GuiButton button);
}
