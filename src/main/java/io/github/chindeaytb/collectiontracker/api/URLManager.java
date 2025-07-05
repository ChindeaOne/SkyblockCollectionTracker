package io.github.chindeaytb.collectiontracker.api;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class URLManager {
    public static final String TOKEN_URL;
    public static final String COLLECTION_URL;
    public static final String BAZAAR_URL;
    public static final String CHECK_BAZAAR_TYPE_URL;
    public static final String STATUS_URL;
    public static final String AGENT;

    static {
        Properties props = new Properties();
        try (InputStream in = URLManager.class.getClassLoader().getResourceAsStream("urls.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load urls.properties", e);
        }
        TOKEN_URL = props.getProperty("TOKEN_URL");
        COLLECTION_URL = props.getProperty("COLLECTION_URL");
        BAZAAR_URL = props.getProperty("BAZAAR_URL");
        CHECK_BAZAAR_TYPE_URL = props.getProperty("CHECK_BAZAAR_TYPE_URL");
        STATUS_URL = props.getProperty("STATUS_URL");
        AGENT = props.getProperty("AGENT");
    }
}