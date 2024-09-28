package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.init.HypixelConnection;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
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

    private static long lastTrackTime = 0;
    private static final int COOLDOWN_PERIOD = 30;  // Cooldown period in seconds

    // Add a cache for storing the fetched data
    private static final HashMap<String, CachedData> collectionCache = new HashMap<>();

    // Cache expiration time in seconds (e.g., 300 seconds = 5 minutes)
    private static final int CACHE_EXPIRATION = 300;

    // Method to start tracking
    public static void startTracking(ICommandSender sender) {
        long currentTime = System.currentTimeMillis();

        // Check if the cooldown period has passed
        if ((currentTime - lastTrackTime) / 1000 < COOLDOWN_PERIOD) {
            sender.addChatMessage(new ChatComponentText("§cPlease wait before starting the tracker again."));
            return;
        }

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);  // Recreate the scheduler if it was stopped
        }

        lastTrackTime = currentTime;  // Update last track time
        isTracking = true;
        sender.addChatMessage(new ChatComponentText("§aTracking " + collection + " collection"));
        logger.info("Tracking started for player with UUID: {}", UUID);
        scheduleDataFetch(sender);
    }

    // Method to stop tracking
    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();  // Stops all scheduled tasks
            sender.addChatMessage(new ChatComponentText("§cTracking has been stopped."));
            logger.info("Tracking stopped.");
            isTracking = false;
            lastTrackTime = System.currentTimeMillis();  // Set the stop time to enforce the cooldown
            previousCollection = -1;
        } else {
            sender.addChatMessage(new ChatComponentText("§cNothing is tracked currently."));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    // Method to fetch data periodically
    public static void scheduleDataFetch(ICommandSender sender) {
        scheduler.scheduleAtFixedRate(() -> fetchData(sender), 15, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 300 seconds");
    }

    // Method that fetches data from the API
    public static void fetchData(ICommandSender sender) {
        try {
            // Check if we have cached data and if it's still valid
            if (collectionCache.containsKey(UUID)) {
                CachedData cachedData = collectionCache.get(UUID);
                if (cachedData.isValid()) {
                    logger.info("Using cached data for player with UUID: {}", UUID);
                    TrackCollection.displayCollection(cachedData.getJsonData(), sender);
                    return;
                } else {
                    collectionCache.remove(UUID);  // Invalidate the cache if expired
                }
            }

            // No valid cache, fetch from API
            HypixelApiURL = getSkyBlockProfileURL(UUID, apiKey);
            logger.debug("Fetching data from URL: {}", HypixelApiURL);
            String jsonData = fetchJsonData(HypixelApiURL);

            // Store fetched data in cache
            collectionCache.put(UUID, new CachedData(jsonData, System.currentTimeMillis()));

            TrackCollection.displayCollection(jsonData, sender);
            logger.info("Data successfully fetched and displayed for player with UUID: {}", UUID);
        } catch (Exception e) {
            logger.error("Error fetching data from the Hypixel API: {}", e.getMessage(), e);
            sender.addChatMessage(new ChatComponentText("An error occurred while fetching the data. Please try again later."));
        }
    }

    // Inner class to store cached data and timestamp
    private static class CachedData {
        private final String jsonData;
        private final long timestamp;

        public CachedData(String jsonData, long timestamp) {
            this.jsonData = jsonData;
            this.timestamp = timestamp;
        }

        public String getJsonData() {
            return jsonData;
        }

        public boolean isValid() {
            // Check if cache is still valid
            return (System.currentTimeMillis() - timestamp) < CACHE_EXPIRATION * 1000;
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
