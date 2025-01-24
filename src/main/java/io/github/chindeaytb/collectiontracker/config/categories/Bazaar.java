package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class Bazaar {

    @Expose
    @ConfigOption(
            name = "Use Bazaar Prices",
            desc = "Use bazaar prices instead of NPC prices"
    )
    @ConfigEditorBoolean
    public boolean useBazaar = false;

    @Expose
    @ConfigOption(
            name = "",
            desc = "Select weather you want to use enchanted or enchanted block version"
    )
    @ConfigEditorDropdown(
            values = {"Enchanted", "Enchanted Block"}
    )
    public int bazaarType = 0;
}
