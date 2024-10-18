package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.chindeaytb.collectiontracker.gui.CollectionOverlay;
import io.github.chindeaytb.collectiontracker.init.PlayerName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;

public class TrackCollection {

    public static long previousCollection = -1;
    private static final Logger logger = LogManager.getLogger(TrackCollection.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void displayCollection(String jsonResponse) {
        executor.submit(() -> {
            try {
                // Initialize JsonReader to process JSON incrementally
                JsonReader reader = new JsonReader(new StringReader(jsonResponse));
                reader.beginObject(); // Start reading the JSON object

                JsonObject correctProfile = null;

                // Loop through the root object
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("profiles")) {
                        // Start reading profiles array
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            String profileId = null;

                            // Find the profile with the target profile_id
                            while (reader.hasNext()) {
                                String key = reader.nextName();
                                if (key.equals("profile_id")) {
                                    profileId = reader.nextString();
                                } else if (key.equals("members")) {
                                    if (profileId != null && profileId.replace("-", "").equals(UUID)) {
                                        correctProfile = parseMembersObject(reader);
                                    } else {
                                        reader.skipValue();
                                    }
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();

                            if (correctProfile != null) {
                                break; // Exit the loop once the correct profile is found
                            }
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                reader.close();

            } catch (Exception e) {
                logger.error("An error occurred while processing the JSON response for player: {}", PlayerName.player_name, e);
            }

        });
    }

    // This method will check each member UUID and compare it to your UUID after removing the dashes
    private static JsonObject parseMembersObject(JsonReader reader) throws Exception {
        reader.beginObject();
        JsonObject memberData = null;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(UUID)) {
                memberData = parseCollectionObject(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return memberData;
    }

    private static JsonObject parseCollectionObject(JsonReader reader) throws Exception {
        JsonObject collectionsObject = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("collection")) {
                collectionsObject = new JsonObject();
                reader.beginObject();
                while (reader.hasNext()) {
                    String collectionName = reader.nextName();
                    if (collectionMatches(collectionName)) {
                        long currentCollection = reader.nextLong();

                        // Capitalize the first letter of the collection for the GUI
                        String formattedCollection = collection.substring(0, 1).toUpperCase() + collection.substring(1);

                        // Collection Per Hour calculation
                        String collectionPerHour;
                        if (previousCollection > 0) {
                            long collectedIn3Min = currentCollection - previousCollection;
                            long perHour = collectedIn3Min * 20;

                            // If there's no change, show "Paused"
                            if (collectedIn3Min == 0) {
                                collectionPerHour = "Paused"; // Display "Paused" in the GUI
                                TrackingHandlerClass.pauseTracking();
                            } else {
                                collectionPerHour = formatNumber(perHour);
                            }
                        } else  {
                                collectionPerHour = "Calculating..."; // For first-time calculations
                        }

                        logger.info("New collection is {}", currentCollection);
                        logger.info("Old collection is {}", previousCollection);

                        previousCollection = currentCollection;
                        // Update the GUI instead of sending chat messages
                        CollectionOverlay.updateCollectionData(formattedCollection, formatNumber(currentCollection), collectionPerHour);

                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return collectionsObject;
    }

    private static boolean collectionMatches(String collectionName) {
        switch (collection) {
            case "gold":
                return collectionName.equals("GOLD_INGOT");
            case "iron":
                return collectionName.equals("IRON_INGOT");
            case "redstone":
                return collectionName.equals("REDSTONE");
            case "cobblestone":
                return collectionName.equals("COBBLESTONE");
            case "netherrack":
                return collectionName.equals("NETHERRACK");
            case "endstone":
                return collectionName.equals("ENDER_STONE");
            case "diamond":
                return collectionName.equals("DIAMOND");
            case "quartz":
                return collectionName.equals("QUARTZ");
            case "obsidian":
                return collectionName.equals("OBSIDIAN");
            case "gemstone":
                return collectionName.equals("GEMSTONE_COLLECTION");
            case "umber":
                return collectionName.equals("UMBER");
            case "coal":
                return collectionName.equals("COAL");
            case "emerald":
                return collectionName.equals("EMERALD");
            case "glacite":
                return collectionName.equals("GLACITE");
            case "tungsten":
                return collectionName.equals("TUNGSTEN");
            case "mithril":
                return collectionName.equals("MITHRIL_ORE");
            case "mycelium":
                return collectionName.equals("MYCEL");
            case "red sand":
                return collectionName.equals("SAND:1");
            case "hard stone":
                return collectionName.equals("HARD_STONE");
            case "sulphur":
                return collectionName.equals("SULPHUR");
            default:
                return false;
        }
    }

    private static String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1_000_000) {
            return String.format("%.3fk", number / 1000.0); // Thousands (k)
        } else if (number < 1_000_000_000) {
            return String.format("%.3fM", number / 1_000_000.0); // Millions (M)
        } else {
            return String.format("%.3fB", number / 1_000_000_000.0); // Billions (B)
        }
    }
}
