package io.github.chindeaytb.collectiontracker.config.categories;

import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

@SuppressWarnings("unused")
public class GUIConfig {

    @ConfigOption(name = "Edit GUI Location", desc = "Allows the player to change the position of the tracker.")
    @ConfigEditorButton(buttonText = "Edit")
    public transient Void a = null;

}
