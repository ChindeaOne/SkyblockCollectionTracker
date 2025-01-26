package io.github.chindeaytb.collectiontracker.tracker;

import io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay;
import io.github.chindeaytb.collectiontracker.util.Hypixel;
import io.github.chindeaytb.collectiontracker.util.PlayerData;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.github.chindeaytb.collectiontracker.commands.StartTracker.collection;
import static io.github.chindeaytb.collectiontracker.tracker.DataFetcher.scheduler;
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.*;

public class TrackingHandlerClass {

    private static final Logger logger = LogManager.getLogger(TrackingHandlerClass.class);
    private static final int COOLDOWN_PERIOD = 15;
    public static boolean isTracking = false;
    public static boolean isPaused = false;
    public static long startTime;
    public static long lastTime;
    private static long lastTrackTime = 0;

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

        lastTrackTime = currentTime;
        isTracking = true;
        isPaused = false;

        CollectionOverlay.setVisible(true);

        startTime = 0;
        lastTime = 0;

        logger.info("Tracking started for player: {}", PlayerData.INSTANCE.getPlayerName());

        DataFetcher.scheduleDataFetch();
    }

    public static void stopTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            isTracking = false;
            isPaused = false;
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            sender.addChatMessage(new ChatComponentText("§cStopped tracking!"));
            logger.info("Tracking stopped.");


            lastTrackTime = System.currentTimeMillis();
            startTime = 0;
            lastTime = 0;
            previousCollection = -1;
            sessionStartCollection = 0;

            CollectionOverlay.stopTracking();
        } else {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    public static void stopTracking() {
        if (scheduler != null && !scheduler.isShutdown()) {
            isTracking = false;
            isPaused = false;
            afk = false;
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }

            if (!Hypixel.INSTANCE.getServer()) {
                logger.info("Tracking stopped because player disconnected from the server.");
            } else if (afk) {
                logger.info("Tracking stopped because the player went AFK.");
            } else {
                logger.info("Tracking stopped because the api server is offline.");
            }

            lastTrackTime = System.currentTimeMillis();
            startTime = 0;
            lastTime = 0;
            previousCollection = -1;
            sessionStartCollection = 0;

            CollectionOverlay.stopTracking();
        } else {
            logger.warn("Attempted to stop tracking, but no tracking is active.");
        }
    }

    public static void pauseTracking(ICommandSender sender) {
        if (scheduler != null && !scheduler.isShutdown()) {
            if (isPaused) {
                sender.addChatMessage(new ChatComponentText("§cTracking is already paused!"));
                logger.warn("Attempted to pause tracking, but tracking is already paused.");
                return;
            }
            isPaused = true;
            lastTime += (System.currentTimeMillis() - startTime) / 1000;
            sender.addChatMessage(new ChatComponentText("§7Tracking paused."));
            logger.info("Tracking paused.");
        } else {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to pause tracking, but no tracking is active.");
        }
    }

    public static void resumeTracking(ICommandSender sender) {
        if (scheduler == null || scheduler.isShutdown() && !isTracking) {
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
            logger.warn("Attempted to resume tracking, but no tracking is active.");
            return;
        }

        if (isTracking && isPaused) {
            sender.addChatMessage(new ChatComponentText("§7Resuming tracking."));
            logger.info("Resuming tracking.");
            startTime = System.currentTimeMillis();
            isPaused = false;
        } else if (isTracking) {
            sender.addChatMessage(new ChatComponentText("§cTracking is already active!"));
            logger.warn("Attempted to resume tracking, but tracking is already active.");
        } else {
            sender.addChatMessage(new ChatComponentText("§cTracking has not been started yet!"));
            logger.warn("Attempted to resume tracking, but tracking has not been started.");
        }
    }

    public static long getUptimeInSeconds() {
        if (startTime == 0) {
            return 0;
        }

        if (isPaused) {
            return lastTime;
        } else {
            return lastTime + (System.currentTimeMillis() - startTime) / 1000;
        }
    }

    public static String getUptime() {
        if (startTime == 0) return "00:00:00";

        long uptime;

        if (isPaused) {
            uptime = lastTime;
        } else {
            uptime = lastTime + (System.currentTimeMillis() - startTime) / 1000;
        }

        long hours = uptime / 3600;
        long minutes = (uptime % 3600) / 60;
        long seconds = uptime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}