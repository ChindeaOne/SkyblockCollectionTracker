package io.github.chindeaytb.collectiontracker.init;

import io.github.chindeaytb.collectiontracker.commands.*;
import io.github.chindeaytb.collectiontracker.token.TokenManager;
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

@Mod(modid = HypixelConnection.MODID, clientSideOnly = true, version = "1.0.2")
public class HypixelConnection {

    public static final String MODID = "skyblockcollectiontracker";
    public static final Logger logger = LogManager.getLogger(HypixelConnection.class);

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

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Nothing yet
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            logger.info("Player and world fully loaded on tick.");
            if (PlayerUUID.UUID.isEmpty()) {
                PlayerUUID.getUUID();
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
