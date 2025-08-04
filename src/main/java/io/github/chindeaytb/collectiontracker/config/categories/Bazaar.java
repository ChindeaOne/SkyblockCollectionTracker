package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.config.categories.bazaar.BazaarConfig;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class Bazaar {
    @Expose
    @ConfigOption(
            name = "Toggle Bazaar",
            desc = "Enable or disable the Bazaar feature."
    )
    @Accordion
    public BazaarConfig bazaarConfig = new BazaarConfig();

}
