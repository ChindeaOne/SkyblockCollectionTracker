package io.github.chindeaytb.collectiontracker.collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionsManager {

    public static final String[] COLLECTIONS = {
            "cocoa beans", "carrot", "cactus", "raw chicken", "sugar cane", "pumpkin", "wheat", "seeds", "red mushroom",
            "brown mushroom", "raw rabbit", "nether wart", "mutton", "melon", "potato", "leather", "porkchop", "feather",
            "lapis lazuli", "redstone", "umber", "coal", "mycelium", "end stone", "quartz", "sand", "iron", "amber",
            "topaz", "sapphire", "amethyst", "jasper", "ruby", "jade", "opal", "aquamarine", "citrine", "onyx", "peridot",
            "tungsten", "obsidian", "diamond", "cobblestone", "glowstone", "gold", "flint", "hard stone", "mithril",
            "emerald", "red sand", "ice", "glacite", "sulphur", "netherrack", "ender pearl", "chili pepper", "slimeball",
            "magma cream", "ghast tear", "gunpowder", "rotten flesh", "spider eye", "bone", "blaze rod", "string",
            "lily pad", "prismarine shard", "ink sac", "raw fish", "pufferfish", "clownfish", "raw salmon", "magmafish",
            "prismarine crystals", "clay", "sponge", "wilted berberis", "living metal heart", "caducous stem", "agaricus cap",
            "hemovibe", "half-eaten carrot", "timite"
    };

    public static final String[] SACK_COLLECTIONS = {
        "cropie", "squash", "rabbit foot", "rabbit hide", "titanium", "refined mineral", "glossy gemstone", "sludge juice", "yoggie"
    };

    private static final Set<String> COLLECTIONS_SET = new HashSet<>(Arrays.asList(COLLECTIONS));
    private static final Set<String> SACK_COLLECTIONS_SET = new HashSet<>(Arrays.asList(SACK_COLLECTIONS));

    public static String collection_source = null;

    public static boolean isValidCollection(String collectionName) {
        return COLLECTIONS_SET.contains(collectionName);
    }

    public static boolean isValidSackCollection(String collectionName) {
        return SACK_COLLECTIONS_SET.contains(collectionName);
    }

    public static boolean notRiftCollection(String collection) {
        return !collection.equals("wilted berberis") &&
                !collection.equals("living metal heart") &&
                !collection.equals("caducous stem") &&
                !collection.equals("agaricus cap") &&
                !collection.equals("hemovibe") &&
                !collection.equals("half-eaten carrot") &&
                !collection.equals("timite");
    }
}