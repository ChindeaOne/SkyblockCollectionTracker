package io.github.chindeaytb.collectiontracker.init;

import io.github.chindeaytb.collectiontracker.commands.*;
import io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mod(modid = HypixelConnection.MODID, clientSideOnly = true, version = "1.0.0")
public class HypixelConnection {

    public static final String MODID = "skyblockcollectiontracker";
    public static final Logger logger = LogManager.getLogger(HypixelConnection.class);

    // Static variables to hold the session ID and token
    public static String sessionId;
    public static String token;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event bus
        MinecraftForge.EVENT_BUS.register(this);

        // Register commands
        CommandHelper commandHelper = new CommandHelper();
        SetCollection setCollection = new SetCollection();
        StopTracker stopTracker = new StopTracker();
        MoveGui moveGui = new MoveGui();
        ClientCommandHandler.instance.registerCommand(new SCT_Commands(commandHelper, setCollection, stopTracker, moveGui));

        // Log initialization
        logger.info("Skyblock Collections Tracker mod initialized.");
    }

    /**
     * Sends the player's UUID and sessionId to the server.
     *
     * @param uuid The player's UUID.
     * @param sessionId The session ID.
     */
    private void sendUUIDToServer(String uuid, String sessionId) {
        try {
            String urlString = "https://hypixelapikey-d40a5ed42094.herokuapp.com/uuid";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload
            String postData = "{\"uuid\":\"" + uuid + "\", \"sessionId\":\"" + sessionId + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("UUID sent successfully to server.");
            } else {
                logger.error("Failed to send UUID to server: HTTP response code {}", responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            logger.error("An error occurred while sending UUID to server: {}", e.getMessage());
        }
    }

    /**
     * Calls the /herokutoken endpoint and retrieves the token.
     */
    private void fetchHerokuToken() {
        try {
            String urlString = "https://hypixelapikey-d40a5ed42094.herokuapp.com/herokutoken";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the token from the custom header
                token = connection.getHeaderField("X-Heroku-Token");
                logger.info("Heroku token fetched successfully: {}", token);
            } else {
                logger.error("Failed to fetch Heroku token: HTTP response code {}", responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            logger.error("An error occurred while fetching the Heroku token: {}", e.getMessage());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Generate the sessionId along with fetching the token
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            logger.info("Session ID generated: {}", sessionId);
        }

        // Fetch Heroku token during post-initialization
        fetchHerokuToken();

        // Log post-initialization
        logger.info("Skyblock Collections Tracker post-initialization complete.");
    }

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Ensure sessionId exists before proceeding
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            logger.info("Player and world fully loaded on tick.");
            if (PlayerUUID.UUID.isEmpty()) {
                PlayerUUID.getUUID();
                sendUUIDToServer(PlayerUUID.UUID, sessionId);
            }
            MinecraftForge.EVENT_BUS.unregister(this); // Unsubscribe once loaded
        }
    }

    @SubscribeEvent
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ICommandSender sender = new ICommandSender() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public IChatComponent getDisplayName() {
                return null;
            }

            @Override
            public void addChatMessage(IChatComponent component) {
            }

            @Override
            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
                return false;
            }

            @Override
            public BlockPos getPosition() {
                return null;
            }

            @Override
            public Vec3 getPositionVector() {
                return null;
            }

            @Override
            public World getEntityWorld() {
                return null;
            }

            @Override
            public Entity getCommandSenderEntity() {
                return null;
            }

            @Override
            public boolean sendCommandFeedback() {
                return false;
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int amount) {
            }
        };
        HypixelApiFetcher.stopTracking(sender);
    }
}
