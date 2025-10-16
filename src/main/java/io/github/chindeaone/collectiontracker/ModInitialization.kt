package io.github.chindeaone.collectiontracker

import io.github.chindeaone.collectiontracker.commands.CollectionList
import io.github.chindeaone.collectiontracker.commands.CommandHelper
import io.github.chindeaone.collectiontracker.commands.GuiMenu
import io.github.chindeaone.collectiontracker.commands.PauseTracker
import io.github.chindeaone.collectiontracker.commands.ResumeTracker
import io.github.chindeaone.collectiontracker.commands.SCT_Commands
import io.github.chindeaone.collectiontracker.commands.StartMultiTracker
import io.github.chindeaone.collectiontracker.commands.StartTracker
import io.github.chindeaone.collectiontracker.commands.StopTracker
import io.github.chindeaone.collectiontracker.config.ConfigManager
import io.github.chindeaone.collectiontracker.util.ModulesLoader
import io.github.chindeaone.collectiontracker.util.VersionConstants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = ModInitialization.MODID, clientSideOnly = true, useMetadata = true, version = VersionConstants.MOD_VERSION)
class ModInitialization {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {

        loadModule(this)
        ModulesLoader.modules.forEach { loadModule(it) }

        logger.info("[SCT]: Skyblock Collection Tracker pre-initialization complete.")
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        // Register the event bus
        configManager = ConfigManager()

        MinecraftForge.EVENT_BUS.register(configManager)
        logger.info("[SCT]: ConfigManager initialized.")

        // Register commands
        val commandHelper = CommandHelper()
        val startTracker = StartTracker()
        val stopTracker = StopTracker()
        val pauseTracker = PauseTracker()
        val resumeTracker = ResumeTracker()
        val collectionList = CollectionList()
        val startMultiTracker = StartMultiTracker()
        val guiMenu = GuiMenu()
        ClientCommandHandler.instance.registerCommand(
            SCT_Commands(
                commandHelper,
                startTracker,
                stopTracker,
                pauseTracker,
                resumeTracker,
                collectionList,
                startMultiTracker,
                guiMenu
            )
        )

        logger.info("[SCT]: SkyblockCollectionTracker initialized.")

        loadedClasses.clear()
    }

    private val loadedClasses = mutableSetOf<String>()

    private fun loadModule(obj: Any) {
        if (!loadedClasses.add(obj.javaClass.name)) throw IllegalStateException("Module ${obj.javaClass.name} is already loaded")
        modules.add(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (screenToOpen != null) {
            screenTicks++

            // Delay the screen opening by 5 ticks
            if (screenTicks == 5) {
                // Close any open screen
                Minecraft.getMinecraft().thePlayer?.closeScreen()

                // Open the specified screen
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen)

                // Reset the counter and screen to open
                screenTicks = 0
                screenToOpen = null
            }
        }
    }

    companion object {
        lateinit var configManager: ConfigManager
        const val MODID = "skyblockcollectiontracker"

        val logger: Logger = LogManager.getLogger(ModInitialization::class.java)

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MODID]!!.version

        var screenToOpen: GuiScreen? = null
        val modules: MutableList<Any> = ArrayList()
        private var screenTicks = 0
    }
}
