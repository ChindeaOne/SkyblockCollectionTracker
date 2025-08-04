package io.github.chindeaytb.collectiontracker.config.categories.bazaar;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class BazaarConfig {
    @Expose
    @ConfigOption(
            name = "Use Bazaar Prices",
            desc = "Toggle to use bazaar prices instead of NPC prices"
    )
    @ConfigEditorBoolean
    public boolean useBazaar = false;

    @Expose
    @ConfigOption(
            name = "Select Bazaar Type",
            desc = "Select which type of bazaar version for the collection to use"
    )
    @ConfigEditorDropdown(
            values = {"Enchanted version", "Super enchanted version"}
    )
    public String bazaarType = "Enchanted version";
}
