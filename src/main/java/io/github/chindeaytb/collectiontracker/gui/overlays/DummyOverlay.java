package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.config.core.Position;
import io.github.chindeaytb.collectiontracker.mixins.AccessorGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class DummyOverlay extends GuiScreen {

    private final Position overlayPosition;

    private boolean dragging = false;
    private boolean resizing = false;
    private int dragOffsetX, dragOffsetY;
    private GuiContainer oldScreen = null;

    public DummyOverlay(Position position, GuiContainer oldScreen) {
        this.overlayPosition = position;
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
        if (oldScreen != null) {
            AccessorGuiContainer accessor = (AccessorGuiContainer) oldScreen;
            accessor.invokeDrawGuiContainerBackgroundLayer_sct(partialTicks, -1, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        int effectiveWidth = Math.max(overlayPosition.getWidth(), 10);
        int effectiveHeight = overlayPosition.getHeight();

        overlayPosition.setDimensions(effectiveWidth, effectiveHeight);

        GlStateManager.pushMatrix();
        GlStateManager.translate(overlayPosition.getX(), overlayPosition.getY(), 0);
        GlStateManager.scale(overlayPosition.getScaleX(), overlayPosition.getScaleY(), 1.0f);
        GlStateManager.translate(-overlayPosition.getX(), -overlayPosition.getY(), 0);

        drawRect(overlayPosition.getX(), overlayPosition.getY(), overlayPosition.getX() + effectiveWidth, overlayPosition.getY() + effectiveHeight, -0x7fbfbfc0);

        String overlayText = "§aMove the overlay";
        int textWidth = fontRenderer.getStringWidth(overlayText);
        int textHeight = fontRenderer.FONT_HEIGHT;

        int textX = overlayPosition.getX() + (effectiveWidth - textWidth) / 2;
        int textY = overlayPosition.getY() + (effectiveHeight - textHeight) / 2;
        fontRenderer.drawString(overlayText, textX, textY, 0xFFFFFF);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        drawStaticText(fontRenderer);
        GlStateManager.popMatrix();

        if (dragging) {
            overlayPosition.setPosition(mouseX - dragOffsetX, mouseY - dragOffsetY);
        } else if (resizing) {
            overlayPosition.setDimensions(Math.max(50, mouseX - overlayPosition.getX()), Math.max(20, mouseY - overlayPosition.getY()));
        }
    }

    private void drawStaticText(FontRenderer fontRenderer) {
        int screenWidth = width;
        int textWidth = fontRenderer.getStringWidth("§aUse -/+ keys to resize the overlay");

        int textX = (screenWidth - textWidth) / 2;
        int textY = 10;

        fontRenderer.drawString("§aUse -/+ keys to resize the overlay", textX, textY, 0xFFFFFF);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_EQUALS || keyCode == Keyboard.KEY_ADD) {
            overlayPosition.setScaling(overlayPosition.getScaleX() + 0.1f, overlayPosition.getScaleY() + 0.1f);
        } else if (keyCode == Keyboard.KEY_MINUS || keyCode == Keyboard.KEY_SUBTRACT) {
            overlayPosition.setScaling(Math.max(0.1f, overlayPosition.getScaleX() - 0.1f), Math.max(0.1f, overlayPosition.getScaleY() - 0.1f));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOverOverlay(mouseX, mouseY)) {
                dragging = true;
                dragOffsetX = mouseX - overlayPosition.getX();
                dragOffsetY = mouseY - overlayPosition.getY();
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
        return mouseX >= overlayPosition.getX() && mouseX <= overlayPosition.getX() + overlayPosition.getWidth() && mouseY >= overlayPosition.getY() && mouseY <= overlayPosition.getY() + overlayPosition.getHeight();
    }

    private boolean isMouseOverResizeHandle(int mouseX, int mouseY) {
        int handleSize = 10;
        return mouseX >= overlayPosition.getX() + overlayPosition.getWidth() - handleSize && mouseX <= overlayPosition.getX() + overlayPosition.getWidth() && mouseY >= overlayPosition.getY() + overlayPosition.getHeight() - handleSize && mouseY <= overlayPosition.getY() + overlayPosition.getHeight();
    }

    @Override
    public void onGuiClosed() {
        saveOverlayPositionAndSize();
        CollectionOverlay.setVisible(true);
    }

    private void saveOverlayPositionAndSize() {
        CollectionOverlay.updateOverlayPositionAndSize(overlayPosition.getX(), overlayPosition.getY(), overlayPosition.getWidth(), overlayPosition.getHeight(), overlayPosition.getScaleX(), overlayPosition.getScaleY());
    }
}