package io.github.chindeaytb.collectiontracker

import io.github.chindeaytb.collectiontracker.commands.*
import io.github.chindeaytb.collectiontracker.config.ConfigManager
import io.github.chindeaytb.collectiontracker.connection.DisconnectHandlerClass
import io.github.chindeaytb.collectiontracker.connection.ServerConnectHandlerClass
import io.github.chindeaytb.collectiontracker.commands.GuiMenu
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = ModInitialization.MODID, clientSideOnly = true,useMetadata = true, version = "sctVersion",)
class ModInitialization {

    @Mod.EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLInitializationEvent) {
        // Register the event bus
        MinecraftForge.EVENT_BUS.register(DisconnectHandlerClass())
        logger.info("DisconnectHandler initialized.")
        MinecraftForge.EVENT_BUS.register(ServerConnectHandlerClass())
        logger.info("ServerConnectHandler initialized.")

        configManager = ConfigManager()
        MinecraftForge.EVENT_BUS.register(configManager)
        logger.info("ConfigManager initialized.")
        MinecraftForge.EVENT_BUS.register(this)

        // Register commands
        val commandHelper = CommandHelper()
        val setCollection = SetCollection()
        val stopTracker = StopTracker()
        val guiMenu = GuiMenu()
        val moveGui = MoveGui()
        ClientCommandHandler.instance.registerCommand(SCT_Commands(commandHelper, setCollection, stopTracker, guiMenu, moveGui))

        logger.info("Skyblock Collections Tracker mod initialized.")
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        try {
            TokenManager.fetchAndStoreToken()
        } catch (e: Exception) {
            logger.error("Failed to fetch token: {}", e.message)
        }

        logger.info("Skyblock Collections Tracker post-initialization complete.")
    }

    companion object {
        lateinit var configManager: ConfigManager
        const val MODID = "skyblockcollectiontracker"

        val logger: Logger = LogManager.getLogger(ModInitialization::class.java)

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MODID]!!.version
    }
}
