plugins {
    id("java")

    // The remapping plugin.
    id("com.itsschatten.nmsremapper")

    // Used to obtain the shadowJar task mainly.
    // There is no configuration done in this plugin, it should be handled by pci-conventions
    id("io.github.goooler.shadow")
    //id("com.github.johnrengelman.shadow")
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

tasks {
    // Make sure shadowJar runs before our remap and then take that artifact and remap it.
    remap {
        dependsOn(shadowJar)
        val file = shadowJar.flatMap { it.archiveFile }.get().asFile

        // We only care about the input file here.
        inputFile.set(file)
    }
}