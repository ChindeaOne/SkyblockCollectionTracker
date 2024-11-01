/*
 * This kotlin object is derived from the Skyhanni mod.
 */

package sct

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.setVersionFromGit(): String {
    val baos = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags", "--abbrev=0")
        standardOutput = baos
        isIgnoreExitValue = true
    }
    return baos.toString().trim().removePrefix("v")
}
