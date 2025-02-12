package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
import io.github.chindeaytb.collectiontracker.collections.prices.BazaarPrice;
import io.github.chindeaytb.collectiontracker.collections.prices.NPCPrice;
import io.github.chindeaytb.collectiontracker.config.categories.Bazaar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;
import java.util.Objects;

import static io.github.chindeaytb.collectiontracker.collections.BazaarCollectionsManager.enchantedCollectionCraft;
import static io.github.chindeaytb.collectiontracker.commands.StartTracker.collection;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.getUptimeInSeconds;
import static io.github.chindeaytb.collectiontracker.util.TextUtils.updateStats;

public class TrackingRates {

    private static final Logger logger = LogManager.getLogger(TrackingRates.class);

    public static float previousCollection = -1;
    public static float sessionStartCollection = 0;
    public static boolean afk = false;

    public static float collectionAmount;
    public static float collectionPerHour;
    public static float collectionMade;
    public static float moneyPerHourNPC;
    public static float moneyPerHourBazaar;

    public static void displayCollection(String jsonResponse) {
        try (JsonReader reader = new JsonReader(new StringReader(jsonResponse))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (!jsonObject.entrySet().isEmpty()) {
                    String key = jsonObject.entrySet().iterator().next().getKey();
                    float currentCollection = jsonObject.get(key).getAsFloat();

                    if (previousCollection > 0) {
                        if (currentCollection == previousCollection) {
                            afk = true;
                            if (TrackingHandlerClass.isTracking) {
                                TrackingHandlerClass.stopTracking();
                            }
                            return;
                        }
                    } else {
                        sessionStartCollection = currentCollection;
                    }

                    long uptime = getUptimeInSeconds();
                    float collectedSinceStart = currentCollection - sessionStartCollection;
                    float priceNPC, priceBazaar;
                    Bazaar bazaarConfig = Objects.requireNonNull(ModInitialization.configManager.getConfig()).bazaar;

                    priceBazaar = BazaarPrice.getPrice(collection);
                    priceNPC = NPCPrice.getNpcPrice(collection);

                    priceNPC = CollectionsManager.notRiftCollection(collection) ? priceNPC : 0;
                    priceBazaar = CollectionsManager.notRiftCollection(collection) ? priceBazaar : 0;

                    collectionAmount = (float) Math.floor(currentCollection);
                    collectionPerHour = uptime > 0 ? (float) Math.floor((collectedSinceStart / uptime) * 3600) : 0;
                    collectionMade = (float) Math.floor(collectedSinceStart);
                    moneyPerHourNPC = uptime > 0 ? (float) Math.floor(priceNPC * collectedSinceStart / (uptime / 3600.0f)) : 0;
                    moneyPerHourBazaar = uptime > 0 ? (float) Math.floor(priceBazaar * (collectedSinceStart / enchantedCollectionCraft(collection, bazaarConfig.useBazaar, bazaarConfig.bazaarType)) / (uptime / 3600.0f)) : 0;

                    updateStats();

                    previousCollection = currentCollection;
                } else {
                    logger.warn("Collection '{}' not found in the response.", collection);
                }
            } else {
                logger.error("Invalid JSON response: {}", jsonResponse);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the collection data", e);
        }
    }
}

