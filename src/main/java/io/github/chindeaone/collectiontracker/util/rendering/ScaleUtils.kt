package io.github.chindeaone.collectiontracker.util.rendering

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution

object ScaleUtils {

    private val mc = Minecraft.getMinecraft()

    val height get() = ScaledResolution(mc).scaledHeight
    val width get() = ScaledResolution(mc).scaledWidth
}