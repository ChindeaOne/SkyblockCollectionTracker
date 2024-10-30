package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.player.PlayerUUID;
import io.github.chindeaytb.collectiontracker.api.HypixelApiFetcher;
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataFetcher {

    private static final Logger logger = LogManager.getLogger(DataFetcher.class);

    public static ScheduledExecutorService scheduler;

    private static final HashMap<String, CachedData> collectionCache = new HashMap<>();
    private static final int CACHE_EXPIRATION = 150;

    public static void scheduleDataFetch() {
        scheduler.scheduleAtFixedRate(DataFetcher::fetchData, 15, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 300 seconds");
    }

    public static void fetchData() {
        try {
            if (collectionCache.containsKey(PlayerUUID.UUID)) {
                CachedData cachedData = collectionCache.get(PlayerUUID.UUID);
                if (cachedData.isValid()) {
                    logger.info("Using cached data for player with UUID: {}", PlayerUUID.UUID);
                    String targetProfileId = TrackCollection.findSelectedProfileId(cachedData.getJsonData());
                    logger.info("Selected profile ID: {}", targetProfileId);
                    TrackCollection.displayCollection(cachedData.getJsonData(),targetProfileId);
                    return;
                } else {
                    collectionCache.remove(PlayerUUID.UUID);
                }
            }

            String jsonData = HypixelApiFetcher.fetchJsonData(PlayerUUID.UUID, TokenManager.getToken());

            collectionCache.put(PlayerUUID.UUID, new CachedData(jsonData, System.currentTimeMillis()));

            String targetProfileId = TrackCollection.findSelectedProfileId(jsonData);
            logger.info("Selected profile ID: {}", targetProfileId);
            TrackCollection.displayCollection(jsonData, targetProfileId);
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


}
