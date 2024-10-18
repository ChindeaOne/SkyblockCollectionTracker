package io.github.chindeaytb.collectiontracker.config;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.config.categories.Settings;
import io.github.chindeaytb.collectiontracker.init.ModInitialization;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.annotations.Category;

public class ModConfig extends Config {

    @Override
    public String getTitle() {
        String modName = "SkyblockCollectionTracker";
        return modName + ModInitialization.getVersion() + " by §Chindea_YTB§r, config by §5Moulberry §rand §5nea89";
    }

    @Override
    public void saveNow() {
        ModInitialization.configManager.save();
    }

//    @Expose
//    @Category(name = "Settings", desc = "Settings for the tracker.")
//    public Settings tracker = new Settings();
}
