/*
 * This kotlin object is derived from the Skyhanni mod.
 */

package io.github.chindeaytb.collectiontracker.util

import net.minecraft.client.Minecraft

object HypixelUtils {

    private val HypixelServer get() = Hypixel.server

    val isInHypixel get() = HypixelServer && Minecraft.getMinecraft().thePlayer != null

    @JvmStatic
    val isOnSkyblock get() = isInHypixel && Hypixel.skyblock

}