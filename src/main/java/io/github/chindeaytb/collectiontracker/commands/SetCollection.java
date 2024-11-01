package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.player.PlayerUUID;
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import io.github.chindeaytb.collectiontracker.util.Utils;


import static io.github.chindeaytb.collectiontracker.player.PlayerUUID.UUID;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;

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
            if(UUID.isEmpty()) {
                PlayerUUID.getUUID();
            }
            if (args[0].equalsIgnoreCase("track")) {
                if(!Utils.isOnSkyblock()){
                    sender.addChatMessage(new ChatComponentText("§cYou must be on Skyblock to use this command!"));
                    return;
                }
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
                    if (!isValidCollection(collection)) {
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

    private boolean isValidCollection(String collectionName) {
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
            case "mithril":
            case "mycelium":
            case "red sand":
            case "hard stone":
            case "cocoa beans":
            case "carrot":
            case "cactus":
            case "raw chicken":
            case "sugar cane":
            case "pumpkin":
            case "wheat":
            case "seeds":
            case "mushroom":
            case "raw rabbit":
            case "nether wart":
            case "mutton":
            case "melon":
            case "potato":
            case "leather":
            case "porkchop":
            case "feather":
            case "lapis lazuli":
            case "glowstone":
            case "gravel":
            case "slimeball":
            case "magma cream":
            case "ghast tear":
            case "gunpowder":
            case "rotten flesh":
            case "spider eye":
            case "bone":
            case "blaze rod":
            case "string":
            case "acacia wood":
            case "spruce wood":
            case "jungle wood":
            case "birch wood":
            case "oak wood":
            case "dark oak wood":
            case "lily pad":
            case "prismarine shard":
            case "ink sac":
            case "raw fish":
            case "pufferfish":
            case "clownfish":
            case "raw salmon":
            case "magmafish":
            case "prismarine crystal":
            case "clay":
            case "sponge":
            case "wilted berberis":
            case "living metal heart":
            case "caducous stem":
            case "agaricus cap":
            case "hemovibe":
            case "half-eaten carrot":
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
