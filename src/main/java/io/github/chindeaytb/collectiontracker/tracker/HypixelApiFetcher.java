package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.init.HypixelConnection;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;
import static io.github.chindeaytb.collectiontracker.init.HypixelConnection.apiKey;
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.previousCollection;

public class HypixelApiFetcher {

    public static String HypixelApiURL = "";
    public static final String BASE_URL = "https://api.hypixel.net/v2/skyblock/profiles";

    // Use the logger from HypixelConnection class
    private static final Logger logger = HypixelConnection.logger;

    // Declare the scheduler
    private static ScheduledExecutorService scheduler;
    public static boolean isTracking = false;

    // Method to start tracking
    public static void startTracking(ICommandSender sender) {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);  // Recreate the scheduler if it was stopped
        }

        isTracking = true;
        logger.info("Tracking started for player with UUID: {}", UUID);
        scheduleDataFetch(sender);
    }

    // Method to stop tracking
    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();  // Stops all scheduled tasks
            sender.addChatMessage(new ChatComponentText("§cTracking has been stopped."));
            logger.info("Tracking stopped for player with UUID: {}", UUID);
            isTracking = false;
            previousCollection = -1;
        } else {
            sender.addChatMessage(new ChatComponentText("§cNothing is tracked currently."));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    // Method to fetch data periodically
    public static void scheduleDataFetch(ICommandSender sender) {
        scheduler.scheduleAtFixedRate(() -> fetchData(sender), 15, 300, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 300 seconds for player with UUID: {}", UUID);
    }

    // Method that fetches data from the API
    public static void fetchData(ICommandSender sender) {
        try {
            HypixelApiURL = getSkyBlockProfileURL(UUID, apiKey);
            logger.debug("Fetching data from URL: {}", HypixelApiURL);
            String jsonData = fetchJsonData(HypixelApiURL);
            TrackCollection.displayCollection(jsonData, sender);
            logger.info("Data successfully fetched and displayed for player with UUID: {}", UUID);
        } catch (Exception e) {
            // Log the error with detailed information
            logger.error("Error fetching data from the Hypixel API: {}", e.getMessage(), e);

            // Notify the sender (player) about the error
            sender.addChatMessage(new ChatComponentText("An error occurred while fetching the data. Please try again later."));

            // Optionally, provide more details for debugging in-game (only for operators or admins)
            if (sender.canCommandSenderUseCommand(4, "")) {
                sender.addChatMessage(new ChatComponentText("Error: " + e.getMessage()));
            }
        }
    }

    public static String getSkyBlockProfileURL(String uuid, String apiKey) {
        return BASE_URL + "?uuid=" + uuid + "&key=" + apiKey;
    }

    public static String fetchJsonData(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }
}
