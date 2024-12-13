package io.github.chindeaytb.collectiontracker.collections;

public class ValidCollectionsManager {

    public static boolean isValidCollection(String collectionName) {
        switch (collectionName) {
            case "gold":
            case "iron":
            case "redstone":
            case "cobblestone":
            case "netherrack":
            case "endstone":
            case "diamond":
            case "quartz":
            case "obsidian":
            case "gemstone":
            case "umber":
            case "coal":
            case "emerald":
            case "glacite":
            case "tungsten":
            case "mithril":
            case "mycelium":
            case "red sand":
            case "hard stone":
            case "cocoa beans":
            case "carrot":
            case "cactus":
            case "raw chicken":
            case "sugar cane":
            case "pumpkin":
            case "wheat":
            case "seeds":
            case "mushroom":
            case "raw rabbit":
            case "nether wart":
            case "mutton":
            case "melon":
            case "potato":
            case "leather":
            case "porkchop":
            case "feather":
            case "lapis lazuli":
            case "glowstone":
            case "gravel":
            case "slimeball":
            case "magma cream":
            case "ghast tear":
            case "gunpowder":
            case "rotten flesh":
            case "spider eye":
            case "bone":
            case "blaze rod":
            case "string":
            case "acacia wood":
            case "spruce wood":
            case "jungle wood":
            case "birch wood":
            case "oak wood":
            case "dark oak wood":
            case "lily pad":
            case "prismarine shard":
            case "ink sac":
            case "raw fish":
            case "pufferfish":
            case "clownfish":
            case "raw salmon":
            case "magmafish":
            case "prismarine crystal":
            case "clay":
            case "sponge":
            case "wilted berberis":
            case "living metal heart":
            case "caducous stem":
            case "agaricus cap":
            case "hemovibe":
            case "half-eaten carrot":
            case "timite":
                return true;
            default:
                return false;
        }
    }

}
