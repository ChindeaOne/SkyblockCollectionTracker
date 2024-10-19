package io.github.chindeaytb.collectiontracker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class MoveableOverlay extends GuiScreen {

    private static int x = 4;  // Default X position
    private static int y = 150; // Default Y position
    private static boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    // Flag to track whether the fake GUI is active
    private static boolean fakeGuiActive = false;
    private static FakeGui fakeGui;


    // Static method to activate dragging
    public static void activateDragging() {
        dragging = true;  // Start dragging mode
        createFakeGui();  // Create the fake GUI at the current position of the overlay
    }

    private static void createFakeGui() {
        fakeGui = new FakeGui(x, y);
        fakeGuiActive = true; // Set the fake GUI state to active
        Minecraft.getMinecraft().displayGuiScreen(fakeGui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw the overlay if the fake GUI isn't active
        if (!fakeGuiActive) {
            drawRect(x, y, x + 150, y + 28, 0xFF000000);  // Example: Draw a black rectangle as the GUI component
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && mouseX >= x && mouseX <= x + 150 && mouseY >= y && mouseY <= y + 28) {  // Left mouse button inside the GUI bounds
            dragging = true;
            dragOffsetX = mouseX - x;
            dragOffsetY = mouseY - y;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging) {
            x = mouseX - dragOffsetX;
            y = mouseY - dragOffsetY;
            // Update fake GUI position if active
            if (fakeGuiActive) {
                fakeGui.setPosition(x, y);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            dragging = false;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            // Remove the fake GUI and restore the overlay position when ESC is pressed
            if (fakeGuiActive) {
                Minecraft.getMinecraft().displayGuiScreen(null);  // Close the fake GUI
                fakeGuiActive = false; // Set the fake GUI state to inactive
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    // Method to return current X position of the GUI
    public static int getX() {
        return x;
    }

    // Method to return current Y position of the GUI
    public static int getY() {
        return y;
    }

    // Static inner class for the fake GUI
    private static class FakeGui extends GuiScreen {
        private int fakeX;
        private int fakeY;

        public FakeGui(int x, int y) {
            this.fakeX = x;
            this.fakeY = y;
        }

        public void setPosition(int x, int y) {
            this.fakeX = x;
            this.fakeY = y;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            // Draw an empty GUI at the current position
            drawRect(fakeX, fakeY, fakeX + 150, fakeY + 28, 0x80FFFFFF); // Transparent rectangle
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return true;
        }
    }
}
