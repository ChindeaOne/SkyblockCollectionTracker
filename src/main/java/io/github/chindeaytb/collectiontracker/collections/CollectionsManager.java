package io.github.chindeaytb.collectiontracker.collections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CollectionsManager {

    public static final String[] COLLECTIONS = {
            "cocoa beans", "carrot", "cactus", "raw chicken", "sugar cane", "pumpkin", "wheat", "seeds", "red mushroom",
            "brown mushroom", "raw rabbit", "nether wart", "mutton", "melon", "potato", "leather", "porkchop", "feather",
            "lapis lazuli", "redstone", "umber", "coal", "mycelium", "end stone", "quartz", "sand", "iron", "amber",
            "topaz", "sapphire", "amethyst", "jasper", "ruby", "jade", "opal", "aquamarine", "citrine", "onyx", "peridot",
            "tungsten", "obsidian", "diamond", "cobblestone", "glowstone", "gold", "flint", "hard stone", "mithril",
            "emerald", "red sand", "ice", "glacite", "sulphur", "netherrack", "ender pearl", "chili pepper", "slimeball",
            "magma cream", "ghast tear", "gunpowder", "rotten flesh", "spider eye", "bone", "blaze rod", "string",
            "acacia", "spruce", "jungle", "birch", "oak", "dark oak", "lily pad", "prismarine shard", "ink sac", "raw fish",
            "pufferfish", "clownfish", "raw salmon", "magmafish", "prismarine crystals", "clay", "sponge", "wilted berberis",
            "living metal heart", "caducous stem", "agaricus cap", "hemovibe", "half-eaten carrot", "timite"
    };

    public static final String[] SACK_COLLECTIONS = {
        "cropie", "squash", "rabbit foot", "rabbit hide", "titanium", "refined mineral", "glossy `gemstone`", "sludge juice",
        "ancient claw", "kuudra teeth", "plasma", "volta", "yoggie", "agarimoo tongue", "nurse shark tooth", "blue shark tooth",
        "tiger shark tooth", "shark fin", "chum", "carnival ticket", "white gift", "green gift", "red gift"
    };

    public static String collection_source = null;

    public static boolean isValidCollection(String collectionName) {
        return Arrays.asList(COLLECTIONS).contains(collectionName);
    }

    public static boolean isValidSackCollection(String collectionName) {
        return Arrays.asList(SACK_COLLECTIONS).contains(collectionName);
    }

    public static boolean notRiftCollection(String collection) {
        switch (collection) {
            case "wilted berberis":
            case "living metal heart":
            case "caducous stem":
            case "agaricus cap":
            case "hemovibe":
            case "half-eaten carrot":
            case "timite":
                return false;
            default:
                return true;
        }
    }
}