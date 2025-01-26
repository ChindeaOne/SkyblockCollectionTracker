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

        val currentX = config?.overlay?.overlayPosition?.x ?: 4
        val currentY = config?.overlay?.overlayPosition?.y ?: 150
        val scale = config?.overlay?.overlayPosition?.scale ?: 1.0f

        val position = Position(currentX, currentY)
        position.setScaling(scale)
        ModInitialization.screenToOpen = DummyOverlay(
            position, Minecraft.getMinecraft().currentScreen as? GuiContainer
        )

        Minecraft.getMinecraft().displayGuiScreen(
            DummyOverlay(
                position, Minecraft.getMinecraft().currentScreen as? GuiContainer
            )
        )
    }
}