package io.github.chindeaytb.collectiontracker.init;

import io.github.chindeaytb.collectiontracker.commands.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = HypixelConnection.MODID)
public class HypixelConnection {

    public static final String MODID = "SkyblockCollections";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        SetHypixelApiKey setHypixelApiKey = new SetHypixelApiKey();
        CommandHelper commandHelper = new CommandHelper();
        SetCollection setCollection = new SetCollection();
        StopTracker stopTracker = new StopTracker();
        ClientCommandHandler.instance.registerCommand(new SCT_Commands(commandHelper, setHypixelApiKey, setCollection, stopTracker));
    }

}
