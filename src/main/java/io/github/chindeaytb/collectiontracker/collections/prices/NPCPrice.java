package io.github.chindeaytb.collectiontracker.collections.prices;

import java.util.HashMap;
import java.util.Map;

public class NPCPrice {

    private static final Map<String, Integer> collectionPrices = new HashMap<>();

    static {
        // Farming
        collectionPrices.put("cactus", 4);
        collectionPrices.put("carrot", 3);
        collectionPrices.put("cocoa beans", 3);
        collectionPrices.put("feather", 3);
        collectionPrices.put("leather", 3);
        collectionPrices.put("melon", 2);
        collectionPrices.put("red mushroom", 5);
        collectionPrices.put("brown mushroom", 5);
        collectionPrices.put("raw rabbit", 4);
        collectionPrices.put("rabbit foot", 5);
        collectionPrices.put("rabbit hide", 5);
        collectionPrices.put("nether wart", 4);
        collectionPrices.put("mutton", 5);
        collectionPrices.put("potato", 3);
        collectionPrices.put("pumpkin", 10);
        collectionPrices.put("raw chicken", 4);
        collectionPrices.put("porkchop", 4);
        collectionPrices.put("seeds", 3);
        collectionPrices.put("sugar cane", 4);
        collectionPrices.put("wheat", 6);

        // Mining
        collectionPrices.put("coal", 1);
        collectionPrices.put("cobblestone", 1);
        collectionPrices.put("diamond", 8);
        collectionPrices.put("emerald", 4);
        collectionPrices.put("end stone", 2);
        collectionPrices.put("amber", 3);
        collectionPrices.put("topaz", 3);
        collectionPrices.put("sapphire", 3);
        collectionPrices.put("amethyst", 3);
        collectionPrices.put("jasper", 3);
        collectionPrices.put("ruby", 3);
        collectionPrices.put("jade", 3);
        collectionPrices.put("opal", 3);
        collectionPrices.put("aquamarine", 3);
        collectionPrices.put("citrine", 3);
        collectionPrices.put("onyx", 3);
        collectionPrices.put("peridot", 3);
        collectionPrices.put("glacite", 12);
        collectionPrices.put("glowstone", 2);
        collectionPrices.put("gold", 3);
        collectionPrices.put("flint", 4);
        collectionPrices.put("hard stone", 1);
        collectionPrices.put("ice", 1);
        collectionPrices.put("iron", 2);
        collectionPrices.put("lapis lazuli", 1);
        collectionPrices.put("mithril", 8);
        collectionPrices.put("mycelium", 5);
        collectionPrices.put("quartz", 4);
        collectionPrices.put("nertherack", 1);
        collectionPrices.put("obsidian", 7);
        collectionPrices.put("red sand", 5);
        collectionPrices.put("redstone", 1);
        collectionPrices.put("sand", 2);
        collectionPrices.put("sulphur", 10);
        collectionPrices.put("tungsten", 10);
        collectionPrices.put("umber", 10);

        // Combat
        collectionPrices.put("blaze rod", 9);
        collectionPrices.put("bone", 2);
        collectionPrices.put("chili pepper", 5000);
        collectionPrices.put("ender pearl", 7);
        collectionPrices.put("ghast tear", 16);
        collectionPrices.put("gunpowder", 4);
        collectionPrices.put("magma cream", 8);
        collectionPrices.put("rotten flesh", 2);
        collectionPrices.put("slimeball", 5);
        collectionPrices.put("spider eye", 3);
        collectionPrices.put("string", 3);

        // Foraging
        collectionPrices.put("acacia", 2);
        collectionPrices.put("birch", 2);
        collectionPrices.put("dark oak", 2);
        collectionPrices.put("jungle", 2);
        collectionPrices.put("oak", 2);
        collectionPrices.put("spruce", 2);

        // Fishing
        collectionPrices.put("clay", 3);
        collectionPrices.put("clownfish", 20);
        collectionPrices.put("ink sac", 2);
        collectionPrices.put("lily pad", 10);
        collectionPrices.put("magmafish", 20);
        collectionPrices.put("prismarine crystals", 5);
        collectionPrices.put("prismarine shard", 5);
        collectionPrices.put("pufferfish", 15);
        collectionPrices.put("raw fish", 6);
        collectionPrices.put("raw salmon", 10);
        collectionPrices.put("sponge", 50);
    }

    public static int getNpcPrice(String collection) {
        return collectionPrices.getOrDefault(collection, -1);
    }
}