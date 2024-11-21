package io.github.chindeaytb.collectiontracker.collections;

public class CollectionsManager {

    public static String toHypixelCollection(String collectionName) {
        switch (collectionName) {
            // Farming
            case "cocoa beans":
                return "INK_SACK:3";
            case "carrot":
                return "CARROT_ITEM";
            case "cactus":
                return "CACTUS";
            case "raw chicken":
                return "RAW_CHICKEN";
            case "sugar cane":
                return "SUGAR_CANE";
            case "pumpkin":
                return "PUMPKIN";
            case "wheat":
                return "WHEAT";
            case "seeds":
                return "SEEDS";
            case "mushroom":
                return "MUSHROOM_COLLECTION";
            case "raw rabbit":
                return "RABBIT";
            case "nether wart":
                return "NETHER_STALK";
            case "mutton":
                return "MUTTON";
            case "melon":
                return "MELON";
            case "potato":
                return "POTATO_ITEM";
            case "leather":
                return "LEATHER";
            case "porkchop":
                return "PORK";
            case "feather":
                return "FEATHER";

            // Mining
            case "lapis lazuli":
                return "INK_SACK:4";
            case "redstone":
                return "REDSTONE";
            case "umber":
                return "UMBER";
            case "coal":
                return "COAL";
            case "mycelium":
                return "MYCEL";
            case "endstone":
                return "ENDER_STONE";
            case "quartz":
                return "QUARTZ";
            case "sand":
                return "SAND";
            case "iron":
                return "IRON_INGOT";
            case "gemstone":
                return "GEMSTONE_COLLECTION";
            case "tungsten":
                return "TUNGSTEN";
            case "obsidian":
                return "OBSIDIAN";
            case "diamond":
                return "DIAMOND";
            case "cobblestone":
                return "COBBLESTONE";
            case "glowstone":
                return "GLOWSTONE_DUST";
            case "gold":
                return "GOLD_INGOT";
            case "gravel":
                return "GRAVEL";
            case "hard stone":
                return "HARD_STONE";
            case "mithril":
                return "MITHRIL_ORE";
            case "emerald":
                return "EMERALD";
            case "red sand":
                return "SAND:1";
            case "ice":
                return "ICE";
            case "glacite":
                return "GLACITE";
            case "sulphur":
                return "SULPHUR_ORE";
            case "netherrack":
                return "NETHERRACK";

            // Combat
            case "ender pearl":
                return "ENDER_PEARL";
            case "chili pepper":
                return "HOT_PEPPER";
            case "slimeball":
                return "SLIMEBALL";
            case "magma cream":
                return "MAGMA_CREAM";
            case "ghast tear":
                return "GHAST_TEAR";
            case "gunpowder":
                return "SULPHUR";
            case "rotten flesh":
                return "ROTTEN_FLESH";
            case "spider eye":
                return "SPIDER_EYE";
            case "bone":
                return "BONE";
            case "blaze rod":
                return "BLAZE_ROD";
            case "string":
                return "STRING";

            // Foraging
            case "acacia wood":
                return "LOG_2";
            case "spruce wood":
                return "LOG:1";
            case "jungle wood":
                return "LOG:3";
            case "birch wood":
                return "LOG:2";
            case "oak wood":
                return "LOG";
            case "dark oak wood":
                return "LOG_2:1";

            // Fishing
            case "lily pad":
                return "WATER_LILY";
            case "prismarine shard":
                return "PRISMARINE_SHARD";
            case "ink sac":
                return "INK_SACK";
            case "raw fish":
                return "RAW_FISH";
            case "pufferfish":
                return "RAW_FISH:3";
            case "clownfish":
                return "RAW_FISH:2";
            case "raw salmon":
                return "RAW_FISH:1";
            case "magmafish":
                return "MAGMA_FISH";
            case "prismarine crystal":
                return "PRISMARINE_CRYSTALS";
            case "clay":
                return "CLAY_BALL";
            case "sponge":
                return "SPONGE";

            // Rift
            case "wilted berberis":
                return "WILTED_BERBERIS";
            case "living metal heart":
                return "METAL_HEART";
            case "caducous stem":
                return "CADUCOUS_STEM";
            case "agaricus cap":
                return "AGARICUS_CAP";
            case "hemovibe":
                return "HEMOVIBE";
            case "half-eaten carrot":
                return "HALF_EATEN_CARROT";

            default:
                return null;
        }
    }
}
