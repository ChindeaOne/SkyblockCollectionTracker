package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

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
        return completions;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
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

            collection = keyBuilder.toString().trim().toLowerCase();
            sender.addChatMessage(new ChatComponentText("§aTracking " + collection + " collection"));

            if (SetHypixelApiKey.apiKey.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("Set api key first!"));
            } else {
                HypixelApiFetcher.startTracking(sender);
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
