package io.github.chindeaytb.collectiontracker.collections.prices;

public class NPCPrice {

    public static float getNpcPrice(String collection) {

        switch (collection) {
            // Farming
            case "cactus":
                return 4;
            case "carrot":
                return 3;
            case "cocoa beans":
                return 3;
            case "feather":
                return 3;
            case "leather":
                return 3;
            case "melon":
                return 2;
            case "mushroom":
                return 5;
            case "mutton":
                return 5;
            case "nether wart":
                return 4;
            case "potato":
                return 3;
            case "pumpkin":
                return 10;
            case "raw chicken":
                return 4;
            case "porkchop":
                return 4;
            case "raw rabbit":
                return 4;
            case "seeds":
                return 3;
            case "sugar cane":
                return 4;
            case "wheat":
                return 6;

            // Mining
            case "coal":
                return 1;
            case "cobblestone":
                return 1;
            case "diamond":
                return 8;
            case "emerald":
                return 4;
            case "end stone":
                return 2;
            case "gemstone":
                return 3;
            case "glacite":
                return 12;
            case "glowstone":
                return 2;
            case "gold":
                return 3;
            case "gravel":
                return 3;
            case "hard stone":
                return 1;
            case "ice":
                return 0.5f;
            case "iron":
                return 2;
            case "lapis lazuli":
                return 1;
            case "mithril":
                return 8;
            case "mycelium":
                return 5;
            case "quartz":
                return 4;
            case "nertherack":
                return 1;
            case "obsidian":
                return 7;
            case "red sand":
                return 5;
            case "redstone":
                return 1;
            case "sand":
                return 2;
            case "sulphur":
                return 10;
            case "tungsten":
                return 10;
            case "umber":
                return 10;

            // Combat
            case "blaze rod":
                return 9;
            case "bone":
                return 2;
            case "chili pepper":
                return 5000;
            case "ender pearl":
                return 7;
            case "ghast tear":
                return 16;
            case "gunpowder":
                return 4;
            case "magma cream":
                return 8;
            case "rotten flesh":
                return 2;
            case "slimeball":
                return 5;
            case "spider eye":
                return 3;
            case "string":
                return 3;

            // Foraging
            case "acacia":
                return 2;
            case "birch":
                return 2;
            case "dark oak":
                return 2;
            case "jungle":
                return 2;
            case "oak":
                return 2;
            case "spruce":
                return 2;

            // Fishing
            case "clay":
                return 3;
            case "clownfish":
                return 20;
            case "ink sac":
                return 2;
            case "lily pad":
                return 10;
            case "magmafish":
                return 20;
            case "prismarine crystals":
                return 5;
            case "prismarine shard":
                return 5;
            case "pufferfish":
                return 15;
            case "raw fish":
                return 6;
            case "raw salmon":
                return 10;
            case "sponge":
                return 50;

            default:
                return -1;
        }
    }

    public static boolean notRiftCollection(String collection) {
        switch (collection) {
            case "wilted berberis":
                return false;
            case "living metal heart":
                return false;
            case "caducous stem":
                return false;
            case "agaricus cap":
                return false;
            case "hemovibe":
                return false;
            case "half-eaten carrot":
                return false;
            case "timite":
                return false;
            default:
                return true;
        }
    }
}
