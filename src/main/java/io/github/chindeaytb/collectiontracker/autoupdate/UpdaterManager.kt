package io.github.chindeaytb.collectiontracker.autoupdate

import io.github.chindeaytb.UpdateSetup
import io.github.chindeaytb.UpdateContext
import io.github.chindeaytb.UpdateTarget
import io.github.chindeaytb.collectiontracker.ModInitialization
import io.github.chindeaytb.collectiontracker.config.ModConfig
import java.util.concurrent.CompletableFuture

object UpdaterManager {

    private var activePromise: CompletableFuture<*>? = null
    private var potentialUpdate: UpdateSetup? = null

    private val context = UpdateContext(
        "sct",
        ModInitialization.version,
        "none",
        ModInitialization.MODID,
        UpdateTarget.deleteAndSaveInTheSameFolder(UpdaterManager::class.java)
    )

    init{
        context.cleanup()
    }

    fun checkUpdate(config: ModConfig?) {
        val currentStream = config?.about?.update
        if(currentStream != null){
            if(currentStream == 2){
                context.setStream("beta")
            } else if(currentStream == 1){
                context.setStream("release")
            }
        }
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