package io.github.chindeaytb.collectiontracker.collections;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;

public class CollectionsManager {

    public static boolean collectionMatches(String collectionName) {
        switch (collection) {

            // Farming
            case "cocoa beans":
                return collectionName.equals("INK_SACK:3");
            case "carrot":
                return collectionName.equals("CARROT_ITEM");
            case "cactus":
                return collectionName.equals("CACTUS");
            case "raw chicken":
                return collectionName.equals("RAW_CHICKEN");
            case "sugar cane":
                return collectionName.equals("SUGAR_CANE");
            case "pumpkin":
                return collectionName.equals("PUMPKIN");
            case "wheat":
                return collectionName.equals("WHEAT");
            case "seeds":
                return collectionName.equals("SEEDS");
            case "mushroom":
                return collectionName.equals("MUSHROOM_COLLECTION");
            case "raw rabbit":
                return collectionName.equals("RABBIT");
            case "nether wart":
                return collectionName.equals("NETHER_STALK");
            case "mutton":
                return collectionName.equals("MUTTON");
            case "melon":
                return collectionName.equals("MELON");
            case "potato":
                return collectionName.equals("POTATO_ITEM");
            case "leather":
                return collectionName.equals("LEATHER");
            case "porkchop":
                return collectionName.equals("PORK");
            case "feather":
                return collectionName.equals("FEATHER");

            // Mining
            case "lapis lazuli":
                return collectionName.equals("INK_SACK:4");
            case "redstone":
                return collectionName.equals("REDSTONE");
            case "umber":
                return collectionName.equals("UMBER");
            case "coal":
                return collectionName.equals("COAL");
            case "mycelium":
                return collectionName.equals("MYCEL");
            case "endstone":
                return collectionName.equals("ENDER_STONE");
            case "quartz":
                return collectionName.equals("QUARTZ");
            case "sand":
                return collectionName.equals("SAND");
            case "iron":
                return collectionName.equals("IRON_INGOT");
            case "gemstone":
                return collectionName.equals("GEMSTONE_COLLECTION");
            case "tungsten":
                return collectionName.equals("TUNGSTEN");
            case "obsidian":
                return collectionName.equals("OBSIDIAN");
            case "diamond":
                return collectionName.equals("DIAMOND");
            case "cobblestone":
                return collectionName.equals("COBBLESTONE");
            case "glowstone":
                return collectionName.equals("GLOWSTONE_DUST");
            case "gold":
                return collectionName.equals("GOLD_INGOT");
            case "gravel":
                return collectionName.equals("GRAVEL");
            case "hard stone":
                return collectionName.equals("HARD_STONE");
            case "mithril":
                return collectionName.equals("MITHRIL_ORE");
            case "emerald":
                return collectionName.equals("EMERALD");
            case "red sand":
                return collectionName.equals("SAND:1");
            case "ice":
                return collectionName.equals("ICE");
            case "glacite":
                return collectionName.equals("GLACITE");
            case "sulphur":
                return collectionName.equals("SULPHUR_ORE");
            case "netherrack":
                return collectionName.equals("NETHERRACK");

            // Combat
            case "ender pearl":
                return collectionName.equals("ENDER_PEARL");
            case "chili pepper":
                return collectionName.equals("HOT_PEPPER");
            case "slimeball":
                return collectionName.equals("SLIMEBALL");
            case "magma cream":
                return collectionName.equals("MAGMA_CREAM");
            case "ghast tear":
                return collectionName.equals("GHAST_TEAR");
            case "gunpowder":
                return collectionName.equals("SULPHUR");
            case "rotten flesh":
                return collectionName.equals("ROTTEN_FLESH");
            case "spider eye":
                return collectionName.equals("SPIDER_EYE");
            case "bone":
                return collectionName.equals("BONE");
            case "blaze rod":
                return collectionName.equals("BLAZE_ROD");
            case "string":
                return collectionName.equals("STRING");

            // Foraging
            case "acacia wood":
                return collectionName.equals("LOG_2");
            case "spruce wood":
                return collectionName.equals("LOG:1");
            case "jungle wood":
                return collectionName.equals("LOG:3");
            case "birch wood":
                return collectionName.equals("LOG:2");
            case "oak wood":
                return collectionName.equals("LOG");
            case "dark oak wood":
                return collectionName.equals("LOG_2:1");

            // Fishing
            case "lily pad":
                return collectionName.equals("WATER_LILY");
            case "prismarine shard":
                return collectionName.equals("PRISMARINE_SHARD");
            case "ink sac":
                return collectionName.equals("INK_SACK");
            case "raw fish":
                return collectionName.equals("RAW_FISH");
            case "pufferfish":
                return collectionName.equals("RAW_FISH:3");
            case "clownfish":
                return collectionName.equals("RAW_FISH:2");
            case "raw salmon":
                return collectionName.equals("RAW_FISH:1");
            case "magmafish":
                return collectionName.equals("MAGMA_FISH");
            case "prismarine crystal":
                return collectionName.equals("PRISMARINE_CRYSTALS");
            case "clay":
                return collectionName.equals("CLAY_BALL");
            case "sponge":
                return collectionName.equals("SPONGE");

            // Rift
            case "wilted berberis":
                return collectionName.equals("WILTED_BERBERIS");
            case "living metal heart":
                return collectionName.equals("METAL_HEART");
            case "caducous stem":
                return collectionName.equals("CADUCOUS_STEM");
            case "agaricus cap":
                return collectionName.equals("AGARICUS_CAP");
            case "hemovibe":
                return collectionName.equals("HEMOVIBE");
            case "half-eaten carrot":
                return collectionName.equals("HALF_EATEN_CARROT");

            default:
                return false;
        }
    }
}
