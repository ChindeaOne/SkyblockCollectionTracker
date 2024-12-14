package io.github.chindeaytb.collectiontracker.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.chindeaytb.collectiontracker.ModInitialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RepoUtils {

    public static final String MODRINTH_URL = "https://modrinth.com/mod/sct/versions/v" + ModInitialization.getVersion();
    private static final String API_URL = "https://api.github.com/repos/ChindeaYTB/SkyblockCollectionTracker/releases";
    private static final Logger logger = LogManager.getLogger(RepoUtils.class);
    public static String latestVersion;

    public static void checkForUpdates() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            if (connection.getResponseCode() == 200) {
                JsonArray releases = getJsonArray(connection);

                JsonObject latestRelease = null;
                for (JsonElement element : releases) {
                    JsonObject release = element.getAsJsonObject();
                    if (latestRelease == null || isNewerVersion(release, latestRelease)) {
                        latestRelease = release;
                    }
                }

                if (latestRelease != null) {
                    latestVersion = latestRelease.get("tag_name").getAsString().replaceFirst("^v", "");
                    logger.info("A new version (v{}) is available! Download it from Modrinth: {}", latestVersion, MODRINTH_URL);
                } else {
                    logger.warn("No releases found.");
                }
            } else {
                logger.error("Failed to check for updates. HTTP Response Code: {}", connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("An error occurred while checking for updates", e);
        }
    }

    private static JsonArray getJsonArray(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());
        return jsonElement.getAsJsonArray();
    }

    private static boolean isNewerVersion(JsonObject candidate, JsonObject current) {
        String candidateDate = candidate.get("created_at").getAsString();
        String currentDate = current.get("created_at").getAsString();
        return candidateDate.compareTo(currentDate) > 0;
    }
}
