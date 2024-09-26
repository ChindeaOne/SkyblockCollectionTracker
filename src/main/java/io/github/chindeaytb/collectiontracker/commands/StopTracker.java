package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.tracker.HypixelApiFetcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class StopTracker extends CommandBase {

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct stop";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("stop");
        }
        return completions;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            HypixelApiFetcher.stopTracking(sender);
        } else {
            sender.addChatMessage(new ChatComponentText("Use: /sct stop to stop tracking."));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
