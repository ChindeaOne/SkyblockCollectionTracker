package io.github.chindeaytb.collectiontracker.api.tokenapi;

public class TokenManager {

    private static String token;

    // Fetch the token and store it
    public static void fetchAndStoreToken() throws Exception {
        TokenFetcher tokenFetcher = new TokenFetcher();
        token = tokenFetcher.fetchToken();
    }

    // Get the stored token
    public static String getToken() {
        return token;
    }
}