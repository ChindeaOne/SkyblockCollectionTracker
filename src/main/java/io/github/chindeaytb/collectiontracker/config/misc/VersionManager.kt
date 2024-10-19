package io.github.chindeaytb.collectiontracker.config.misc

import io.github.moulberry.moulconfig.processor.MoulConfigProcessor

object VersionManager {

    fun injectConfigProcessor(processor: MoulConfigProcessor<*>) {
        processor.registerConfigEditor(VersionDisplay::class.java) { option, _ ->
            VersionCheck(option)
        }
    }

}