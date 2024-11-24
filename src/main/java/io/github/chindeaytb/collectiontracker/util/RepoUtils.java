package io.github.chindeaytb.collectiontracker.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RepoUtils {

    private static final String API_URL = "https://api.github.com/repos/ChindeaYTB/SkyblockCollectionTracker/releases/latest";
    public static String latestVersion;
    public static String releasePageUrl;
    private static final Logger logger = LogManager.getLogger(RepoUtils.class);

    public static void checkForUpdates() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            if (connection.getResponseCode() == 200) {
                JsonObject jsonResponse = getJsonObject(connection);

                latestVersion = jsonResponse.get("tag_name").getAsString();

                releasePageUrl = jsonResponse.get("html_url").getAsString();

            } else {
                logger.error("Failed to check for updates. HTTP Response Code: {}", connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("An error occurred while checking for updates", e);
        }
    }

    private static JsonObject getJsonObject(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());
        return jsonElement.getAsJsonObject();
    }
}
