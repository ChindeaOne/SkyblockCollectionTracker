package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.gui.MoveableOverlay;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;

public class MoveGui extends CommandBase {
    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct move";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(!isTracking){
            sender.addChatMessage(new ChatComponentText("§cNo tracking active!"));
        }else if (args.length > 0 && args[0].equalsIgnoreCase("move")) {
            MoveableOverlay.activateDragging();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
