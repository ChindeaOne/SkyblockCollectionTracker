package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.util.*;

public class CollectionList extends CommandBase {

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct collections";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("collections")) {
            sendCollectionList(sender);
        }
    }

    private void sendCollectionList(ICommandSender sender) {
        ChatUtils.INSTANCE.sendMessage("§aThese are all the collections:");

        Map<String, String> categoryColors = new HashMap<>();
        categoryColors.put("Farming", "§a");
        categoryColors.put("Mining", "§9");
        categoryColors.put("Combat", "§4");
        categoryColors.put("Foraging", "§6");
        categoryColors.put("Fishing", "§3");
        categoryColors.put("Rift", "§5");

        Map<String, List<String>> categorizedCollections = new LinkedHashMap<>();
        categorizedCollections.put("Farming", Arrays.asList("cocoa beans", "carrot", "cactus", "raw chicken", "sugar cane", "pumpkin", "wheat", "seeds", "red mushroom", "brown mushroom", "raw rabbit", "nether wart", "mutton", "melon", "potato", "leather", "porkchop", "feather"));
        categorizedCollections.put("Mining", Arrays.asList("lapis lazuli", "redstone", "umber", "coal", "mycelium", "end stone", "quartz", "sand", "iron", "amber", "topaz", "sapphire", "amethyst", "jasper", "ruby", "jade", "opal", "aquamarine", "citrine", "onyx", "peridot", "tungsten", "obsidian", "diamond", "cobblestone", "glowstone", "gold", "flint", "hard stone", "mithril", "emerald", "red sand", "ice", "glacite", "sulphur", "netherrack"));
        categorizedCollections.put("Combat", Arrays.asList("ender pearl", "chili pepper", "slimeball", "magma cream", "ghast tear", "gunpowder", "rotten flesh", "spider eye", "bone", "blaze rod", "string"));
        categorizedCollections.put("Foraging", Arrays.asList("acacia", "spruce", "jungle", "birch", "oak", "dark oak"));
        categorizedCollections.put("Fishing", Arrays.asList("lily pad", "prismarine shard", "ink sac", "raw fish", "pufferfish", "clownfish", "raw salmon", "magmafish", "prismarine crystals", "clay", "sponge"));
        categorizedCollections.put("Rift", Arrays.asList("wilted berberis", "living metal heart", "caducous stem", "agaricus cap", "hemovibe", "half-eaten carrot", "timite"));

        for (Map.Entry<String, List<String>> entry : categorizedCollections.entrySet()) {
            String category = entry.getKey();
            String color = categoryColors.getOrDefault(category, "§f");
            sender.addChatMessage(new ChatComponentText("   " + color + category + " Collections:"));

            for (String collection : entry.getValue()) {
                ChatComponentText message = new ChatComponentText("   " + color + "- " + collection);
                message.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sct track " + collection)));
                sender.addChatMessage(message);
            }
            sender.addChatMessage(new ChatComponentText(""));
        }
    }


    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
