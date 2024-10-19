package io.github.chindeaytb.collectiontracker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;

public class CollectionOverlay {

    // Default strings to empty or placeholders
    private static String collectionName = "";
    private static String collectionAmount = "";
    private static String collectionPerHour = "";
    private static long startTime = 0;


    public CollectionOverlay() {
        // Register this class to the event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Method to update the collection data (called when tracking starts)
    public static void updateCollectionData(String name, String amount, String perHour) {
        collectionName = name != null ? name : "";  // Default to empty if null
        collectionAmount = amount != null ? amount : "";
        collectionPerHour = perHour != null ? perHour : "";


        // Reset start time to the current time when tracking starts
        if (isTracking && startTime == 0) { // Only reset when starting a new tracking session
            startTime = System.currentTimeMillis();
        }
    }

    // Event listener for rendering the overlay
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (isTracking) {

            int x = MoveableOverlay.getX();  // Get the updated X position
            int y = MoveableOverlay.getY();  // Get the updated Y position

            Minecraft mc = Minecraft.getMinecraft();

            FontRenderer fontRenderer = mc.fontRendererObj; // Default Minecraft font renderer

            // Prepare the texts with color codes embedded directly in the string
            String collectionText = (collectionName.isEmpty() ? "" : "§a" + collectionName + " collection ") + (collectionAmount.isEmpty() ? "" : "§f> " + collectionAmount);
            String perHourText = (collectionPerHour.isEmpty() ? "" : "§aColl/h " + "§f> " + collectionPerHour);
            String uptimeText = (collectionName.isEmpty() ? "" : "§aUptime " + "§f> " + getUptime());

            // Determine the maximum width of the text
            int maxWidth = Math.max(fontRenderer.getStringWidth(collectionText),
                    Math.max(fontRenderer.getStringWidth(perHourText), fontRenderer.getStringWidth(uptimeText)));

            // Set box dimensions based on text content
            int boxHeight = 28;  // Height of the box remains fixed
            int boxWidth = maxWidth + 10;  // Width depends on the longest string with some padding

            // Draw a semi-transparent background for the box at the new position
            Gui.drawRect(x, y, x + boxWidth, y + boxHeight, 0x10000000); // Semi-transparent background

            // Scaling text to 85%
            float scale = 0.85f;

            // Vertical text centering calculations
            int lineHeight = (int) (12 * scale);  // Approximate height of each line of text scaled to 85%
            int totalTextHeight = 3 * lineHeight; // 3 lines of text
            int startY = y + (boxHeight - totalTextHeight) / 2; // Center the text vertically in the box

            // Render the text with scaling
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale); // Apply 85% scaling

            // Adjust textX and textY for the scaling factor
            int textX = (int) ((x + 1) / scale); // Scale position for 85%
            int textY = (int) ((startY + 2) / scale);     // Scale position for 85%

            // Render each full string with its embedded color codes
            fontRenderer.drawString(collectionText, textX, textY, 0xFFFFFF); // Draw the full collectionText
            textY += lineHeight; // Move to next line

            fontRenderer.drawString(perHourText, textX, textY, 0xFFFFFF); // Draw the full perHourText
            textY += lineHeight; // Move to the next line

            fontRenderer.drawString(uptimeText, textX, textY, 0xFFFFFF); // Draw the full uptimeText

            GlStateManager.popMatrix(); // Restore previous OpenGL state
        }
    }


    // Method to calculate and return the uptime as a formatted string
    private static String getUptime() {
        if (startTime == 0) {
            return "00:00:00"; // If tracking hasn't started, show 00:00:00
        }

        long uptime = (System.currentTimeMillis() - startTime) / 1000; // Convert to seconds
        long hours = uptime / 3600;
        long minutes = (uptime % 3600) / 60;
        long seconds = uptime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Method to reset tracking data when stopping tracking
    public static void stopTracking() {
        startTime = 0; // Reset the start time to 0 when tracking stops
        updateCollectionData(null, null, null);
    }
}
