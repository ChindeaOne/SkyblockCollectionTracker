package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.gui.CollectionOverlay;
import io.github.chindeaytb.collectiontracker.init.PlayerUUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.previousCollection;
import static io.github.chindeaytb.collectiontracker.tracker.DataFetcher.scheduler;

public class TrackingHandlerClass {

    private static final Logger logger = LogManager.getLogger(TrackingHandlerClass.class);

    public static boolean isTracking = false;

    private static long lastTrackTime = 0;
    private static final int COOLDOWN_PERIOD = 30;

    private static CollectionOverlay collectionOverlay = null;

    private static final int PAUSE_PERIOD = 600; // Retry period when paused (in seconds)


    public static void startTracking(ICommandSender sender) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastTrackTime) / 1000 < COOLDOWN_PERIOD) {
            sender.addChatMessage(new ChatComponentText("§cPlease wait before tracking another collection!"));
            return;
        }

        sender.addChatMessage(new ChatComponentText("§aTracking " + collection + " collection"));

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        if (collectionOverlay == null) {
            collectionOverlay = new CollectionOverlay();
        }

        lastTrackTime = currentTime;
        isTracking = true;

        logger.info("Tracking started for player with UUID: {}", PlayerUUID.UUID);

        DataFetcher.scheduleDataFetch();
    }

    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            sender.addChatMessage(new ChatComponentText("§cStopped tracking!"));
            logger.info("Tracking stopped.");

            isTracking = false;
            lastTrackTime = System.currentTimeMillis();
            previousCollection = -1;

            if (collectionOverlay != null) {
                CollectionOverlay.stopTracking();
                collectionOverlay = null;
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    public static void pauseTracking() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            logger.info("Tracking paused. Will check again in {} seconds.", PAUSE_PERIOD);

            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(TrackingHandlerClass::resumeTracking, PAUSE_PERIOD, TimeUnit.SECONDS);
        }
    }

    public static void resumeTracking() {
        logger.info("Resuming tracking to check for updates.");

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        scheduler = Executors.newScheduledThreadPool(1);
        DataFetcher.scheduleDataFetch();
    }
}
