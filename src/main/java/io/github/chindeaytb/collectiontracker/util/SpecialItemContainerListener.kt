package io.github.chindeaytb.collectiontracker.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class SpecialItemContainerListener {

    var currentlyOpenChestName = ""
    private var lastClick = -1L
    val keybinds = listOf(Keyboard.KEY_1, Keyboard.KEY_2, Keyboard.KEY_3, Keyboard.KEY_4)
    private val logger: Logger = LogManager.getLogger(SpecialItemContainerListener::class.java)

    @SubscribeEvent
    fun onGuiKeyboardInput(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        checkContainerName(event)
    }

    @SubscribeEvent
    fun onGuiMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
        checkContainerName(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onGuiOpen(event: GuiOpenEvent) {
        if (!HypixelUtils.isOnSkyblock) return

        if (event.gui is GuiChest) {
            val chest = event.gui as GuiChest
            val container = chest.inventorySlots as ContainerChest

            currentlyOpenChestName = container.lowerChestInventory.displayName.unformattedText
        } else {
            currentlyOpenChestName = ""
        }
    }

    @Suppress("InvalidSubscribeEvent")
    fun checkContainerName(event: GuiScreenEvent) {
        if (!HypixelUtils.isOnSkyblock) return
        val gui = event.gui as? GuiChest ?: return
        if (!currentlyOpenChestName.contains("Commissions")) return

        val guiChest = event.gui as? GuiChest ?: return
        val container = guiChest.inventorySlots as? ContainerChest ?: return

        var slot = -1
        for (i in keybinds.indices) {
            if (isKeyDown(keybinds[i])) {
                if (System.currentTimeMillis() - lastClick > 300) {
                    slot = if (i < 2) i + 11 else i + 12
                }
            }
        }
        if (slot == -1) return
        val itemStack = container.getSlot(slot).stack ?: return
        if (itemStack.displayName.isEmpty()) return
        sendLeftMouseClick(gui.inventorySlots.windowId, slot)
        logger.info("Clicked on item: ${itemStack.displayName} in slot: $slot")
        lastClick = System.currentTimeMillis()
        event.isCanceled = true
    }

    fun isKeyDown(keyCode: Int): Boolean {
        return if (!isKeyValid(keyCode)) {
            false
        } else if (keyCode < 0) {
            Mouse.isButtonDown(keyCode + 100)
        } else {
            Keyboard.isKeyDown(keyCode)
        }
    }

        fun isKeyValid(keyCode: Int): Boolean {
            return keyCode != 0
        }

        fun sendLeftMouseClick(windowId: Int, slot: Int) {
            Minecraft.getMinecraft().playerController.windowClick(
                windowId,
                slot, 0, 0, Minecraft.getMinecraft().thePlayer
            )
        }
}
