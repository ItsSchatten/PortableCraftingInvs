plugins {
    id("pci-conventions")
    id("special-sources")
}

dependencies {
    compileOnly(project(":api"))
    compileOnly("org.spigotmc:spigot:1.21-R0.1-SNAPSHOT:remapped-mojang")
}
