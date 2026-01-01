package io.github.chindeaone.collectiontracker.config.categories.overlay;

import com.google.gson.annotations.Expose;
import io.github.chindeaone.collectiontracker.config.core.ConfigLink;
import io.github.chindeaone.collectiontracker.config.core.Position;
import io.github.moulberry.moulconfig.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverlaySingle {

    @Expose
    @ConfigOption(
            name = "Overlay Text",
            desc = "Drag text to change the appearance of the overlaySingle."
    )
    @ConfigEditorDraggableList(
            exampleText = {
                    "§aGold collection §f> 200.000M",
                    "§aGold collection (session) §f> 10.000M",
                    "§aColl/h §f> Calculating...",
                    "§a$/h (NPC/Bazaar) §f> 100k/h",
                    "§aExtras"
            }
    )
    public List<Integer> statsText = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));

    @Expose
    @ConfigLink(owner = OverlaySingle.class, field = "singleStatsOverlay")
    public Position overlayPosition = new Position(100, 150);
}
