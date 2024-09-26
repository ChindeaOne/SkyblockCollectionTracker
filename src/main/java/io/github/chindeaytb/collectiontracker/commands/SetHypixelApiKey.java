package io.github.chindeaytb.collectiontracker.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.chindeaytb.collectiontracker.init.PlayerUUID;
import io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class SetHypixelApiKey extends CommandBase {

    public static String apiKey = "";

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
            completions.add("setkey");
        }
        return completions;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        Thread thread = new Thread(() -> {
            try {
                PlayerUUID.getUUID();

                if (args[0].equalsIgnoreCase("setkey")) {
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText("Use: /sct setkey <your_api_key>"));
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

                apiKey = keyBuilder.toString().trim();
                if(apiKey.isEmpty()) {
                    sender.addChatMessage(new ChatComponentText("Please enter a valid api key"));
                }
                else {
                    String apiUrl = HypixelApiFetcher.getSkyBlockProfileURL(PlayerUUID.UUID, apiKey);
                    try {
                        String jsonResponse = HypixelApiFetcher.fetchJsonData(apiUrl);

                        // Parse the JSON response to check the "success" field
                        JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
                        boolean success = jsonObject.get("success").getAsBoolean();

                        if (!success) {
                            sender.addChatMessage(new ChatComponentText("Invalid API key"));
                        } else {
                            sender.addChatMessage(new ChatComponentText("§3Key has been set!"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
