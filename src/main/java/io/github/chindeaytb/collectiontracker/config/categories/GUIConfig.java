package io.github.chindeaytb.collectiontracker.config.categories;

import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import io.github.chindeaytb.collectiontracker.gui.GuiManager;

@SuppressWarnings("unused")
public class GUIConfig {

    @ConfigOption(name = "Edit GUI Location", desc = "Allows the player to change the position of the tracker.")
    @ConfigEditorButton(buttonText = "Edit")
    public Runnable positions = GuiManager::openGuiPositionEditor;

}
