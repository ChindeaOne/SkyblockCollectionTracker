package io.github.chindeaytb.collectiontracker.util

import io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay

object ModulesLoader {
    val modules: List<Any> = buildList {
        add(DisconnectHandlerClass())
        add(CollectionOverlay())
        add(Hypixel)
        add(ServerUtils)
    }
}