package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;


public class GuiMenu extends CommandBase {
    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ModInitialization.configManager.openConfigGui();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
