plugins {
    // Conventions plugin, contains most of the dependencies we'll use.
    id("pci-conventions")
}

version = "2.0.0"

dependencies {
    // Always using the latest API version here, this plugin is built to always support the latest version.
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")
}
