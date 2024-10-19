package io.github.chindeaytb.collectiontracker.config.misc

import io.github.chindeaytb.collectiontracker.init.ModInitialization
import io.github.moulberry.moulconfig.gui.GuiOptionEditor
import io.github.moulberry.moulconfig.internal.TextRenderUtils
import io.github.moulberry.moulconfig.processor.ProcessedOption
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting.GREEN

class VersionCheck(option: ProcessedOption) : GuiOptionEditor(option) {

    override fun render(x: Int, y: Int, width: Int) {
        val fr = Minecraft.getMinecraft().fontRendererObj

        val width = width - 20

        val widthRemaining = width - 10
        val currentVersion = "Version: " + ModInitialization.version

        // Render the current version in green
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat() + 10, y.toFloat(), 1F)
        GlStateManager.scale(2F, 2F, 1F)
        TextRenderUtils.drawStringCenteredScaledMaxWidth(
            "${GREEN}$currentVersion",
            fr,
            widthRemaining / 4F,
            10F,
            true,
            widthRemaining / 2,
            -1
        )
        GlStateManager.popMatrix()

    }

    override fun getHeight(): Int {
        return 55
    }

    override fun mouseInput(x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int): Boolean {
        return false
    }

    override fun keyboardInput(): Boolean {
        return false
    }
}
