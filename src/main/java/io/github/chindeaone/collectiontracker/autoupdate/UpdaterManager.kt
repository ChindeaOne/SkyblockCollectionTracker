package io.github.chindeaone.collectiontracker.autoupdate

import io.github.chindeaone.UpdateContext
import io.github.chindeaone.UpdateSetup
import io.github.chindeaone.UpdateTarget
import io.github.chindeaone.collectiontracker.ModInitialization
import net.minecraft.client.Minecraft
import java.util.concurrent.CompletableFuture

object UpdaterManager {

    private var activePromise: CompletableFuture<*>? = null
    private var potentialUpdate: UpdateSetup? = null
    private val version = Minecraft.getMinecraft().version.split("-")[0]

    private val context = UpdateContext(
        "sct",
        version,
        ModInitialization.version,
        "none",
        ModInitialization.MODID,
        UpdateTarget.deleteAndSaveInTheSameFolder(UpdaterManager::class.java)
    )

    init{
        context.cleanup()
    }

    private fun setUpdateStream(): String {
        val currentStream = ModInitialization.configManager.config!!.about.update
        return when (currentStream) {
            1 -> "release"
            2 -> "beta"
            else -> "none"
        }
    }

    fun update() {
        val stream = setUpdateStream()
        context.setStream(stream)
        activePromise = context.checkUpdate().thenAcceptAsync {
            potentialUpdate = it
            queueUpdate()
        }
    }

    private fun queueUpdate(){
        activePromise = CompletableFuture.supplyAsync{
            potentialUpdate!!.prepareUpdate()
        }.thenAcceptAsync {
            potentialUpdate!!.executeUpdate()
        }
    }
}