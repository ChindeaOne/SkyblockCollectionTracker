package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.config.categories.mining.GemstonesConfig;
import io.github.chindeaytb.collectiontracker.config.categories.mining.KeybindConfig;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class Mining {

    @Expose
    @ConfigOption(name = "Commissions Keybinds", desc = "")
    @Accordion
    public KeybindConfig commissions = new KeybindConfig();

    @Expose
    @ConfigOption(name = "Gemstone Variants", desc = "Choose which gemstone variants to track.")
    @Accordion
    public GemstonesConfig gemstones = new GemstonesConfig();
}
