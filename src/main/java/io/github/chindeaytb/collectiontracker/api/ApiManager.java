package io.github.chindeaytb.collectiontracker.api;

import io.github.chindeaytb.collectiontracker.api.decryptor.Decryptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ApiManager {

    private static String KEY = null;
    private static String TOKEN_URL = null;
    private static String BASE_URL = null;
    private static String USER_AGENT = null;
    private static String TOKEN_VALIDATION = null;

    private static final Logger logger = LogManager.getLogger(ApiManager.class);

    static {
        File decryptedFile = null;
        try {
            decryptedFile = File.createTempFile("secret", ".properties");
            decryptedFile.deleteOnExit();

            try (InputStream encryptedInputStream = ApiManager.class.getClassLoader().getResourceAsStream("secret.properties.enc")) {

                assert encryptedInputStream != null;
                Decryptor.decryptFile(encryptedInputStream, decryptedFile.getAbsolutePath());

                try (InputStream input = Files.newInputStream(decryptedFile.toPath())) {
                    Properties properties = new Properties();
                    properties.load(input);

                    KEY = properties.getProperty("KEY");
                    TOKEN_URL = properties.getProperty("TOKEN_URL");
                    BASE_URL = properties.getProperty("BASE_URL");
                    USER_AGENT = properties.getProperty("User-Agent");
                    TOKEN_VALIDATION = properties.getProperty("Token-Validation");
                }
            }
        } catch (IOException e) {
            logger.error("Error loading properties file", e);
        } finally {
            if (decryptedFile != null && decryptedFile.exists()) {
                decryptedFile.delete();
            }
        }
    }

    @SuppressWarnings("unused")
    public static String getKey() {
        return KEY;
    }

    public static String getTokenUrl() {
        return TOKEN_URL;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static String getTokenValidation() {
        return TOKEN_VALIDATION;
    }
}