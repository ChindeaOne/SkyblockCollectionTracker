package io.github.chindeaone.collectiontracker.gui.overlays;

import io.github.chindeaone.collectiontracker.mixins.AccessorGuiContainer;
import io.github.chindeaone.collectiontracker.util.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class DummyOverlay extends GuiScreen {

    private boolean draggingSingle = false;
    private boolean draggingList = false;
    private int dragOffsetX, dragOffsetY;
    private int dragOffsetListX, dragOffsetListY;
    private GuiContainer oldScreen = null;

    public DummyOverlay(GuiContainer oldScreen) {
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

        // Draw both single and list dummy overlays
        RenderUtils.INSTANCE.drawRectDummy(fontRenderer);
        RenderUtils.INSTANCE.drawRectDummyList(fontRenderer);

        // Update dragging positions
        if (draggingSingle) {
            RenderUtils.INSTANCE.getPosition().setPosition(mouseX - dragOffsetX, mouseY - dragOffsetY);
        }
        if (draggingList) {
            RenderUtils.INSTANCE.getPositionList().setPosition(mouseX - dragOffsetListX, mouseY - dragOffsetListY);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            float scaleChange = 0.05f;

            // Derive current mouse coordinates for hover detection
            int mx = Mouse.getX() * this.width / this.mc.displayWidth;
            int my = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;

            boolean overSingle = isMouseOverOverlay(mx, my);
            boolean overList = isMouseOverOverlayList(mx, my);

            if (overSingle) {
                float currentScale = RenderUtils.INSTANCE.getPosition().getScale();
                if (scrollAmount > 0) {
                    RenderUtils.INSTANCE.getPosition().setScaling(Math.min(10.0f, currentScale + scaleChange));
                } else {
                    RenderUtils.INSTANCE.getPosition().setScaling(Math.max(0.1f, currentScale - scaleChange));
                }
            } else if (overList) {
                float currentScaleList = RenderUtils.INSTANCE.getPositionList().getScale();
                if (scrollAmount > 0) {
                    RenderUtils.INSTANCE.getPositionList().setScaling(Math.min(10.0f, currentScaleList + scaleChange));
                } else {
                    RenderUtils.INSTANCE.getPositionList().setScaling(Math.max(0.1f, currentScaleList - scaleChange));
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOverOverlayList(mouseX, mouseY)) {
                draggingList = true;
                dragOffsetListX = mouseX - RenderUtils.INSTANCE.getPositionList().getX();
                dragOffsetListY = mouseY - RenderUtils.INSTANCE.getPositionList().getY();
            } else if (isMouseOverOverlay(mouseX, mouseY)) {
                draggingSingle = true;
                dragOffsetX = mouseX - RenderUtils.INSTANCE.getPosition().getX();
                dragOffsetY = mouseY - RenderUtils.INSTANCE.getPosition().getY();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSingle = false;
        draggingList = false;
    }

    private boolean isMouseOverOverlay(int mouseX, int mouseY) {
        int x = RenderUtils.INSTANCE.getPosition().getX();
        int y = RenderUtils.INSTANCE.getPosition().getY();
        int width = RenderUtils.INSTANCE.getPosition().getWidth();
        int height = RenderUtils.INSTANCE.getPosition().getHeight();
        float scale = RenderUtils.INSTANCE.getPosition().getScale();

        int right = x + Math.round(width * scale);
        int bottom = y + Math.round(height * scale);

        return mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom;
    }

    private boolean isMouseOverOverlayList(int mouseX, int mouseY) {
        int x = RenderUtils.INSTANCE.getPositionList().getX();
        int y = RenderUtils.INSTANCE.getPositionList().getY();
        int width = RenderUtils.INSTANCE.getPositionList().getWidth();
        int height = RenderUtils.INSTANCE.getPositionList().getHeight();
        float scale = RenderUtils.INSTANCE.getPositionList().getScale();

        int right = x + Math.round(width * scale);
        int bottom = y + Math.round(height * scale);

        return mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom;
    }

    @Override
    public void onGuiClosed() {
        CollectionOverlay.setVisible(true);
    }
}