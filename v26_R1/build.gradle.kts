plugins {
    id("pci-conventions")
}

dependencies {
    compileOnly(project(":api"))
    compileOnly("org.spigotmc:spigot:26.1-R0.1-SNAPSHOT")
}
