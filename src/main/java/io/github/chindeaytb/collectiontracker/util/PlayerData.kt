package io.github.chindeaytb.collectiontracker.util

import net.minecraft.client.Minecraft

object PlayerData {

    val playerUUID: String get() = Minecraft.getMinecraft().thePlayer.uniqueID.toString().replace("-", "")

    val playerName: String get() = Minecraft.getMinecraft().thePlayer.name

}