plugins {
    id("pci-conventions")

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // API for events.
    compileOnly(project(":api"))

    // Paper's API.
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}
