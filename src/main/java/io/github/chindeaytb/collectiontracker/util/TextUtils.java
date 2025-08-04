package io.github.chindeaytb.collectiontracker.util;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.collections.BazaarCollectionsManager;
import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
import io.github.chindeaytb.collectiontracker.collections.GemstonesManager;
import io.github.chindeaytb.collectiontracker.collections.prices.NpcPrices;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.chindeaytb.collectiontracker.collections.CollectionsManager.collectionType;
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
                    if (CollectionsManager.collectionSource.equals("collection") && collection != null) {
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
                    boolean hasNpcPrice = NpcPrices.getNpcPrice(collection) != -1;
                    if (!CollectionsManager.isRiftCollection(collection) && BazaarCollectionsManager.hasBazaarData) {
                        if (config.bazaar.bazaarConfig.useBazaar) {
                            switch (collectionType) {
                                case "normal":
                                    float localMoneyPerHour = moneyPerHourBazaar.get(collectionType);
                                    if (localMoneyPerHour > 0) {
                                        overlayLines.add("$/h (Bazaar): " + formatNumber(localMoneyPerHour));
                                    } else {
                                        overlayLines.add("$/h (Bazaar): Calculating...");
                                    }
                                    break;
                                case "enchanted":
                                    if (config.bazaar.bazaarConfig.bazaarType.equals("Enchanted version")) {
                                        float enchantedPrice = moneyPerHourBazaar.get("Enchanted version");
                                        if (enchantedPrice > 0) {
                                            overlayLines.add("$/h (Bazaar): " + formatNumber(enchantedPrice));
                                        } else {
                                            overlayLines.add("$/h (Bazaar): Calculating...");
                                        }
                                    } else {
                                        float superEnchantedPrice = moneyPerHourBazaar.get("Super Enchanted version");
                                        if (superEnchantedPrice == -1.0f) {
                                            config.bazaar.bazaarConfig.bazaarType = "Enchanted version";
                                        } else if (superEnchantedPrice > 0) {
                                            overlayLines.add("$/h (Bazaar): " + formatNumber(superEnchantedPrice));
                                        } else {
                                            overlayLines.add("$/h (Bazaar): Calculating...");
                                        }
                                    }
                                    break;
                                case "gemstone":
                                    float gemstonePrice = moneyPerHourBazaar.get(config.mining.gemstones.gemstoneVariants.toUpperCase());
                                    if (gemstonePrice > 0) {
                                        overlayLines.add("$/h (Bazaar): " + formatNumber(gemstonePrice));
                                    } else {
                                        overlayLines.add("$/h (Bazaar): Calculating...");
                                    }
                                    break;
                            }
                        } else if (hasNpcPrice) {
                            if (moneyPerHourNPC > 0) {
                                overlayLines.add("$/h (NPC): " + formatNumber(moneyPerHourNPC));
                            } else {
                                overlayLines.add("$/h (NPC): Calculating...");
                            }
                        }
                    } else if (config.bazaar.bazaarConfig.useBazaar) {
                        config.bazaar.bazaarConfig.useBazaar = false;
                        ChatUtils.INSTANCE.sendMessage("§cYou cannot use Bazaar prices for this collection!");
                    }
                    break;
                case 4:
                    if (config.bazaar.bazaarConfig.useBazaar) {
                        if (GemstonesManager.checkIfGemstone(collection)) {
                            String extrasText = "Variant: " + config.mining.gemstones.gemstoneVariants;
                            overlayLines.add(extrasText);
                        } else if (collectionType.equals("enchanted")) {
                            String itemName;
                            if (config.bazaar.bazaarConfig.bazaarType.equals("Enchanted version")) {
                                itemName = formatBazaarItemName(BazaarCollectionsManager.enchantedRecipe.keySet().iterator().next());
                                overlayLines.add("Item: " + itemName);
                            } else {
                                itemName = formatBazaarItemName(BazaarCollectionsManager.superEnchantedRecipe.keySet().iterator().next());
                                overlayLines.add("Item: " + itemName);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private static String formatBazaarItemName(String name) {
        String[] words = name.split("_");
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                formatted.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            } else {
                formatted.append(" ").append(word);
            }
        }
        return formatted.toString();
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
