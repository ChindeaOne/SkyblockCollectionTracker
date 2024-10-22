package sct

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.setVersionFromGit(): String {
    // Fetch the latest Git tag version
    val baos = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags", "--abbrev=0")
        standardOutput = baos
        isIgnoreExitValue = true
    }
    return baos.toString().trim().removePrefix("v")
}
