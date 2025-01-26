package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.config.core.Position;
import io.github.chindeaytb.collectiontracker.mixins.AccessorGuiContainer;
import io.github.chindeaytb.collectiontracker.util.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class DummyOverlay extends GuiScreen {

    private final Position overlayPosition;

    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;
    private GuiContainer oldScreen = null;

    private int boxWidth;
    private int boxHeight;

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

        List<String> overlayLines = TextUtils.getStrings();
        if (overlayLines.isEmpty()) return;

        int padding = 5;
        int maxWidth = 0;
        for (String line : overlayLines) {
            int lineWidth = fontRenderer.getStringWidth(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }

        int textHeight = fontRenderer.FONT_HEIGHT * overlayLines.size();
        boxWidth = maxWidth + 2 * padding;
        boxHeight = textHeight + 2 * padding;

        GlStateManager.pushMatrix();
        GlStateManager.translate(overlayPosition.getX(), overlayPosition.getY(), 0);
        GlStateManager.scale(overlayPosition.getScale(), overlayPosition.getScale(), 1.0f);
        GlStateManager.translate(-overlayPosition.getX(), -overlayPosition.getY(), 0);

        drawRect(overlayPosition.getX(), overlayPosition.getY(), overlayPosition.getX() + boxWidth, overlayPosition.getY() + boxHeight, -0x7fbfbfc0);

        String overlayText = "§aMove the overlay";
        int textWidth = fontRenderer.getStringWidth(overlayText);

        int textX = overlayPosition.getX() + (boxWidth - textWidth) / 2;
        int textY = overlayPosition.getY() + textHeight / 2;
        fontRenderer.drawString(overlayText, textX, textY, 0xFFFFFF);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        drawStaticText(fontRenderer);
        GlStateManager.popMatrix();

        if (dragging) {
            overlayPosition.setPosition(mouseX - dragOffsetX, mouseY - dragOffsetY);
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
            overlayPosition.setScaling(overlayPosition.getScale() + 0.1f);
        } else if (keyCode == Keyboard.KEY_MINUS || keyCode == Keyboard.KEY_SUBTRACT) {
            overlayPosition.setScaling(Math.max(0.1f, overlayPosition.getScale() - 0.1f));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOverOverlay(mouseX, mouseY)) {
                dragging = true;
                dragOffsetX = mouseX - overlayPosition.getX();
                dragOffsetY = mouseY - overlayPosition.getY();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    private boolean isMouseOverOverlay(int mouseX, int mouseY) {
        return mouseX >= overlayPosition.getX() && mouseX <= overlayPosition.getX() + boxWidth && mouseY >= overlayPosition.getY() && mouseY <= overlayPosition.getY() + boxHeight;
    }

    @Override
    public void onGuiClosed() {
        saveOverlayPositionAndSize();
        CollectionOverlay.setVisible(true);
    }

    private void saveOverlayPositionAndSize() {
        CollectionOverlay.updateOverlayPositionAndSize(overlayPosition.getX(), overlayPosition.getY(), overlayPosition.getScale());
    }
}