package io.github.chindeaytb.collectiontracker.collections.prices;

import io.github.chindeaytb.collectiontracker.api.bazaarapi.FetchBazaarPrice;
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager;
import io.github.chindeaytb.collectiontracker.util.PlayerData;

public class BazaarPrice {

    public static float getPrice(String collection) {
        String price = FetchBazaarPrice.fetchPrice(PlayerData.INSTANCE.getPlayerUUID(), TokenManager.getToken(), collection);

        if(price == null) {
            return -1;
        }

        return Float.parseFloat(price);
    }
}
