package io.github.chindeaytb.collectiontracker.tracker;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;
import static io.github.chindeaytb.collectiontracker.commands.SetHypixelApiKey.apiKey;

public class HypixelApiFetcher {

    public static String HypixelApiURL = "";
    public static final String BASE_URL = "https://api.hypixel.net/v2/skyblock/profiles";

    // Create a scheduler
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Method to start tracking
    public static void startTracking(ICommandSender sender) {
        scheduleDataFetch(sender);
    }

    // Method to stop tracking
    public static void stopTracking(ICommandSender sender) {
        scheduler.shutdownNow();  // Stops all scheduled tasks
        sender.addChatMessage(new ChatComponentText("§4Tracking has been stopped."));
    }

    // Method to fetch data periodically
    public static void scheduleDataFetch(ICommandSender sender) {
        scheduler.scheduleAtFixedRate(() -> fetchData(sender), 0, 5, TimeUnit.MINUTES);
    }

    // Method that fetches data from the API
    public static void fetchData(ICommandSender sender) {
        try {
            HypixelApiURL = getSkyBlockProfileURL(UUID, apiKey);
            String jsonData = fetchJsonData(HypixelApiURL);
            TrackCollection.displayCollection(jsonData, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSkyBlockProfileURL(String uuid, String apiKey) {
        return BASE_URL + "?uuid=" + uuid + "&key=" + apiKey;
    }

    public static String fetchJsonData(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }
}

