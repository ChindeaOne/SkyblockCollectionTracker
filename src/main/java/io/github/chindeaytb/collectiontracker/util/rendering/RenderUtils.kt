package io.github.chindeaytb.collectiontracker.util.rendering

import io.github.chindeaytb.collectiontracker.ModInitialization
import io.github.chindeaytb.collectiontracker.commands.StartTracker
import io.github.chindeaytb.collectiontracker.config.core.Position
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass
import io.github.chindeaytb.collectiontracker.util.CollectionColors
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager

object RenderUtils {

    var position: Position = ModInitialization.configManager.config!!.overlay.overlayPosition

    var maxWidth: Int = 0
    var textHeight: Int = 0

    private fun getDimensions(fontRenderer: FontRenderer) {
        val overlayLines = TextUtils.getStrings()

        if (overlayLines.isEmpty()) return

        for (line: String in overlayLines) {
            val lineWidth: Int = fontRenderer.getStringWidth(line)
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth
            }
        }
        textHeight = fontRenderer.FONT_HEIGHT * (overlayLines.size + 1)
        position.setDimensions(maxWidth, textHeight)
    }

    fun drawRect(fontRenderer: FontRenderer) {

        GlStateManager.pushMatrix()
        GlStateManager.scale(position.scale, position.scale, 1.0f)

        if (TrackingHandlerClass.startTime != 0L) {
            if (ModInitialization.configManager.config!!.overlay.overlayTextColor) {
                renderColors(fontRenderer)
            } else {
                renderStrings(fontRenderer)
            }
        }
        GlStateManager.popMatrix()
    }

    fun drawRectDummy(
        fontRenderer: FontRenderer
    ) {
        getDimensions(fontRenderer)

        GlStateManager.pushMatrix()

        GlStateManager.translate(position.x.toDouble(), position.y.toDouble(), 0.0)
        GlStateManager.scale(position.scale, position.scale, 1.0f)

        // Draw the overlay background in local (unscaled) coordinates
        Gui.drawRect(
            0,
            0,
            position.width,
            position.height,
            -0x7fbfbfc0
        )

        // Center the overlay text within the overlay bounds in local coordinates
        val overlayText = "§aMove the overlay"
        val textWidth = fontRenderer.getStringWidth(overlayText)
        val textX: Int = (position.width - textWidth) / 2
        val textY: Int = (position.height - fontRenderer.FONT_HEIGHT) / 2
        fontRenderer.drawString(overlayText, textX, textY, 0xFFFFFF)
        GlStateManager.popMatrix()

        GlStateManager.pushMatrix()
        drawStaticText(fontRenderer)
        GlStateManager.popMatrix()
    }

    private fun drawStaticText(fontRenderer: FontRenderer) {
        val textScale = 0.8

        val resizeText = "§aUse the mouse wheel to resize the overlay"
        val textWidth = fontRenderer.getStringWidth(resizeText)
        val textX = (ScaleUtils.width / 2f) / textScale - (textWidth / 2f)
        val textY = 10 / textScale

        GlStateManager.pushMatrix()
        GlStateManager.scale(textScale, textScale, 1.0)
        fontRenderer.drawString(resizeText, textX.toInt(), textY.toInt(), 0xFFFFFF)
        GlStateManager.popMatrix()

        val positionText = "§7Position: X=${position.x}, Y=${position.y}"
        val positionWidth = fontRenderer.getStringWidth(positionText)
        val positionX = (ScaleUtils.width / 2f) / textScale - (positionWidth / 2f)
        val positionY = (textY + fontRenderer.FONT_HEIGHT + 5) / textScale

        GlStateManager.pushMatrix()
        GlStateManager.scale(textScale, textScale, 1.0)
        fontRenderer.drawString(positionText, positionX.toInt(), positionY.toInt(), 0xFFFFFF)
        GlStateManager.popMatrix()
    }

    private fun renderStrings(
        fontRenderer: FontRenderer
    ) {
        val overlayLines = TextUtils.getStrings()
        if (overlayLines.isEmpty()) return

        val scaledOverlayX: Int = (position.x / position.scale).toInt()
        var scaledOverlayY: Int = (position.y / position.scale).toInt()

        for (line in overlayLines) {
            val splitIndex = line.lastIndexOf(": ")
            if (splitIndex != -1) {
                val prefix = line.substring(0, splitIndex + 2)
                val numberPart = line.substring(splitIndex + 2)

                fontRenderer.drawStringWithShadow(prefix, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), 0x55FF55)

                val prefixWidth = fontRenderer.getStringWidth(prefix)
                fontRenderer.drawStringWithShadow(numberPart, (scaledOverlayX + prefixWidth).toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
            } else {
                fontRenderer.drawStringWithShadow(line, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
            }

            scaledOverlayY += fontRenderer.FONT_HEIGHT
        }

        val uptimeString = TextUtils.uptimeString()
        val splitIndex = uptimeString.lastIndexOf(": ")
        if (splitIndex != -1) {
            val prefix = uptimeString.substring(0, splitIndex + 2)
            val numberPart = uptimeString.substring(splitIndex + 2)

            fontRenderer.drawStringWithShadow(prefix, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), 0x55FF55)

            val prefixWidth = fontRenderer.getStringWidth(prefix)
            fontRenderer.drawStringWithShadow(numberPart, (scaledOverlayX + prefixWidth).toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
        } else {
            fontRenderer.drawStringWithShadow(uptimeString, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
        }
    }

    private fun renderColors(
        fontRenderer: FontRenderer
    ) {

        val overlayLines = TextUtils.getStrings()
        if (overlayLines.isEmpty()) return

        val scaledOverlayX: Int = (position.x / position.scale).toInt()
        var scaledOverlayY: Int = (position.y / position.scale).toInt()

        val color = StartTracker.collection.let { CollectionColors.colors[it] }

        for (line in overlayLines) {
            val splitIndex = line.lastIndexOf(": ")
            if (splitIndex != -1) {
                val prefix = line.substring(0, splitIndex + 2)
                val numberPart = line.substring(splitIndex + 2)

                if (color != null) {
                    fontRenderer.drawStringWithShadow(prefix, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), color)
                }

                val prefixWidth = fontRenderer.getStringWidth(prefix)
                fontRenderer.drawStringWithShadow(numberPart, (scaledOverlayX + prefixWidth).toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
            } else {
                if (color != null) {
                    fontRenderer.drawStringWithShadow(line, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), color)
                }
            }

            scaledOverlayY += fontRenderer.FONT_HEIGHT
        }
        if (color != null) {
            val splitIndex = TextUtils.uptimeString().lastIndexOf(": ")
            if (splitIndex != -1) {
                val prefix = TextUtils.uptimeString().substring(0, splitIndex + 2)
                val numberPart = TextUtils.uptimeString().substring(splitIndex + 2)

                fontRenderer.drawStringWithShadow(prefix, scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), color)

                val prefixWidth = fontRenderer.getStringWidth(prefix)
                fontRenderer.drawStringWithShadow(numberPart, (scaledOverlayX + prefixWidth).toFloat(), scaledOverlayY.toFloat(), 0xFFFFFF)
            } else {
                fontRenderer.drawStringWithShadow(TextUtils.uptimeString(), scaledOverlayX.toFloat(), scaledOverlayY.toFloat(), color)
            }
        }
    }
}