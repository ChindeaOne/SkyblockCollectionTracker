package io.github.chindeaytb.collectiontracker.collections;

public class ValidCollectionsManager {

    public static boolean isValidCollection(String collectionName) {
        switch (collectionName) {
            // Farming
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

                // Mining
            case "lapis lazuli":
            case "redstone":
            case "umber":
            case "coal":
            case "mycelium":
            case "end stone":
            case "quartz":
            case "sand":
            case "iron":
            case "gemstone":
            case "tungsten":
            case "obsidian":
            case "diamond":
            case "cobblestone":
            case "glowstone":
            case "gold":
            case "gravel":
            case "hard stone":
            case "mithril":
            case "emerald":
            case "red sand":
            case "ice":
            case "glacite":
            case "sulphur":
            case "netherrack":

                // Combat
            case "ender pearl":
            case "chili pepper":
            case "slimeball":
            case "magma cream":
            case "ghast tear":
            case "gunpowder":
            case "rotten flesh":
            case "spider eye":
            case "bone":
            case "blaze rod":
            case "string":

                // Foraging
            case "acacia":
            case "spruce":
            case "jungle":
            case "birch":
            case "oak":
            case "dark oak":

                // Fishing
            case "lily pad":
            case "prismarine shard":
            case "ink sac":
            case "raw fish":
            case "pufferfish":
            case "clownfish":
            case "raw salmon":
            case "magmafish":
            case "prismarine crystals":
            case "clay":
            case "sponge":

                // Rift
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