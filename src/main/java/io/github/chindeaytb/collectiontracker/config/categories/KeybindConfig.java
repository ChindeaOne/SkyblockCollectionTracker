package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorKeybind;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import org.lwjgl.input.Keyboard;

public class KeybindConfig {

    @Expose
    @ConfigOption(name = "Enable Commissions keybinds", desc = "Lets you use your number keys to quickly claim your commissions")
    @ConfigEditorBoolean
    public boolean enableCommissionsKeybinds = false;

    @Expose
    @ConfigOption(name = "Commission 1", desc = "Keybind to claim the first commission")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_1)
    public int commission1 = Keyboard.KEY_1;

    @Expose
    @ConfigOption(name = "Commission 2", desc = "Keybind to claim the second commission")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_2)
    public int commission2 = Keyboard.KEY_2;

    @Expose
    @ConfigOption(name = "Commission 3", desc = "Keybind to claim the third commission")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_3)
    public int commission3 = Keyboard.KEY_3;

    @Expose
    @ConfigOption(name = "Commission 4", desc = "Keybind to claim the fourth commission")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_4)
    public int commission4 = Keyboard.KEY_4;
}
