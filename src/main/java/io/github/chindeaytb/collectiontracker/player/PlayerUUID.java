package io.github.chindeaytb.collectiontracker.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerUUID {
    private static final String MOJANG_API = "https://api.mojang.com/users/profiles/minecraft/";
    public static String UUID = "";

    private static final Logger logger = LogManager.getLogger(PlayerName.class);

    public static void getUUID() {
        PlayerName.getPlayerName();

        if (!PlayerName.player_name.isEmpty()) {
            try {
                UUID = fetchUUID(PlayerName.player_name, MOJANG_API);
            } catch (Exception e) {
                logger.error("An error occurred while fetching the UUID for player: {}", PlayerName.player_name, e);
            }
        }
    }

    public static String fetchUUID(String playerName, String MOJANG_API) throws Exception {
        String api = MOJANG_API.concat(playerName);
        URL url = new URL(api);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to fetch UUID: HTTP response code " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(content.toString()).getAsJsonObject();
            return jsonObject.get("id").getAsString();
        } catch (JsonSyntaxException e) {
            throw new Exception("Failed to parse JSON: " + content);
        }
    }

}
