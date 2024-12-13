package io.github.chindeaytb.collectiontracker.util

import io.github.chindeaytb.collectiontracker.api.serverapi.ServerStatus
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.Executors

object ServerUtils {

    var serverStatus = false

    private const val NORMAL_CHECK_INTERVAL = 6000  // 5 minute
    private var tickCounter = 0
    private var currentCheckInterval = NORMAL_CHECK_INTERVAL
    private const val COOLDOWN_CHECK_INTERVAL = 12000  // 10 minutes
    private var consecutiveFailures = 0

    private val executorService = Executors.newSingleThreadExecutor()
    private val logger: Logger = LogManager.getLogger(ServerUtils::class.java)

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        tickCounter++
        if (tickCounter >= currentCheckInterval) {
            tickCounter = 0
            executorService.submit { checkServerStatusPeriodically() }
        }
    }

    private fun checkServerStatusPeriodically() {
        logger.info("Checking server status...")
        serverStatus = ServerStatus.checkServer()

        if (serverStatus) {
            logger.info("Server is alive.")
            consecutiveFailures = 0
            currentCheckInterval = NORMAL_CHECK_INTERVAL
            if (TokenManager.getToken() == null) {
                TokenManager.fetchAndStoreToken()
            }
        } else {
            logger.warn("Server is not alive.")
            consecutiveFailures++
            if (consecutiveFailures >= 3) {
                currentCheckInterval = COOLDOWN_CHECK_INTERVAL
            }
            TrackingHandlerClass.stopTracking()
        }
    }
}