package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.gui.CollectionOverlay;
import io.github.chindeaytb.collectiontracker.init.HypixelConnection;
import io.github.chindeaytb.collectiontracker.init.PlayerUUID;
import io.github.chindeaytb.collectiontracker.token.TokenManager;
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
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.previousCollection;

public class HypixelApiFetcher {

    public static final String BASE_URL = "https://skyblockcollections.com/hypixelapi";
    private static final Logger logger = HypixelConnection.logger;

    private static ScheduledExecutorService scheduler;
    public static boolean isTracking = false;

    private static long lastTrackTime = 0;
    private static final int COOLDOWN_PERIOD = 30;

    private static final HashMap<String, CachedData> collectionCache = new HashMap<>();
    private static final int CACHE_EXPIRATION = 150;

    private static CollectionOverlay collectionOverlay = null;

    private static final int PAUSE_PERIOD = 600; // Retry period when paused (in seconds)

    public static void startTracking(ICommandSender sender) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastTrackTime) / 1000 < COOLDOWN_PERIOD) {
            sender.addChatMessage(new ChatComponentText("§cPlease wait before tracking another collection!"));
            return;
        }

        sender.addChatMessage(new ChatComponentText("§aTracking " + collection + " collection"));

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        if (collectionOverlay == null) {
            collectionOverlay = new CollectionOverlay();
        }

        lastTrackTime = currentTime;
        isTracking = true;

        logger.info("Tracking started for player with UUID: {}", PlayerUUID.UUID);

        scheduleDataFetch();
    }

    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            sender.addChatMessage(new ChatComponentText("§cStopped tracking!"));
            logger.info("Tracking stopped.");

            isTracking = false;
            lastTrackTime = System.currentTimeMillis();
            previousCollection = -1;

            if (collectionOverlay != null) {
                CollectionOverlay.stopTracking();
                collectionOverlay = null;
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    public static void scheduleDataFetch() {
        scheduler.scheduleAtFixedRate(HypixelApiFetcher::fetchData, 15, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 300 seconds");
    }

    public static void fetchData() {
        try {
            if (collectionCache.containsKey(PlayerUUID.UUID)) {
                CachedData cachedData = collectionCache.get(PlayerUUID.UUID);
                if (cachedData.isValid()) {
                    logger.info("Using cached data for player with UUID: {}", PlayerUUID.UUID);
                    TrackCollection.displayCollection(cachedData.getJsonData());
                    return;
                } else {
                    collectionCache.remove(PlayerUUID.UUID);
                }
            }

            logger.info("Fetching data from URL: {}", BASE_URL);
            String jsonData = fetchJsonData(PlayerUUID.UUID, TokenManager.getToken());

            collectionCache.put(PlayerUUID.UUID, new CachedData(jsonData, System.currentTimeMillis()));
            TrackCollection.displayCollection(jsonData);
            logger.info("Data successfully fetched and displayed for player with UUID: {}", PlayerUUID.UUID);

        } catch (Exception e) {
            logger.error("Error fetching data from the Hypixel API: {}", e.getMessage(), e);
        }
    }

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
            return (System.currentTimeMillis() - timestamp) < CACHE_EXPIRATION * 1000;
        }
    }

    public static String fetchJsonData(String uuid, String token) {
        try {
            URL url = new URL(BASE_URL);

            // Open a connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Set the headers (X-UUID and Authorization)
            conn.setRequestProperty("X-UUID", uuid);
            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");

            // Get the response code
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {  // If response is OK
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Close connections
                in.close();
                conn.disconnect();

                // Return the fetched data
                return content.toString();
            } else {
                logger.error("Failed to fetch data, server responded with code: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            // Handle and log the error
            logger.error("An error occurred while fetching data from the server: {}", e.getMessage());
            return null;
        }
    }

    public static void pauseTracking() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            logger.info("Tracking paused. Will check again in {} seconds.", PAUSE_PERIOD);

            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(HypixelApiFetcher::resumeTracking, PAUSE_PERIOD, TimeUnit.SECONDS);
        }
    }

    public static void resumeTracking() {
        logger.info("Resuming tracking to check for updates.");

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        scheduler = Executors.newScheduledThreadPool(1);
        scheduleDataFetch();
    }
}
