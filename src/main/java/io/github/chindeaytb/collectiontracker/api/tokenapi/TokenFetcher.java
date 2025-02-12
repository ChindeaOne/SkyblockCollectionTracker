package io.github.chindeaytb.collectiontracker.api.tokenapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.chindeaytb.collectiontracker.api.URLManager;
import io.github.chindeaytb.collectiontracker.util.PlayerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenFetcher {

    private static final Logger logger = LogManager.getLogger(TokenFetcher.class);

    public String fetchToken() throws Exception {
        URL url = new URL(URLManager.TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-UUID", PlayerData.INSTANCE.getPlayerUUID());
        connection.setRequestProperty("User-Agent", URLManager.AGENT);

        connection.setConnectTimeout(5000); // 5 seconds
        connection.setReadTimeout(5000); // 5 seconds

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                JsonObject jsonResponse = new JsonParser().parse(content.toString()).getAsJsonObject();
                String token = jsonResponse.has("token") ? jsonResponse.get("token").getAsString() : null;

                logger.info("Successfully fetched token");
                return token;
            }
        } else {
            logger.error("Failed to fetch token, response code: {}", responseCode);
            throw new Exception("Failed to fetch token, response code: " + responseCode);
        }
    }
}
