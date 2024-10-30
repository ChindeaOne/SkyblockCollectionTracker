package io.github.chindeaytb.collectiontracker.api;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiManager {

    private static String TOKEN_URL = null;
    private static String BASE_URL = null;
    private static String User_Agent = null;
    private static String Token_Validation = null;

    private static final Logger logger = LogManager.getLogger(ApiManager.class);

    static {
        try (InputStream input = ApiManager.class.getClassLoader().getResourceAsStream("secret.properties")) {
            if (input == null) {
                logger.error("Unable to find secret.properties");
            } else {
                Properties prop = new Properties();
                prop.load(input);

                TOKEN_URL = prop.getProperty("TOKEN_URL");
                BASE_URL = prop.getProperty("BASE_URL");
                User_Agent = prop.getProperty("User-Agent") + "/" + ModInitialization.getVersion();
                Token_Validation = prop.getProperty("Token-Validation");
            }
        } catch (IOException e) {
            logger.error("Error loading properties file", e);
        }
    }

    public static String getTokenUrl() {
        return TOKEN_URL;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getAgent() {
        return User_Agent;
    }

    public static String getTokenValidation() {
        return Token_Validation;
    }
}