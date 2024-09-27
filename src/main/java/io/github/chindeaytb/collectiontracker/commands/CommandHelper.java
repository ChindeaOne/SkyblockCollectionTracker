package io.github.chindeaytb.collectiontracker.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper extends CommandBase {
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
            completions.add("help");
        }
        return completions;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if (args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
        } else {
            sender.addChatMessage(new ChatComponentText("Unknown command. Use /sct help."));
        }
    }

    private void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("                   <Info>"));
        sender.addChatMessage(new ChatComponentText("§a◆/sct track => Tracks your Skyblock collection."));
        sender.addChatMessage(new ChatComponentText("§a◆/sct stop => Stops tracking."));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
