package io.github.chindeaytb.collectiontracker.init;

import io.github.chindeaytb.collectiontracker.commands.*;
import io.github.chindeaytb.collectiontracker.connection.DisconnectHandlerClass;
import io.github.chindeaytb.collectiontracker.connection.ServerConnectHandlerClass;
import io.github.chindeaytb.collectiontracker.tokenapi.TokenManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModInitialization.MODID, clientSideOnly = true, version = "1.0.2")
public class ModInitialization {

    public static final String MODID = "skyblockcollectiontracker";
    public static final Logger logger = LogManager.getLogger(ModInitialization.class);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event bus
        MinecraftForge.EVENT_BUS.register(new DisconnectHandlerClass());
        MinecraftForge.EVENT_BUS.register(new ServerConnectHandlerClass());
        MinecraftForge.EVENT_BUS.register(this);

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
        try {
            // Fetch and store the token
            TokenManager.fetchAndStoreToken();
            logger.info("Token successfully fetched and stored.");
        } catch (Exception e) {
            logger.error("Failed to fetch token: {}", e.getMessage());
        }

        // Log post-initialization
        logger.info("Skyblock Collections Tracker post-initialization complete.");
    }
}
