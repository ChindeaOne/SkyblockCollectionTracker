package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.gui.CollectionOverlay;
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

import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;
import static io.github.chindeaytb.collectiontracker.init.HypixelConnection.apiKey;
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.previousCollection;

public class HypixelApiFetcher {

    public static String HypixelApiURL = "";
    public static final String BASE_URL = "https://api.hypixel.net/v2/skyblock/profiles";
    private static final Logger logger = HypixelConnection.logger;

    private static ScheduledExecutorService scheduler;
    public static boolean isTracking = false;

    private static long lastTrackTime = 0;
    private static final int COOLDOWN_PERIOD = 30;

    private static final HashMap<String, CachedData> collectionCache = new HashMap<>();
    private static final int CACHE_EXPIRATION = 180;

    private static CollectionOverlay collectionOverlay = null;

    public static void startTracking(ICommandSender sender) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastTrackTime) / 1000 < COOLDOWN_PERIOD) {
            sender.addChatMessage(new ChatComponentText("§cPlease wait before tracking another collection!"));
            return;
        }

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        // Ensure only one instance of CollectionOverlay exists
        if (collectionOverlay == null) {
            collectionOverlay = new CollectionOverlay();
        }

        lastTrackTime = currentTime;
        isTracking = true;

        logger.info("Tracking started for player with UUID: {}", UUID);

        scheduleDataFetch(sender);
    }

    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            sender.addChatMessage(new ChatComponentText("§cStopped tracking!"));
            logger.info("Tracking stopped.");

            // Reset tracking variables
            isTracking = false;
            lastTrackTime = System.currentTimeMillis();
            previousCollection = -1;

            // Unregister and reset the overlay
            if (collectionOverlay != null) {
                CollectionOverlay.stopTracking();
                collectionOverlay = null; // Clear the reference to allow garbage collection
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    public static void scheduleDataFetch(ICommandSender sender) {
        scheduler.scheduleAtFixedRate(() -> fetchData(sender), 15, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 300 seconds");
    }

    public static void fetchData(ICommandSender sender) {
            try {
                if (collectionCache.containsKey(UUID)) {
                    CachedData cachedData = collectionCache.get(UUID);
                    if (cachedData.isValid()) {
                        logger.info("Using cached data for player with UUID: {}", UUID);
                        TrackCollection.displayCollection(cachedData.getJsonData(), sender);
                        return;
                    } else {
                        collectionCache.remove(UUID);
                    }
                }

                HypixelApiURL = getSkyBlockProfileURL(UUID, apiKey);
                logger.debug("Fetching data from URL: {}", HypixelApiURL);
                String jsonData = fetchJsonData(HypixelApiURL);

                collectionCache.put(UUID, new CachedData(jsonData, System.currentTimeMillis()));

                TrackCollection.displayCollection(jsonData, sender);
                logger.info("Data successfully fetched and displayed for player with UUID: {}", UUID);
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
