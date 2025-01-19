package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.api.hypixelapi.HypixelApiFetcher;
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager;
import io.github.chindeaytb.collectiontracker.util.PlayerData;
import io.github.chindeaytb.collectiontracker.util.ServerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.*;

public class DataFetcher {

    private static final Logger logger = LogManager.getLogger(DataFetcher.class);
    private static final Map<CacheKey, String> collectionCache = new HashMap<>();
    private static final Map<CacheKey, Long> cacheTimestamps = new HashMap<>();
    private static final long CACHE_LIFESPAN = 120000; // 2 minutes
    public static ScheduledExecutorService scheduler;

    public static void scheduleDataFetch() {
        scheduler.scheduleAtFixedRate(DataFetcher::fetchData, 5, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 60 seconds");
    }

    public static void fetchData() {
        try {
            if (!ServerUtils.INSTANCE.getServerStatus()) {
                logger.warn("Server is not alive. Stopping the tracker.");
                TrackingHandlerClass.stopTracking();
                return;
            }
            if(isPaused) return;

            startTime = 0;
            lastTime = 0;

            String playerUUID = PlayerData.INSTANCE.getPlayerUUID();

            String jsonData = getData(playerUUID, collection);

            if (jsonData == null) {
                logger.error("Failed to fetch or retrieve data from the cache");
                return;
            }

            TrackCollection.displayCollection(jsonData);
            logger.info("Data successfully fetched or retrieved and displayed for player with UUID: {} and collection: {}", playerUUID, collection);

        } catch (Exception e) {
            logger.error("Error fetching data from the Hypixel API: {}", e.getMessage(), e);
        }
    }

    public static String getData(String playerUUID, String collection) {
        CacheKey cacheKey = new CacheKey(playerUUID, collection);
        Long lastFetched = cacheTimestamps.get(cacheKey);

        if (lastFetched != null && (System.currentTimeMillis() - lastFetched) < CACHE_LIFESPAN) {
            logger.info("Returning cached data for player with UUID: {} and collection: {}", playerUUID, collection);
            return collectionCache.get(cacheKey);
        }

        logger.info("Cache expired or missing. Fetching new data for player with UUID: {} and collection: {}", playerUUID, collection);
        String jsonData = HypixelApiFetcher.fetchJsonData(playerUUID, TokenManager.getToken(), collection);

        if (jsonData != null) {
            collectionCache.put(cacheKey, jsonData);
            cacheTimestamps.put(cacheKey, System.currentTimeMillis());
        }

        return jsonData;
    }

    private static class CacheKey {
        private final String uuid;
        private final String collection;

        public CacheKey(String uuid, String collection) {
            this.uuid = uuid;
            this.collection = collection;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return uuid.equals(cacheKey.uuid) && collection.equals(cacheKey.collection);
        }

        @Override
        public int hashCode() {
            int result = uuid.hashCode();
            result = 31 * result + collection.hashCode();
            return result;
        }
    }
}
