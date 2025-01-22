package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import io.github.chindeaytb.collectiontracker.config.core.Position;
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

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.*;

public class CollectionOverlay {

    private static String collectionName = "";
    private static String collectionAmount = "";
    private static String collectionPerHour = "";
    private static String collectionMade = "";
    private static String moneyPerHour = "";
    private static int overlayX;
    private static int overlayY;
    private static int boxWidth;
    private static int boxHeight;
    private static float scaleX;
    private static float scaleY;

    private static boolean visible = true;
    private static ModConfig config;

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

    public static void updateCollectionData(String name, String amount, String perHour, String made, String money) {
        collectionName = name != null ? name : "";
        collectionAmount = amount != null ? amount : "";
        collectionPerHour = perHour != null ? perHour : "";
        collectionMade = made != null ? made : "";
        moneyPerHour = money != null ? money : "";

        if(startTime == 0){
            startTime = System.currentTimeMillis();
        }
    }

    public static @NotNull List<String> getStrings() {
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
                    if (!moneyPerHour.isEmpty()) {
                        overlayLines.add("§a$/h (NPC) §f> " + moneyPerHour);
                    }
                    break;
                case 4:
                    if (startTime != 0) {
                        overlayLines.add("§aUptime §f> " + getUptime());
                    }
                    break;
            }
        }
        return overlayLines;
    }

    public static void stopTracking() {
        updateCollectionData(null, null, null, null, null);
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

        List<String> overlayLines = getStrings();
        if (overlayLines.isEmpty()) return;

        Position position = config.overlay.overlayPosition;
        overlayX = position.getX();
        overlayY = position.getY();
        boxWidth = position.getWidth();
        boxHeight = position.getHeight();
        scaleX = position.getScaleX();
        scaleY = position.getScaleY();

        Gui.drawRect(overlayX, overlayY, overlayX + boxWidth, overlayY + boxHeight, 0x10000000);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleX, scaleY, 1.0f);

        int scaledOverlayX = (int) (overlayX / scaleX);
        int scaledOverlayY = (int) (overlayY / scaleY);
        int textY = scaledOverlayY + 2;

        for (String line : overlayLines) {
            fontRenderer.drawString(line, scaledOverlayX + 1, textY, 0xFFFFFF);
            textY += fontRenderer.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
    }
}
