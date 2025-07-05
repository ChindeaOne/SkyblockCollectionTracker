package io.github.chindeaytb.collectiontracker.collections;

import java.util.HashMap;
import java.util.Map;

public class BazaarCollectionsManager {

    public static int enchantedCollectionCraft(String collection, Boolean useBazaar, Integer type) {

        if(!useBazaar) {
            return 1;
        }

        if (type == 1) {
            return enchantedBlockRecipe.getOrDefault(collection, 1);
        } else {
            return enchantedRecipe.getOrDefault(collection, 1);
        }
    }

    private static final Map<String, Integer> enchantedRecipe = new HashMap<>();
    private static final Map<String, Integer> enchantedBlockRecipe = new HashMap<>();

    static {
        // Enchanted Recipes
        // Farming
        enchantedRecipe.put("cocoa", 160);
        enchantedRecipe.put("carrot", 160);
        enchantedRecipe.put("cactus", 160);
        enchantedRecipe.put("raw chicken", 160);
        enchantedRecipe.put("sugar cane", 160);
        enchantedRecipe.put("pumpkin", 160);
        enchantedRecipe.put("wheat", 160);
        enchantedRecipe.put("seeds", 160);
        enchantedRecipe.put("red mushroom", 160);
        enchantedRecipe.put("brown mushroom", 160);
        enchantedRecipe.put("raw rabbit", 160);
        enchantedRecipe.put("nether wart", 160);
        enchantedRecipe.put("mutton", 160);
        enchantedRecipe.put("melon", 160);
        enchantedRecipe.put("potato", 160);
        enchantedRecipe.put("leather", 576);
        enchantedRecipe.put("porkchop", 160);
        enchantedRecipe.put("feather", 160);

        // Mining
        enchantedRecipe.put("lapis lazuli", 160);
        enchantedRecipe.put("redstone", 160);
        enchantedRecipe.put("umber", 160);
        enchantedRecipe.put("coal", 160);
        enchantedRecipe.put("mycelium", 160);
        enchantedRecipe.put("end stone", 160);
        enchantedRecipe.put("quartz", 160);
        enchantedRecipe.put("sand", 160);
        enchantedRecipe.put("iron", 160);
        enchantedRecipe.put("tungsten", 160);
        enchantedRecipe.put("obsidian", 160);
        enchantedRecipe.put("diamond", 160);
        enchantedRecipe.put("cobblestone", 160);
        enchantedRecipe.put("glowstone", 160);
        enchantedRecipe.put("gold", 160);
        enchantedRecipe.put("flint", 160);
        enchantedRecipe.put("hard stone", 576);
        enchantedRecipe.put("mithril", 160);
        enchantedRecipe.put("emerald", 160);
        enchantedRecipe.put("red sand", 160);
        enchantedRecipe.put("ice", 160);
        enchantedRecipe.put("sulphur", 160);
        enchantedRecipe.put("netherrack", 160);

        // Combat
        enchantedRecipe.put("ender pearl", 20);
        enchantedRecipe.put("slimeball", 160);
        enchantedRecipe.put("magma cream", 160);
        enchantedRecipe.put("ghast tear", 5);
        enchantedRecipe.put("gunpowder", 160);
        enchantedRecipe.put("rotten flesh", 160);
        enchantedRecipe.put("spider eye", 160);
        enchantedRecipe.put("bone", 160);
        enchantedRecipe.put("blaze rod", 160);
        enchantedRecipe.put("string", 160);

        // Fishing
        enchantedRecipe.put("lily pad", 160);
        enchantedRecipe.put("prismarine shard", 80);
        enchantedRecipe.put("ink sack", 80);
        enchantedRecipe.put("raw fish", 160);
        enchantedRecipe.put("pufferfish", 160);
        enchantedRecipe.put("clownfish", 160);
        enchantedRecipe.put("raw salmon", 160);
        enchantedRecipe.put("prismarine crystals", 80);
        enchantedRecipe.put("clay", 160);
        enchantedRecipe.put("sponge", 40);

        // Sacks
        enchantedRecipe.put("rabbit foot", 160);
        enchantedRecipe.put("rabbit hide", 576);
        enchantedRecipe.put("titanium", 160);
        enchantedRecipe.put("shark fin", 160);

        // Enchanted Block Recipes
        // Farming
        enchantedBlockRecipe.put("cocoa", 20480);
        enchantedBlockRecipe.put("carrot", 20480);
        enchantedBlockRecipe.put("cactus", 25600);
        enchantedBlockRecipe.put("sugar cane", 25600);
        enchantedBlockRecipe.put("pumpkin", 25600);
        enchantedBlockRecipe.put("wheat", 25600);
        enchantedBlockRecipe.put("seeds", 25600);
        enchantedBlockRecipe.put("red mushroom", 5120);
        enchantedBlockRecipe.put("brown mushroom", 5120);
        enchantedBlockRecipe.put("nether wart", 25600);
        enchantedBlockRecipe.put("mutton", 25600);
        enchantedBlockRecipe.put("melon", 25600);
        enchantedBlockRecipe.put("potato", 25600);
        enchantedBlockRecipe.put("porkchop", 25600);

        // Mining
        enchantedBlockRecipe.put("lapis lazuli", 25600);
        enchantedBlockRecipe.put("redstone", 25600);
        enchantedBlockRecipe.put("umber", 25600);
        enchantedBlockRecipe.put("coal", 25600);
        enchantedBlockRecipe.put("mycelium", 25600);
        enchantedBlockRecipe.put("quartz", 25600);
        enchantedBlockRecipe.put("iron", 25600);
        enchantedBlockRecipe.put("tungsten", 25600);
        enchantedBlockRecipe.put("diamond", 25600);
        enchantedBlockRecipe.put("glowstone", 25600);
        enchantedBlockRecipe.put("gold", 25600);
        enchantedBlockRecipe.put("hard stone", 331776);
        enchantedBlockRecipe.put("mithril", 25600);
        enchantedBlockRecipe.put("emerald", 25600);
        enchantedBlockRecipe.put("red sand", 25600);
        enchantedBlockRecipe.put("ice", 25600);
        enchantedBlockRecipe.put("sulphur", 25600);

        // Combat
        enchantedBlockRecipe.put("ender pearl", 3200);
        enchantedBlockRecipe.put("slimeball", 25600);
        enchantedBlockRecipe.put("magma cream", 25600);
        enchantedBlockRecipe.put("gunpowder", 10240);
        enchantedBlockRecipe.put("spider eye", 10240);
        enchantedBlockRecipe.put("bone", 25600);
        enchantedBlockRecipe.put("blaze rod", 25600);

        // Fishing
        enchantedBlockRecipe.put("fish", 25600);
        enchantedBlockRecipe.put("salmon", 25600);
        enchantedBlockRecipe.put("clay", 25600);
        enchantedBlockRecipe.put("sponge", 1024);

        // Sacks
        enchantedBlockRecipe.put("titanium", 12800);
    }
}
