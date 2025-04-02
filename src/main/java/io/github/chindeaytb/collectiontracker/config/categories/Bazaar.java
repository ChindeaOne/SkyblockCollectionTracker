package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigAccordionId;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class Bazaar {
    @Expose
    @ConfigOption(
            name = "Use Bazaar Prices",
            desc = "Toggle to use bazaar prices instead of NPC prices"
            )
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean useBazaar = false;

    @Expose
    @ConfigOption(
            name = "Select Bazaar Type",
            desc = "Select which type of bazaar version for the collection to use"
    )
    @ConfigEditorDropdown(
        values = {"Enchanted version", "Enchanted block version"}
    )
    @ConfigAccordionId(id = 0)
    public int bazaarType = 0;
}
