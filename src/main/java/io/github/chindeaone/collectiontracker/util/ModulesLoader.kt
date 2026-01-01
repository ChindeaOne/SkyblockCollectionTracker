/*
 * This kotlin object is derived from the NotEnoughUpdates mod.
 */

package io.github.chindeaone.collectiontracker.util

import io.github.chindeaone.collectiontracker.autoupdate.UpdaterManager
import io.github.chindeaone.collectiontracker.gui.overlays.CollectionOverlay

object ModulesLoader {
    val modules: List<Any> = buildList {
        add(CollectionOverlay())
        add(Hypixel)
        add(ServerUtils)
        add(UpdaterManager)
        add(CommissionsKeybinds())
    }
}