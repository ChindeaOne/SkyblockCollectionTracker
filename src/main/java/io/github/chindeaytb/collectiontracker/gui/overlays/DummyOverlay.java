package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.mixins.AccessorGuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class DummyOverlay extends GuiScreen {

    private int overlayX;
    private int overlayY;
    private int overlayWidth;
    private int overlayHeight;
    private boolean dragging = false;
    private boolean resizing = false;
    private int dragOffsetX, dragOffsetY;
    private GuiContainer oldScreen = null;

    public DummyOverlay(int startX, int startY, int startWidth, int startHeight, GuiContainer oldScreen) {
        this.overlayX = startX;
        this.overlayY = startY;
        this.overlayWidth = startWidth;
        this.overlayHeight = startHeight;
        this.oldScreen = oldScreen;
    }

    @Override
    public void initGui() {
        if (CollectionOverlay.isVisible()) {
            CollectionOverlay.setVisible(false);
        }
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (CollectionOverlay.isVisible()) {
            return;
        }
        drawDefaultBackground();
        if(oldScreen!=null) {
            AccessorGuiContainer accessor = (AccessorGuiContainer) oldScreen;
            accessor.invokeDrawGuiContainerBackgroundLayer_sct(partialTicks, -1, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        String overlayText = "§aMove the overlay";

        drawRect(overlayX, overlayY, overlayX + overlayWidth, overlayY + overlayHeight, -0x7fbfbfc0);

        int textWidth = fontRenderer.getStringWidth(overlayText);
        int textHeight = fontRenderer.FONT_HEIGHT;

        int textX = overlayX + (overlayWidth - textWidth) / 2;
        int textY = overlayY + (overlayHeight - textHeight) / 2;

        fontRenderer.drawString(overlayText, textX, textY, 0xFFFFFF);

        if (dragging) {
            overlayX = mouseX - dragOffsetX;
            overlayY = mouseY - dragOffsetY;
        } else if (resizing) {
            overlayWidth = Math.max(50, mouseX - overlayX);
            overlayHeight = Math.max(20, mouseY - overlayY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOverOverlay(mouseX, mouseY)) {
                dragging = true;
                dragOffsetX = mouseX - overlayX;
                dragOffsetY = mouseY - overlayY;
            } else if (isMouseOverResizeHandle(mouseX, mouseY)) {
                resizing = true;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        resizing = false;
    }

    private boolean isMouseOverOverlay(int mouseX, int mouseY) {
        return mouseX >= overlayX && mouseX <= overlayX + overlayWidth &&
                mouseY >= overlayY && mouseY <= overlayY + overlayHeight;
    }

    private boolean isMouseOverResizeHandle(int mouseX, int mouseY) {
        int handleSize = 10;
        return mouseX >= overlayX + overlayWidth - handleSize && mouseX <= overlayX + overlayWidth &&
                mouseY >= overlayY + overlayHeight - handleSize && mouseY <= overlayY + overlayHeight;
    }

    @Override
    public void onGuiClosed() {
        saveOverlayPositionAndSize();
        CollectionOverlay.setVisible(true);
    }

    private void saveOverlayPositionAndSize() {
        CollectionOverlay.updateOverlayPositionAndSize(overlayX, overlayY, overlayWidth, overlayHeight);
    }
}
