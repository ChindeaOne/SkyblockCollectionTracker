package io.github.chindeaytb.collectiontracker.collections;

import java.util.HashMap;
import java.util.Map;

public class CollectionsManager {

    private static final Map<String, String> collectionMappings = new HashMap<>();

    static {
        // Farming
        collectionMappings.put("cocoa beans", "INK_SACK:3");
        collectionMappings.put("carrot", "CARROT_ITEM");
        collectionMappings.put("cactus", "CACTUS");
        collectionMappings.put("raw chicken", "RAW_CHICKEN");
        collectionMappings.put("sugar cane", "SUGAR_CANE");
        collectionMappings.put("pumpkin", "PUMPKIN");
        collectionMappings.put("wheat", "WHEAT");
        collectionMappings.put("seeds", "SEEDS");
        collectionMappings.put("mushroom", "MUSHROOM_COLLECTION");
        collectionMappings.put("raw rabbit", "RABBIT");
        collectionMappings.put("nether wart", "NETHER_STALK");
        collectionMappings.put("mutton", "MUTTON");
        collectionMappings.put("melon", "MELON");
        collectionMappings.put("potato", "POTATO_ITEM");
        collectionMappings.put("leather", "LEATHER");
        collectionMappings.put("porkchop", "PORK");
        collectionMappings.put("feather", "FEATHER");

        // Mining
        collectionMappings.put("lapis lazuli", "INK_SACK:4");
        collectionMappings.put("redstone", "REDSTONE");
        collectionMappings.put("umber", "UMBER");
        collectionMappings.put("coal", "COAL");
        collectionMappings.put("mycelium", "MYCEL");
        collectionMappings.put("end stone", "ENDER_STONE");
        collectionMappings.put("quartz", "QUARTZ");
        collectionMappings.put("sand", "SAND");
        collectionMappings.put("iron", "IRON_INGOT");
        collectionMappings.put("gemstone:amber", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:topaz", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:sapphire", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:amethyst", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:jasper", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:ruby", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:jade", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:opal", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:aquamarine", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:citrine", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:onyx", "GEMSTONE_COLLECTION");
        collectionMappings.put("gemstone:peridot", "GEMSTONE_COLLECTION");
        collectionMappings.put("tungsten", "TUNGSTEN");
        collectionMappings.put("obsidian", "OBSIDIAN");
        collectionMappings.put("diamond", "DIAMOND");
        collectionMappings.put("cobblestone", "COBBLESTONE");
        collectionMappings.put("glowstone", "GLOWSTONE_DUST");
        collectionMappings.put("gold", "GOLD_INGOT");
        collectionMappings.put("gravel", "GRAVEL");
        collectionMappings.put("hard stone", "HARD_STONE");
        collectionMappings.put("mithril", "MITHRIL_ORE");
        collectionMappings.put("emerald", "EMERALD");
        collectionMappings.put("red sand", "SAND:1");
        collectionMappings.put("ice", "ICE");
        collectionMappings.put("glacite", "GLACITE");
        collectionMappings.put("sulphur", "SULPHUR_ORE");
        collectionMappings.put("netherrack", "NETHERRACK");

        // Combat
        collectionMappings.put("ender pearl", "ENDER_PEARL");
        collectionMappings.put("chili pepper", "HOT_PEPPER");
        collectionMappings.put("slimeball", "SLIMEBALL");
        collectionMappings.put("magma cream", "MAGMA_CREAM");
        collectionMappings.put("ghast tear", "GHAST_TEAR");
        collectionMappings.put("gunpowder", "SULPHUR");
        collectionMappings.put("rotten flesh", "ROTTEN_FLESH");
        collectionMappings.put("spider eye", "SPIDER_EYE");
        collectionMappings.put("bone", "BONE");
        collectionMappings.put("blaze rod", "BLAZE_ROD");
        collectionMappings.put("string", "STRING");

        // Foraging
        collectionMappings.put("acacia", "LOG_2");
        collectionMappings.put("spruce", "LOG:1");
        collectionMappings.put("jungle", "LOG:3");
        collectionMappings.put("birch", "LOG:2");
        collectionMappings.put("oak", "LOG");
        collectionMappings.put("dark oak", "LOG_2:1");

        // Fishing
        collectionMappings.put("lily pad", "WATER_LILY");
        collectionMappings.put("prismarine shard", "PRISMARINE_SHARD");
        collectionMappings.put("ink sac", "INK_SACK");
        collectionMappings.put("raw fish", "RAW_FISH");
        collectionMappings.put("pufferfish", "RAW_FISH:3");
        collectionMappings.put("clownfish", "RAW_FISH:2");
        collectionMappings.put("raw salmon", "RAW_FISH:1");
        collectionMappings.put("magmafish", "MAGMA_FISH");
        collectionMappings.put("prismarine crystals", "PRISMARINE_CRYSTALS");
        collectionMappings.put("clay", "CLAY_BALL");
        collectionMappings.put("sponge", "SPONGE");

        // Rift
        collectionMappings.put("wilted berberis", "WILTED_BERBERIS");
        collectionMappings.put("living metal heart", "METAL_HEART");
        collectionMappings.put("caducous stem", "CADUCOUS_STEM");
        collectionMappings.put("agaricus cap", "AGARICUS_CAP");
        collectionMappings.put("hemovibe", "HEMOVIBE");
        collectionMappings.put("half-eaten carrot", "HALF_EATEN_CARROT");
        collectionMappings.put("timite", "TIMITE");
    }

    public static String toHypixelCollection(String collectionName) {
        return collectionMappings.get(collectionName);
    }
}