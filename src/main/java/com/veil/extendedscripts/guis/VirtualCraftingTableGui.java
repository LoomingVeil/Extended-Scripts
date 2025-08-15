package com.veil.extendedscripts.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation; // For textures
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11; // For OpenGL drawing

public class VirtualCraftingTableGui extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
    // This texture should look similar to vanilla's crafting table GUI.
    // Dimensions: 256x256 (or 512x512 depending on scale)
    // The vanilla crafting table GUI is 176x166 pixels.
    // The background part of this texture should be transparent in the slots.

    public VirtualCraftingTableGui(InventoryPlayer playerInventory, World world) {
        super(new VirtualCraftingTableContainer(playerInventory, world));
    }

    // Draw the background layer (texture, static elements)
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Set color to white for texture drawing
        this.mc.getTextureManager().bindTexture(TEXTURE); // Bind your custom texture

        // Calculate GUI position (centered)
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        // Draw the textured rectangle (your background image)
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    // Draw the foreground layer (text, item tooltips, etc.)
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Draw the title of your GUI
        String title = "Crafting"; // Or a localized string
        this.fontRendererObj.drawString(title, this.xSize / 2 - this.fontRendererObj.getStringWidth(title) / 2, 6, 4210752); // Dark gray text

        // Draw player inventory label
        this.fontRendererObj.drawString(this.mc.thePlayer.inventory.getInventoryName(), 8, this.ySize - 96 + 2, 4210752); // Dark gray text
    }

    // Called when the screen is opened or closed, or resize
    @Override
    public void initGui() {
        super.initGui();
        // You can add buttons or other elements here if needed
    }
}
