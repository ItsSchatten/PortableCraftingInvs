plugins {
    id("pci-conventions")
    id("special-sources")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":api"))
    compileOnly("org.spigotmc:spigot:1.19.2-R0.1-SNAPSHOT:remapped-mojang")
}


