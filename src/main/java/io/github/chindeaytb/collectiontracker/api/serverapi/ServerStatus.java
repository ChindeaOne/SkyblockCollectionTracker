package io.github.chindeaytb.collectiontracker.api.serverapi;

import io.github.chindeaytb.collectiontracker.api.URLManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerStatus {

    private static final Logger logger = LogManager.getLogger(ServerStatus.class);
    private static final int TIMEOUT = 15000; // 15 seconds

    public static boolean checkServer() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URLManager.STATUS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            logger.error("Error checking server status", e);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
