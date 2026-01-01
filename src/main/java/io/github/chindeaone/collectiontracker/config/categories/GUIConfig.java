package io.github.chindeaone.collectiontracker.config.categories;

import io.github.chindeaone.collectiontracker.gui.GuiManager;
import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

@SuppressWarnings("unused")
public class GUIConfig {

    @ConfigOption(name = "Edit GUI Location", desc = "Allows the player to change the position of the overlays.")
    @ConfigEditorButton(buttonText = "Edit")
    public Runnable positions = GuiManager::openGuiPositionEditor;

}
