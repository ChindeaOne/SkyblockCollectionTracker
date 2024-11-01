package io.github.chindeaytb.collectiontracker.serverconn;

import io.github.chindeaytb.collectiontracker.player.PlayerUUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConnectHandlerClass {

    private static final Logger logger = LogManager.getLogger(DisconnectHandlerClass.class);

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            logger.info("Player and world fully loaded on tick.");
            if (PlayerUUID.UUID.isEmpty()) {
                PlayerUUID.getUUID();
            }
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
