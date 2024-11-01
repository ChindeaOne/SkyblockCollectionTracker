package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
import io.github.chindeaytb.collectiontracker.gui.CollectionOverlay;
import io.github.chindeaytb.collectiontracker.player.PlayerName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import static io.github.chindeaytb.collectiontracker.player.PlayerUUID.UUID;

public class TrackCollection {

    public static long previousCollection = -1;
    private static final Logger logger = LogManager.getLogger(TrackCollection.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static String findSelectedProfileId(String jsonResponse) {
        try (JsonReader reader = new JsonReader(new StringReader(jsonResponse))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("profiles")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String profileId = null;
                        boolean isSelected = false;

                        while (reader.hasNext()) {
                            String key = reader.nextName();
                            if (key.equals("profile_id")) {
                                profileId = reader.nextString();
                            } else if (key.equals("selected")) {
                                isSelected = reader.nextBoolean();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();

                        if (isSelected) {
                            return profileId;
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (Exception e) {
            logger.error("An error occurred while finding the selected profile ID", e);
        }

        return null;
    }

    public static void displayCollection(String jsonResponse, String targetProfileId) {
        executor.submit(() -> {
            try (JsonReader reader = new JsonReader(new StringReader(jsonResponse))) {
                reader.beginObject();
                JsonObject correctProfile = null;

                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("profiles")) {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            String profileId = null;

                            while (reader.hasNext()) {
                                String key = reader.nextName();
                                if (key.equals("profile_id")) {
                                    profileId = reader.nextString();
                                } else if (key.equals("members")) {
                                    if (profileId != null && profileId.equals(targetProfileId)) {
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
                                break;
                            }
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (IllegalStateException e) {
                logger.error("An error occurred while processing the JSON response for player: {}. Exception: {}", PlayerName.player_name, e);
            } catch (Exception e) {
                logger.error("An unexpected error occurred while processing the JSON response for player: {}", PlayerName.player_name, e);
            }
        });
    }


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
                    if (CollectionsManager.collectionMatches(collectionName)) {
                        long currentCollection = reader.nextLong();
                        String formattedCollection = collection.substring(0, 1).toUpperCase() + collection.substring(1);

                        String collectionPerHour;
                        if (previousCollection > 0) {
                            long collectedIn3Min = currentCollection - previousCollection;
                            long perHour = collectedIn3Min * 20;

                            if (collectedIn3Min == 0) {
                                collectionPerHour = "Paused";
                                TrackingHandlerClass.pauseTracking();
                            } else {
                                collectionPerHour = formatNumber(perHour);
                            }
                        } else {
                            collectionPerHour = "Calculating...";
                        }

                        logger.info("New collection is {}", currentCollection);
                        logger.info("Old collection is {}", previousCollection);

                        previousCollection = currentCollection;
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

    private static String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1_000_000) {
            return String.format("%.3fk", number / 1000.0);
        } else if (number < 1_000_000_000) {
            return String.format("%.3fM", number / 1_000_000.0); // Millions (M)
        } else {
            return String.format("%.3fB", number / 1_000_000_000.0); // Billions (B)
        }
    }
}
