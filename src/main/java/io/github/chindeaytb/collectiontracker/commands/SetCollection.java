package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import io.github.chindeaytb.collectiontracker.util.HypixelUtils;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;
import io.github.chindeaytb.collectiontracker.collections.ValidCollectionsManager;

public class SetCollection extends CommandBase {

    public static String collection = "";

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct <command> [arguments]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if(!HypixelUtils.isOnSkyblock()){
                sender.addChatMessage(new ChatComponentText("§cYou must be on Hypixel Skyblock to use this command!"));
                return;
            }

            if (args[0].equalsIgnoreCase("track")) {
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Use: /sct track <collection>"));
                    return;
                }

                StringBuilder keyBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    keyBuilder.append(args[i]);
                    if (i < args.length - 1) {
                        keyBuilder.append(" ");
                    }
                }

                if (!isTracking) {
                    collection = keyBuilder.toString().trim().toLowerCase();
                    if (!ValidCollectionsManager.isValidCollection(collection)) {
                        sender.addChatMessage(new ChatComponentText("§4Invalid collection!"));
                    } else{
                        TrackingHandlerClass.startTracking(sender);
                    }
                } else {
                    sender.addChatMessage(new ChatComponentText("§cAlready tracking a collection."));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
