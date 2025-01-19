package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class PauseTracker extends CommandBase {

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct pause";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("pause")) {
            TrackingHandlerClass.pauseTracking(sender);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}