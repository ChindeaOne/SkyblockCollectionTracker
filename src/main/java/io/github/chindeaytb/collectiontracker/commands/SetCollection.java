package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.init.PlayerUUID;
import io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

import static io.github.chindeaytb.collectiontracker.init.PlayerUUID.UUID;
import static io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher.isTracking;

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
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("track");
        }
        if (args.length == 2) {
            completions.add("gold");
            completions.add("iron");
            completions.add("redstone");
            completions.add("cobblestone");
            completions.add("netherrack");
            completions.add("endstone");
            completions.add("diamond");
            completions.add("quartz");
            completions.add("obsidian");
            completions.add("gemstone");
            completions.add("umber");
            completions.add("coal");
            completions.add("emerald");
            completions.add("glacite");
            completions.add("tungsten");
        }
        return completions;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if(UUID.isEmpty()){
                PlayerUUID.getUUID();
            }

            if (args[0].equalsIgnoreCase("track")) {
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText("Use: /sct track <collection>"));
                    return;
                }
            }
            // Join the args to allow spaces in the API key
            StringBuilder keyBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                keyBuilder.append(args[i]);
                if (i < args.length - 1) {
                    keyBuilder.append(" ");
                }
            }

            if(!isTracking) {
                collection = keyBuilder.toString().trim().toLowerCase();
                if (!isValidCollection(collection)) {
                    sender.addChatMessage(new ChatComponentText("§4Invalid collection!"));
                } else {
                        sender.addChatMessage(new ChatComponentText("§aTracking " + collection + " collection"));
                        HypixelApiFetcher.startTracking(sender);

                }
            }

            else {
                sender.addChatMessage(new ChatComponentText("§cAlready tracking a collection."));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidCollection(String collectionName) {
        // Check if the collectionName matches any of the valid collections
        switch (collectionName) {
            case "gold":
            case "iron":
            case "redstone":
            case "cobblestone":
            case "netherrack":
            case "endstone":
            case "diamond":
            case "quartz":
            case "obsidian":
            case "gemstone":
            case "umber":
            case "coal":
            case "emerald":
            case "glacite":
            case "tungsten":
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
