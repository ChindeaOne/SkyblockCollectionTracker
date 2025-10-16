package io.github.chindeaone.collectiontracker.gui

import io.github.chindeaone.collectiontracker.ModInitialization
import io.github.chindeaone.collectiontracker.gui.overlays.CollectionOverlay
import io.github.chindeaone.collectiontracker.gui.overlays.DummyOverlay
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer

object GuiManager {
    @JvmStatic
    fun openGuiPositionEditor() {

        CollectionOverlay.setVisible(false)

        ModInitialization.screenToOpen = DummyOverlay(
            Minecraft.getMinecraft().currentScreen as? GuiContainer
        )

        Minecraft.getMinecraft().displayGuiScreen(
            DummyOverlay(
                Minecraft.getMinecraft().currentScreen as? GuiContainer
            )
        )
    }
}