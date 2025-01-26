package io.github.chindeaytb.collectiontracker.gui;

import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.getUptime;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.startTime;
import static io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay.config;

public class TextUtils {

    public static final String BLACK = "§0";
    public static final String DARK_BLUE = "§1";
    public static final String DARK_GREEN = "§2";
    public static final String DARK_AQUA = "§3";
    public static final String DARK_RED = "§4";
    public static final String DARK_PURPLE = "§5";
    public static final String GOLD = "§6";
    public static final String GRAY = "§7";
    public static final String DARK_GRAY = "§8";
    public static final String BLUE = "§9";
    public static final String GREEN = "§a";
    public static final String AQUA = "§b";
    public static final String RED = "§c";
    public static final String LIGHT_PURPLE = "§d";
    public static final String YELLOW = "§e";
    public static final String WHITE = "§f";

    private static String formatCollectionName(String collection) {
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
            return String.format("%.1fk", number / 1_000.0);
        } else if (number < 1_000_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else {
            return String.format("%.1fB", number / 1_000_000_000.0);
        }
    }

    public static @NotNull List<String> getStrings(String collectionName, float collectionAmount, float collectionPerHour, float collectionMade, float moneyPerHour) {
        List<String> overlayLines = new ArrayList<>();
        Overlay overlay = config.overlay;

        for (int id : overlay.statsText) {
            switch (id) {
                case 0:
                    if (collectionName != null && collectionAmount >= 0) {
                        overlayLines.add(WHITE + formatCollectionName(collectionName) + " collection " + WHITE + "> " + formatNumber(collectionAmount));
                    }
                    break;
                case 1:
                    if (collectionName != null && collectionMade >= 0) {
                        overlayLines.add(WHITE + formatCollectionName(collectionName) + " collection made " + WHITE + "> " + formatNumber(collectionMade));
                    }
                    break;
                case 2:
                    if (collectionPerHour >= 0) {
                        overlayLines.add(WHITE + "Coll/h " + WHITE + "> " + formatNumber(collectionPerHour));
                    }
                    break;
                case 3:
                    if (moneyPerHour >= 0) {
                        overlayLines.add(WHITE + "$/h (NPC) " + WHITE + "> " + formatNumber(moneyPerHour));
                    }
                    break;
                case 4:
                    if (startTime != 0) {
                        overlayLines.add(WHITE + "Uptime " + WHITE + "> " + getUptime());
                    }
                    break;
            }
        }
        return overlayLines;
    }

}
