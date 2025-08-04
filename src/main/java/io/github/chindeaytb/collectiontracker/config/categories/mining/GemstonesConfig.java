package io.github.chindeaytb.collectiontracker.config.categories.mining;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class GemstonesConfig {

    @Expose
    @ConfigOption(name = "Gemstone variants", desc = "Choose which gemstone variants to track.")
    @ConfigEditorDropdown(
            values = {"Rough", "Flawed", "Fine", "Flawless", "Perfect"}
    )
    public String gemstoneVariants = "Fine"; // Default to Fine variant
}
