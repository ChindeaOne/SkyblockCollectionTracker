package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;

public class CollectionOverlay {

    private static String collectionName = "";
    private static String collectionAmount = "";
    private static String collectionPerHour = "";
    private static String collectionMade = "";
    private static long startTime = 0;

    private static int overlayX;
    private static int overlayY;
    private static int boxWidth = 100;
    private static int boxHeight = 28;

    private static boolean visible = true;
    private static ModConfig config ;

    @SubscribeEvent
    public void setConfig(TickEvent.ClientTickEvent event) {
        if(config != null) return;
        config = ModInitialization.configManager.getConfig();
    }

    public static void setVisible(boolean visibility) {
        visible = visibility;
    }

    public static boolean isVisible() {
        return visible;
    }

    public static void updateOverlayPositionAndSize(int newX, int newY, int newWidth, int newHeight) {
        overlayX = newX;
        overlayY = newY;
        boxWidth = newWidth;
        boxHeight = newHeight;

        if (config != null) {
            config.overlay.overlayPosition.set(newX, newY);
        }
    }

    public static void updateCollectionData(String name, String amount, String perHour, String made) {
        collectionName = name != null ? name : "";
        collectionAmount = amount != null ? amount : "";
        collectionPerHour = perHour != null ? perHour : "";
        collectionMade = made != null ? made : "";

        if (isTracking && startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!isTracking || !visible || config == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

        List<String> overlayLines = getStrings();

        overlayX = config.overlay.overlayPosition.getX();
        overlayY = config.overlay.overlayPosition.getY();

        if (overlayLines.isEmpty()) return;

        int maxWidth = overlayLines.stream()
                .mapToInt(fontRenderer::getStringWidth)
                .max()
                .orElse(0);

        boxWidth = maxWidth + 10;
        boxHeight = overlayLines.size() * 12 + 6;

        Gui.drawRect(overlayX, overlayY, overlayX + boxWidth, overlayY + boxHeight, 0x10000000);

        float scale = 0.85f;
        int lineHeight = (int) (12 * scale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        int textX = (int) ((overlayX + 1) / scale);
        int textY = (int) ((overlayY + 2) / scale);

        for (String line : overlayLines) {
            fontRenderer.drawString(line, textX, textY, 0xFFFFFF);
            textY += lineHeight;
        }

        GlStateManager.popMatrix();
    }

    private static @NotNull List<String> getStrings() {
        List<String> overlayLines = new ArrayList<>();
        Overlay overlay = config.overlay;

        for (int id : overlay.statsText) {
            switch (id) {
                case 0:
                    if (!collectionName.isEmpty() && !collectionAmount.isEmpty()) {
                        overlayLines.add("§a" + collectionName + " collection §f> " + collectionAmount);
                    }
                    break;
                case 1:
                    if (!collectionName.isEmpty() && !collectionMade.isEmpty()) {
                        overlayLines.add("§a" + collectionName + " collection made §f> " + collectionMade);
                    }
                    break;
                case 2:
                    if (!collectionPerHour.isEmpty()) {
                        overlayLines.add("§aColl/h §f> " + collectionPerHour);
                    }
                    break;
                case 3:
                    if (startTime != 0) {
                        overlayLines.add("§aUptime §f> " + getUptime());
                    }
                    break;
            }
        }
        return overlayLines;
    }

    private static String getUptime() {
        if (startTime == 0) return "00:00:00";

        long uptime = (System.currentTimeMillis() - startTime) / 1000;
        long hours = uptime / 3600;
        long minutes = (uptime % 3600) / 60;
        long seconds = uptime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void stopTracking() {
        startTime = 0;
        updateCollectionData(null, null, null, null);
        setVisible(false);
    }

    public static int getOverlayWidth() {
        return boxWidth;
    }

    public static int getOverlayHeight() {
        return boxHeight;
    }
}
