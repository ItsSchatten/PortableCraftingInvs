plugins {
    id("pci-conventions")

    id("io.papermc.paperweight.userdev") version "1.7.1"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // API for events.
    compileOnly(project(":api"))

    // Paper's API.
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}
