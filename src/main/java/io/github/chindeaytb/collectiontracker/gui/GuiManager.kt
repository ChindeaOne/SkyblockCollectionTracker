package io.github.chindeaytb.collectiontracker.gui

import io.github.chindeaytb.collectiontracker.ModInitialization
import io.github.chindeaytb.collectiontracker.config.core.Position
import io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay
import io.github.chindeaytb.collectiontracker.gui.overlays.DummyOverlay
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer

object GuiManager {
    @JvmStatic
    fun openGuiPositionEditor() {

        CollectionOverlay.setVisible(false)

        val config = ModInitialization.configManager.config

        val currentX = config?.overlay?.overlayPosition?.x ?:4
        val currentY = config?.overlay?.overlayPosition?.y ?:150
        val currentWidth = config?.overlay?.overlayPosition?.width ?: 100
        val currentHeight = config?.overlay?.overlayPosition?.height ?: 28
        val scaleX = config?.overlay?.overlayPosition?.scaleX ?: 1.0f
        val scaleY = config?.overlay?.overlayPosition?.scaleY ?: 1.0f

        val position = Position(currentX, currentY, currentWidth, currentHeight)
        position.setScaling(scaleX, scaleY)
        ModInitialization.screenToOpen =
            DummyOverlay(
                position,
                Minecraft.getMinecraft().currentScreen as? GuiContainer
            )

        Minecraft.getMinecraft().displayGuiScreen(
            DummyOverlay(
                position,
                Minecraft.getMinecraft().currentScreen as? GuiContainer
            )
        )
    }
}