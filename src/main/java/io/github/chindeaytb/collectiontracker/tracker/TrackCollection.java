package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.StringReader;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;

public class TrackCollection {

    public static long previousCollection = -1;

    public static void displayCollection(String jsonResponse, ICommandSender sender) {
        try {
            // Initialize JsonReader to process JSON incrementally
            JsonReader reader = new JsonReader(new StringReader(jsonResponse));
            reader.beginObject(); // Start reading the JSON object

            boolean success = false;
            JsonObject correctProfile = null;

            // Loop through the root object
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("success")) {
                    success = reader.nextBoolean();
                } else if (name.equals("profiles")) {
                    // Start reading profiles array
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        JsonObject profileObject = new JsonObject();
                        String profileId = null;

                        // Find the profile with the target profile_id
                        while (reader.hasNext()) {
                            String key = reader.nextName();
                            if (key.equals("profile_id")) {
                                profileId = reader.nextString();
                            } else if (key.equals("members")) {
                                if (profileId != null && profileId.replace("-", "").equals(UUID)) {
                                    correctProfile = parseMembersObject(reader, sender);
                                } else {
                                    reader.skipValue();
                                }
                            }
                            else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();

                        if (correctProfile != null)
                            break;
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method will check each member UUID and compare it to your UUID after removing the dashes
    private static JsonObject parseMembersObject(JsonReader reader, ICommandSender sender) throws Exception {
        reader.beginObject();
        JsonObject memberData = null;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(UUID)) {
                memberData = parseCollectionObject(reader, sender);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return memberData;
    }

    private static JsonObject parseCollectionObject(JsonReader reader, ICommandSender sender) throws Exception {
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

                        // Capitalize the first letter of the collection for the chat message
                        String formattedCollection = collection.substring(0, 1).toUpperCase() + collection.substring(1);

                        // Send chat message with the capitalized collection name
                        sender.addChatMessage(
                                new ChatComponentText(formattedCollection + " collection: " + formatNumber(currentCollection)));

                        if (previousCollection > 0) {
                            long collectedIn5Min = currentCollection - previousCollection;
                            long perHour = collectedIn5Min * 12;

                            sender.addChatMessage(
                                    new ChatComponentText("Collection/h: " + formatNumber(perHour)));
                        }
                        previousCollection = currentCollection;
                    }
                    else {
                        reader.skipValue();
                    }
                }  reader.endObject();
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
            default:
                return false;
        }
    }

    private static String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1_000_000) {
            return String.format("%.1fk", number / 1000.0); // Thousands (k)
        } else if (number < 1_000_000_000) {
            return String.format("%.1fM", number / 1_000_000.0); // Millions (M)
        } else {
            return String.format("%.1fB", number / 1_000_000_000.0); // Billions (B)
        }
    }
}
