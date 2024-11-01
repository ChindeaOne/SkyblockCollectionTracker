package io.github.chindeaytb.collectiontracker.serverconn;

import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DisconnectHandlerClass {

    private static final Logger logger = LogManager.getLogger(DisconnectHandlerClass.class);

    @SubscribeEvent
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        logger.info("Player has disconnected.");
        ICommandSender sender = new ICommandSender() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public IChatComponent getDisplayName() {
                return null;
            }

            @Override
            public void addChatMessage(IChatComponent component) {
            }

            @Override
            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
                return false;
            }

            @Override
            public BlockPos getPosition() {
                return null;
            }

            @Override
            public Vec3 getPositionVector() {
                return null;
            }

            @Override
            public World getEntityWorld() {
                return null;
            }

            @Override
            public Entity getCommandSenderEntity() {
                return null;
            }

            @Override
            public boolean sendCommandFeedback() {
                return false;
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int amount) {
            }
        };
        TrackingHandlerClass.stopTracking(sender);
    }
}
