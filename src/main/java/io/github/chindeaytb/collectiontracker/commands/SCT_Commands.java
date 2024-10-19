package io.github.chindeaytb.collectiontracker.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class SCT_Commands extends CommandBase {

    private final CommandHelper commandHelper;
    private final SetCollection setCollection;
    private final StopTracker stopTracker;
    private final GuiMenu guiMenu;
    private final MoveGui moveGui;

    public SCT_Commands(CommandHelper commandHelper, SetCollection setCollection, StopTracker stopTracker, GuiMenu guiMenu, MoveGui moveGui) {
        this.commandHelper = commandHelper;
        this.setCollection = setCollection;
        this.stopTracker = stopTracker;
        this.guiMenu = guiMenu;
        this.moveGui = moveGui;
    }

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
        if (args.length == 1) {
            // Provide completions for the first argument (commands)
            return CommandBase.getListOfStringsMatchingLastWord(args, "help", "track", "stop", "move");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("track")) {
            // Provide completions for the second argument (collection names) when the first argument is 'track'
            return CommandBase.getListOfStringsMatchingLastWord(args,
                    "gold", "iron", "redstone", "cobblestone", "netherrack", "endstone",
                    "diamond", "quartz", "obsidian", "gemstone", "umber", "coal",
                    "emerald", "glacite", "tungsten", "mycelium", "red sand", "hard stone", "sulphur");
        }

        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if (args.length == 0) {
            guiMenu.processCommand(sender, args);
            return;
        }

        // Route commands to their respective handlers
        switch (args[0].toLowerCase()) {
            case "help":
                commandHelper.processCommand(sender, args);
                break;
            case "track":
                setCollection.processCommand(sender, args);
                break;
            case "stop":
                stopTracker.processCommand(sender, args);
                break;
            case "move":
                moveGui.processCommand(sender, args);
                break;
            default:
                sender.addChatMessage(new ChatComponentText("Unknown command. Use /sct help."));
                break;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
