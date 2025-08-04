package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
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
        ChatUtils.INSTANCE.sendMessage("§aList of all collections available:");
        sender.addChatMessage(new ChatComponentText(""));

        Map<String, String> categoryColors = new HashMap<>();
        categoryColors.put("Farming", "§a");
        categoryColors.put("Mining", "§9");
        categoryColors.put("Combat", "§4");
        categoryColors.put("Foraging", "§6");
        categoryColors.put("Fishing", "§3");
        categoryColors.put("Rift", "§5");
        categoryColors.put("Sacks", "§8");

        Map<String, List<String>> categorizedCollections = new LinkedHashMap<>();

        for (Map.Entry<String, Set<String>> entry : CollectionsManager.collections.entrySet()) {
            String category = entry.getKey();
            Set<String> items = entry.getValue();

            List<String> sortedItems = new ArrayList<>(items);
            categorizedCollections.put(category, sortedItems);
        }

        for (Map.Entry<String, List<String>> entry : categorizedCollections.entrySet()) {
            String category = entry.getKey();
            String color = categoryColors.getOrDefault(category, "§f");
            sendCategoryMessage(sender, color, category, entry.getValue());
        }
    }

    private void sendCategoryMessage(ICommandSender sender, String color, String category, List<String> collections) {
        sender.addChatMessage(new ChatComponentText("   " + color + category + " Collections:"));
        for (String collection : collections) {
            ChatComponentText message = new ChatComponentText("   " + color + "- " + collection);
            message.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sct track " + collection)));
            sender.addChatMessage(message);
        }
        sender.addChatMessage(new ChatComponentText(""));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
