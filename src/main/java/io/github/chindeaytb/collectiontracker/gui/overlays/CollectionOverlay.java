package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.core.Position;
import io.github.chindeaytb.collectiontracker.gui.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static io.github.chindeaytb.collectiontracker.gui.TextUtils.updateStats;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.*;

public class CollectionOverlay {

    private static int overlayX;
    private static int overlayY;
    private static int boxWidth;
    private static int boxHeight;
    private static float scaleX;
    private static float scaleY;

    private static boolean visible = true;
    public static ModConfig config;

    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visibility) {
        visible = visibility;
    }

    public static void updateOverlayPositionAndSize(int newX, int newY, int newWidth, int newHeight, float newScaleX, float newScaleY) {
        overlayX = newX;
        overlayY = newY;
        boxWidth = newWidth;
        boxHeight = newHeight;
        scaleX = newScaleX;
        scaleY = newScaleY;

        if (config != null) {
            config.overlay.overlayPosition.setPosition(newX, newY);
            config.overlay.overlayPosition.setDimensions(newWidth, newHeight);
            config.overlay.overlayPosition.setScaling(newScaleX, newScaleY);
        }
    }

    public static void stopTracking() {
        updateStats();
        setVisible(false);
    }

    @SubscribeEvent
    public void setConfig(TickEvent.ClientTickEvent event) {
        if (config != null) return;
        config = ModInitialization.configManager.getConfig();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!isTracking || !visible || config == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

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
        boxWidth += maxWidth + 2 * padding;
        boxHeight += textHeight + 2 * padding;

        Position position = config.overlay.overlayPosition;
        setPositions(position);

        overlayX = position.getX();
        overlayY = position.getY();

        Gui.drawRect(overlayX, overlayY, overlayX + boxWidth, overlayY + boxHeight, 0x10000000);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleX, scaleY, 1.0f);

        int scaledOverlayX = (int) (overlayX / scaleX) + padding;
        int scaledOverlayY = (int) (overlayY / scaleY) + padding;

        if (startTime != 0) {
            for (String line : overlayLines) {
                fontRenderer.drawString(line, scaledOverlayX, scaledOverlayY, 0xFFFFFF);
                scaledOverlayY += fontRenderer.FONT_HEIGHT;
            }
        }
        GlStateManager.popMatrix();
    }

    private void setPositions(Position position){
        overlayX = position.getX();
        overlayY = position.getY();
        boxWidth = position.getWidth();
        boxHeight = position.getHeight();
        scaleX = position.getScaleX();
        scaleY = position.getScaleY();
    }
}
