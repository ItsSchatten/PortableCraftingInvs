plugins {
    id("java")

    id("io.freefair.lombok")
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("com.itsschatten.libs:ShadowLibs:1.0.9")
    implementation("com.shanebeestudios.api:VirtualFurnace-API:1.1.5-Schatten")
    implementation("org.bstats:bstats-bukkit:3.0.1")

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

group = "com.itsschatten.portablecrafting"
version = "1.7.12"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
