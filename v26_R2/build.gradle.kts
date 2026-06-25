plugins {
    id("pci-conventions")
}

dependencies {
    compileOnly(project(":api"))
    compileOnly("org.spigotmc:spigot:26.2-R0.1-SNAPSHOT")
}
