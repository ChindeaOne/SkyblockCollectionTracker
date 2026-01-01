package io.github.chindeaone.collectiontracker.commands;

import io.github.chindeaone.collectiontracker.tracker.TrackingHandlerClass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

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
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            TrackingHandlerClass.stopTrackingManual();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
