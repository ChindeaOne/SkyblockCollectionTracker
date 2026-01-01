/*
 * Copyright (C) 2022-2023 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 *
 * NotEnoughUpdates is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * NotEnoughUpdates is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NotEnoughUpdates. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.chindeaone.collectiontracker.util

import io.github.chindeaone.collectiontracker.ModInitialization
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

class CommissionsKeybinds {

    var currentlyOpenChestName = ""
    private var lastClick = -1L
    private val keybinds: List<Int> get() = listOf(
        ModInitialization.configManager.config!!.mining.commissions.commission1,
        ModInitialization.configManager.config!!.mining.commissions.commission2,
        ModInitialization.configManager.config!!.mining.commissions.commission3,
        ModInitialization.configManager.config!!.mining.commissions.commission4,
    )
    private val logger: Logger = LogManager.getLogger(CommissionsKeybinds::class.java)

    @SubscribeEvent
    fun onGuiKeyboardInput(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        checkContainerName(event)
    }

    @SubscribeEvent
    fun onGuiMouseInput(event: GuiScreenEvent.MouseInputEvent.Pre) {
        checkContainerName(event)
    }

    // Inspired by NEU's WardrobeMouseButtons
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

    // Inspired by NEU's WardrobeMouseButtons
    @Suppress("InvalidSubscribeEvent")
    fun checkContainerName(event: GuiScreenEvent) {
        if (!HypixelUtils.isOnSkyblock || !ModInitialization.configManager.config!!.mining.commissions.enableCommissionsKeybinds) return
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
