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

    public SCT_Commands(CommandHelper commandHelper, SetCollection setCollection, StopTracker stopTracker, GuiMenu guiMenu) {
        this.commandHelper = commandHelper;
        this.setCollection = setCollection;
        this.stopTracker = stopTracker;
        this.guiMenu = guiMenu;
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
            return CommandBase.getListOfStringsMatchingLastWord(args, "help", "track", "stop", "pause", "resume");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("track")) {
            return CommandBase.getListOfStringsMatchingLastWord(args,
                    "cocoa beans", "carrot", "cactus", "raw chicken", "sugar cane",
                    "pumpkin", "wheat", "seeds", "mushroom", "raw rabbit", "nether wart",
                    "mutton", "melon", "potato", "leather", "porkchop", "feather",
                    "lapis lazuli", "redstone", "umber", "coal", "mycelium", "end stone",
                    "quartz", "sand", "iron", "gemstone", "tungsten", "obsidian",
                    "diamond", "cobblestone", "glowstone", "gold", "gravel", "hard stone",
                    "mithril", "emerald", "red sand", "ice", "glacite", "sulphur",
                    "netherrack", "ender pearl", "chili pepper", "slimeball", "magma cream",
                    "ghast tear", "gunpowder", "rotten flesh", "spider eye", "bone",
                    "blaze rod", "string", "acacia", "spruce", "jungle", "birch", "oak",
                    "dark oak", "lily pad", "prismarine shard", "ink sac", "raw fish",
                    "pufferfish", "clownfish", "raw salmon", "magmafish", "prismarine crystals",
                    "clay", "sponge", "wilted berberis", "living metal heart", "caducous stem",
                    "agaricus cap", "hemovibe", "half-eaten carrot", "timite");
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if (args.length == 0) {
            guiMenu.processCommand(sender, args);
            return;
        }

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
