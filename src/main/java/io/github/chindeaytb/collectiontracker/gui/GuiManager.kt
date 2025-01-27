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

        var currentX = config?.overlay?.overlayPosition?.x ?: 4
        var currentY = config?.overlay?.overlayPosition?.y ?: 150
        val scale = config?.overlay?.overlayPosition?.scale ?: 1.0f

        if (currentX < 0) currentX = 4
        if (currentY < 0) currentY = 150

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