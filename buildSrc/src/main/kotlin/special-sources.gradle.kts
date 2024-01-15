plugins {
    id("java")

    id("me.tagavari.nmsremap")
    id("com.github.johnrengelman.shadow")
}

tasks {
    assemble {
        dependsOn(remap)
    }

    remap {
        dependsOn(shadowJar)
        val file = shadowJar.flatMap {
            it.archiveFile
        }

        // Set input.
        inputFile.set(file)

        // Rename file.
        archiveName.set("${project.name}-${project.version}.jar")
    }
}