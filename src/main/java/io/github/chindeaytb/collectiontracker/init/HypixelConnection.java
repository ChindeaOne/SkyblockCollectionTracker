package io.github.chindeaytb.collectiontracker.init;

import io.github.chindeaytb.collectiontracker.commands.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Mod(modid = HypixelConnection.MODID, clientSideOnly = true, version = "1.0.0")
public class HypixelConnection {

    public static final String MODID = "skyblockcollectiontracker";
    public static String apiKey;

    // Logger instance
    public static final Logger logger = LogManager.getLogger(HypixelConnection.class);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event bus
        MinecraftForge.EVENT_BUS.register(this);

        // Load API key from config.properties
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("config.properties not found in the classpath");
                return;
            }
            prop.load(input);
            apiKey = prop.getProperty("apikey");

            if (apiKey == null) {
                logger.error("API key not found in config.properties");
            } else {
                logger.info("API key loaded successfully.");
            }
        } catch (IOException e) {
            logger.error("Error loading config.properties", e);
        }

        // Register commands
        CommandHelper commandHelper = new CommandHelper();
        SetCollection setCollection = new SetCollection();
        StopTracker stopTracker = new StopTracker();
        ClientCommandHandler.instance.registerCommand(new SCT_Commands(commandHelper, setCollection, stopTracker));

        // Log initialization
        logger.info("Skyblock Collections Tracker mod initialized.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Log post-initialization
        logger.info("Skyblock Collections Tracker post-initialization complete.");
    }
}
