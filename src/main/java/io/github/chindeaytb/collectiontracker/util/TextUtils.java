package io.github.chindeaytb.collectiontracker.util;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
import io.github.chindeaytb.collectiontracker.collections.prices.NPCPrice;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.chindeaytb.collectiontracker.api.bazaarapi.FetchBazaarPrice.hasBazaarPrice;
import static io.github.chindeaytb.collectiontracker.commands.StartTracker.collection;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.*;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingRates.*;

public class TextUtils {

    static List<String> overlayLines = new ArrayList<>();

    public static void updateStats() {
        ModConfig config = Objects.requireNonNull(ModInitialization.configManager.getConfig());
        Overlay overlay = config.overlay;

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        if (!isTracking) {
            overlayLines.clear();
            return;
        }

        overlayLines.clear();
        for (int id : overlay.statsText) {
            switch (id) {
                case 0:
                    if (CollectionsManager.collection_source.equals("collection") && collection != null) {
                        String collectionText = collectionAmount >= 0
                                ? formatCollectionName(collection) + " collection: " + formatNumber(collectionAmount)
                                : formatCollectionName(collection) + " collection: Calculating...";
                        overlayLines.add(collectionText);
                    }
                    break;
                case 1:
                    if (collection != null) {
                        String sessionText = collectionMade > 0
                                ? formatCollectionName(collection) + " collection (session): " + formatNumber(collectionMade)
                                : formatCollectionName(collection) + " collection (session): Calculating...";
                        overlayLines.add(sessionText);
                    }
                    break;
                case 2:
                    String perHourText = collectionPerHour > 0
                            ? "Coll/h: " + formatNumber(collectionPerHour)
                            : "Coll/h: Calculating...";
                    overlayLines.add(perHourText);
                    break;
                case 3:
                    boolean hasNpcPrice = NPCPrice.getNpcPrice(collection) != -1;

                    if (config.bazaar.useBazaar && hasBazaarPrice) {
                        if (moneyPerHourBazaar > 0) {
                            overlayLines.add("$/h (Bazaar): " + formatNumber(moneyPerHourBazaar));
                        } else {
                            overlayLines.add("$/h (Bazaar): Calculating...");
                        }
                    } else if (hasNpcPrice) {
                        if (moneyPerHourNPC > 0) {
                            overlayLines.add("$/h (NPC): " + formatNumber(moneyPerHourNPC));
                        } else {
                            overlayLines.add("$/h (NPC): Calculating...");
                        }
                    }
                    break;
            }
        }
    }

    public static @NotNull List<String> getStrings() {
        updateStats();
        return overlayLines;
    }

    public static String uptimeString() {
        return ("Uptime: " + getUptime());
    }

    public static String formatCollectionName(String collection) {
        String[] words = collection.split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            } else {
                formattedName.append(" ").append(word.toLowerCase());
            }
        }
        return formattedName.toString();
    }

    private static String formatNumber(float number) {
        number = (float) Math.floor(number);

        if (number < 1_000) {
            return String.valueOf((int) number);
        } else if (number < 1_000_000) {
            return String.format("%.2fk", number / 1_000.0);
        } else if (number < 1_000_000_000) {
            return String.format("%.2fM", number / 1_000_000.0);
        } else {
            return String.format("%.2fB", number / 1_000_000_000.0);
        }
    }
}
