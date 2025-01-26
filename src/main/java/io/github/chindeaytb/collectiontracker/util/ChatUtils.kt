package io.github.chindeaytb.collectiontracker.util

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

object ChatUtils {

    private val SCT = "${EnumChatFormatting.GOLD}SCT${EnumChatFormatting.RESET}"
    private val PREFIX = "${EnumChatFormatting.ITALIC}${EnumChatFormatting.DARK_AQUA}[$SCT${EnumChatFormatting.DARK_AQUA}] ${EnumChatFormatting.RESET}"

    fun sendMessage(message: String) {
        Minecraft.getMinecraft().thePlayer?.addChatMessage(ChatComponentText("$PREFIX$message"))
    }
}
