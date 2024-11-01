package io.github.chindeaytb.collectiontracker.api.tokenapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.github.chindeaytb.collectiontracker.api.ApiManager;

public class TokenFetcher {

    private static final Logger logger = LogManager.getLogger(TokenFetcher.class);

    public String fetchToken() throws Exception {

        URL url = new URL(ApiManager.getTokenUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(15000); // 15 seconds
        connection.setReadTimeout(15000); // 15 seconds

        connection.setRequestProperty("Token-Validation", ApiManager.getTokenValidation());
        connection.setRequestProperty("User-Agent", ApiManager.getUserAgent());

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                logger.info("Token fetched successfully.");
                return content.toString();
            }
        } else {
            logger.error("Failed to fetch token, response code: {}", responseCode);
            throw new Exception("Failed to fetch token, response code: " + responseCode);
        }
    }
}