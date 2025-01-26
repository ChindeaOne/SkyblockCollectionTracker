package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigAccordionId;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bazaar {

    @Expose
    @ConfigOption(
            name = "Use Bazaar Prices",
            desc = "Use bazaar prices instead of NPC prices"
    )
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean useBazaar = false;

    @Expose
    @ConfigOption(
            name = "Select Bazaar Type",
            desc = "Select weather you want to use enchanted or enchanted block version"
    )
    @ConfigEditorDropdown(
            values = {"Enchanted version", "Enchanted block version"}
    )
    @ConfigAccordionId(id = 0)
    public List<Integer> bazaarType = new ArrayList<>(Arrays.asList(0, 1));
}
