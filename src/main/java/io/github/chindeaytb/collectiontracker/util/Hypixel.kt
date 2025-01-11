/*
 * This kotlin object is derived from the Skyhanni mod.
 */

package io.github.chindeaytb.collectiontracker.util

import io.github.chindeaytb.collectiontracker.ModInitialization
import io.github.chindeaytb.collectiontracker.api.serverapi.ServerStatus
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager
import io.github.chindeaytb.collectiontracker.util.ServerUtils.serverStatus
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

object Hypixel {

    private val scoreboardTitlePattern = Regex("SK[YI]BLOCK(?: CO-OP| GUEST)?")

    private val formattingChars = "kmolnrKMOLNR".toSet()
    private val colorChars = "abcdefABCDEF0123456789".toSet()

    var server = false
    var skyblock = false
    var playerLoaded = false

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        DisconnectHandlerClass.onServerDisconnect()
        server = false
        skyblock = false
        playerLoaded = false
        serverStatus = false
    }

    // Method taken from Skyhanni mod
    private fun checkServer() {
        val mc = Minecraft.getMinecraft()
        val player = mc.thePlayer ?: return

        var hypixel = false

        player.clientBrand?.let {
            if (it.contains("hypixel", ignoreCase = true)) {
                hypixel = true
            }
        }

        server = hypixel
    }

    // Method taken from Skyhanni mod
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!HypixelUtils.isInHypixel) {
            checkServer()
            if (HypixelUtils.isInHypixel && !playerLoaded) {
                loadPlayerData()
                if (playerLoaded) {
                    serverStatus = ServerStatus.checkServer()
                    if (!serverStatus) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(
                            ChatComponentText("§cThe API server is down at the moment. Sorry for the inconvenience.")
                        )
                    } else {
                        if (TokenManager.getToken() == null) {
                            TokenManager.fetchAndStoreToken()
                        }
                    }
                    RepoUtils.checkForUpdates()
                    if (hasNewestVersion(ModInitialization.version, RepoUtils.latestVersion)) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(
                            ChatComponentText("§3New SkyblockCollectionTracker version found: ${RepoUtils.latestVersion}\n").appendSibling(
                                ChatComponentText("§a${RepoUtils.MODRINTH_URL}").apply {
                                        chatStyle = ChatStyle().apply {
                                            chatClickEvent = ClickEvent(
                                                ClickEvent.Action.OPEN_URL, RepoUtils.MODRINTH_URL
                                            )
                                        }
                                    }))
                    }
                }
            }
        }

        val inSkyblock = checkScoreboard()

        if (inSkyblock == skyblock) return

        skyblock = inSkyblock
    }

    fun loadPlayerData() {
        val mc = Minecraft.getMinecraft()
        val player = mc.thePlayer
        if (player != null) {
            playerLoaded = true
            PlayerData.playerUUID
            PlayerData.playerName
        }
    }

    // Method taken from Skyhanni mod
    private fun checkScoreboard(): Boolean {
        val minecraft = Minecraft.getMinecraft()
        val world = minecraft.theWorld ?: return false

        val objective = world.scoreboard.getObjectiveInDisplaySlot(1) ?: return false
        val displayName = objective.displayName
        val scoreboardTitle = displayName.removeColor()
        return scoreboardTitlePattern.matches(scoreboardTitle)
    }

    fun hasNewestVersion(currentVersion: String, latestVersion: String): Boolean {
        val currentParts = currentVersion.removePrefix("v").split("-", limit = 2)
        val latestParts = latestVersion.removePrefix("v").split("-", limit = 2)

        val currentNumericParts = currentParts[0].split(".")
        val latestNumericParts = latestParts[0].split(".")

        val maxLength = maxOf(currentNumericParts.size, latestNumericParts.size)
        for (i in 0 until maxLength) {
            val currentPart = currentNumericParts.getOrNull(i)?.toIntOrNull() ?: 0
            val latestPart = latestNumericParts.getOrNull(i)?.toIntOrNull() ?: 0
            if (currentPart < latestPart) return true
            if (currentPart > latestPart) return false
        }

        // If numeric parts are equal, compare pre-release tags
        val currentPreRelease = currentParts.getOrNull(1)
        val latestPreRelease = latestParts.getOrNull(1)

        if (currentPreRelease == null && latestPreRelease != null) return true
        if (currentPreRelease != null && latestPreRelease == null) return false
        if (currentPreRelease != null && latestPreRelease != null) {
            return currentPreRelease < latestPreRelease
        }

        return false
    }
    // Method taken from Skyhanni mod
    private fun CharSequence.removeColor(keepFormatting: Boolean = false): String {
        // Glossary:
        // Formatting indicator: The '§' character indicating the beginning of a formatting sequence
        // Formatting code: The character following a formatting indicator which specifies what color or text style this sequence corresponds to
        // Formatting sequence: The combination of a formatting indicator and code that changes the color or format of a string

        // Flag for whether there is a text style (non-color and non-reset formatting code) currently being applied
        var isFormatted = false

        // Find the first formatting indicator
        var nextFormattingSequence = indexOf('§')

        // If this string does not contain any formatting indicators, just return this string directly
        if (nextFormattingSequence < 0) return this.toString()

        // Let's create a new string, and pre-allocate enough space to store this entire string
        val cleanedString = StringBuilder(this.length)

        // Read index stores the position in `this` which we have written up until now
        // a/k/a where we need to start reading from
        var readIndex = 0

        // As long as there still is a formatting indicator left in our string
        while (nextFormattingSequence >= 0) {

            // Write everything from the read index up to the next formatting indicator into our clean string
            cleanedString.append(this, readIndex, nextFormattingSequence)

            // Get the formatting code (note: this may not be a valid formatting code)
            val formattingCode = this.getOrNull(nextFormattingSequence + 1)

            // If the next formatting sequence's code indicates a non-color format and we should keep those
            if (keepFormatting && formattingCode in formattingChars) {
                // Update formatted flag based on whether this is a reset or a style format code
                isFormatted = formattingCode?.lowercaseChar() != 'r'

                // Set the readIndex to the formatting indicator, so that the next loop will start writing from that paragraph symbol
                readIndex = nextFormattingSequence
                // Find the next § symbol after the formatting sequence
                nextFormattingSequence = indexOf('§', startIndex = readIndex + 1)
            } else {
                // If this formatting sequence should be skipped (either a color code, or !keepFormatting or an incomplete formatting sequence without a code)

                // If being formatted and color code encountered, reset the current formatting code
                if (isFormatted && formattingCode in colorChars) {
                    cleanedString.append("§r")
                    isFormatted = false
                }

                // Set the readIndex to after this formatting sequence, so that the next loop will skip over it before writing the string
                readIndex = nextFormattingSequence + 2
                // Find the next § symbol after the formatting sequence
                nextFormattingSequence = indexOf('§', startIndex = readIndex)

                // If the next read would be out of bound, reset the readIndex to the very end of the string, resulting in a "" string to be appended
                readIndex = readIndex.coerceAtMost(this.length)
            }
        }
        // Finally, after the last formatting sequence was processed, copy over the last sequence of the string
        cleanedString.append(this, readIndex, this.length)

        // And turn the string builder into a string
        return cleanedString.toString()
    }
}