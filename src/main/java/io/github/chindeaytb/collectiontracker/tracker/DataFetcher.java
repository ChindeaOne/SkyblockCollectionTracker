package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.api.hypixelapi.HypixelApiFetcher;
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager;
import io.github.chindeaytb.collectiontracker.util.PlayerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import io.github.chindeaytb.collectiontracker.util.ServerUtils;

public class DataFetcher {

    private static final Logger logger = LogManager.getLogger(DataFetcher.class);

    public static ScheduledExecutorService scheduler;

    private static final Map<CacheKey, CachedData> collectionCache = new HashMap<>();
    private static final int CACHE_EXPIRATION = 150;

    public static void scheduleDataFetch() {
        scheduler.scheduleAtFixedRate(DataFetcher::fetchData, 10, 180, TimeUnit.SECONDS);
        logger.info("Data fetching scheduled to run every 180 seconds");
    }

    public static void fetchData() {
        try {
            if (!ServerUtils.INSTANCE.getServerStatus()) {
                logger.warn("Server is not alive. Stopping the tracker.");
                TrackingHandlerClass.stopTracking();
                return;
            }

            String playerUUID = PlayerData.INSTANCE.getPlayerUUID();
            CacheKey cacheKey = new CacheKey(playerUUID, collection);

            // Check if cached data exists and is valid for the current UUID and collection
            if (collectionCache.containsKey(cacheKey)) {
                CachedData cachedData = collectionCache.get(cacheKey);
                if (cachedData.isValid()) {
                    logger.info("Using cached data for player with UUID: {} and collection: {}", playerUUID, collection);
                    TrackCollection.displayCollection(cachedData.getJsonData());
                    return;
                } else {
                    collectionCache.remove(cacheKey);
                }
            }

            // Fetch new data from the API and cache it
            String jsonData = HypixelApiFetcher.fetchJsonData(playerUUID, TokenManager.getToken(), collection);

            if(jsonData == null) {
                logger.error("Failed to fetch data from the server");
                return;
            }

            collectionCache.put(cacheKey, new CachedData(jsonData, System.currentTimeMillis()));

            TrackCollection.displayCollection(jsonData);
            logger.info("Data successfully fetched and displayed for player with UUID: {} and collection: {}", playerUUID, collection);

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
