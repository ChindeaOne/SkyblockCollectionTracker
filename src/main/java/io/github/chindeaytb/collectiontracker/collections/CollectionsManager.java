package io.github.chindeaytb.collectiontracker.collections;

import java.util.*;

public class CollectionsManager {

    public static Map<String, Set<String>> collections = new HashMap<>();
    public static String collectionSource;
    public static String collectionType = null;

    public static boolean isValidCollection(String collectionName) {
        for (Set<String> collectionSet : collections.values()) {
            if (collectionSet.contains(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollection(String collectionName) {
        for (Map.Entry<String, Set<String>> entry : collections.entrySet()) {
            if (!entry.getKey().equals("Sacks") && entry.getValue().contains(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getAllCollections() {
        Set<String> allCollections = new HashSet<>();
        for (Set<String> collectionSet : collections.values()) {
            allCollections.addAll(collectionSet);
        }
        return allCollections.toArray(new String[0]);
    }

    public static boolean isRiftCollection(String collectionName) {
        return collections
                .getOrDefault("Rift", Collections.emptySet())
                .contains(collectionName);
    }
}