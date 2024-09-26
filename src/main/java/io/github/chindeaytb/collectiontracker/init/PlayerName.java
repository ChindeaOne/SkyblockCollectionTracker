package io.github.chindeaytb.collectiontracker.init;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerName {
    public static String player_name;
    public static void getPlayerName() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player_name = player.getName();
        }
        else player_name = "";
    }
}
