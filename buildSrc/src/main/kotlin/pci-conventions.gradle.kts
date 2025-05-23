plugins {
    id("java")

    id("io.freefair.lombok")
    id("com.gradleup.shadow") // This is a continuation of the Shadow plugin see https://github.com/GradleUp/shadow/issues/908

}

// Repositories, contains central, local (only for the ShadowLibs jar), CodeMC (BStats), and Spigot's.
// The PaperMC Repo is supplied in :paper.
repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://maven.pkg.github.com/ItsSchatten/ShadowLibs")
        credentials {
            // These are placed in the %user%/.gradle/gradle.properties file.
            // Keep the names in mind as they must match to be used.
            username = project.findProperty("gpr.username") as String
            password = project.findProperty("gpr.token") as String
        }
    }

    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
}

// Common dependencies.
dependencies {
    // Many utilities used throughout.
    // See github.com/ItsSchatten/ShadowLibs
    implementation("com.itsschatten.libs:shadowlibs:1.0.11")
    // Statistics for the plugin, relocated in the core module.
    implementation("org.bstats:bstats-bukkit:3.0.1")

    // Lombok for ease of development.
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

group = "com.itsschatten.portablecrafting"
version = project.property("version")!!
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
