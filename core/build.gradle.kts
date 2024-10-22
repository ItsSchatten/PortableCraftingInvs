plugins {
    id("pci-conventions")
}

// Used to configure which "path" we are compiling.
// This probably isn't necessary, but hey.
ext {
    println("Checking environment...")
    set("env", System.getProperty("env") ?: "release")
    set("isDev", get("env") == "dev")
    println("Found environment: " + get("env"))
    println("Dev Mode?: " + get("isDev"))
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    // Common.
    implementation(project(":api"))

    // Versions, we must provide "remap" as the configuration otherwise we won't use the proper file.
    implementation(project(":v1_20_R1", configuration = "remap"))
    implementation(project(":v1_20_R2", configuration = "remap"))
    implementation(project(":v1_20_R3", configuration = "remap"))
    implementation(project(":v1_20_R4", configuration = "remap"))
    implementation(project(":v1_21_R1", configuration = "remap"))
    implementation(project(":v1_21_R2", configuration = "remap"))

    // This doesn't require a direct remapping, it should be remapped already.
    implementation(project(":paper", configuration = "reobf"))

    // Spigot.
    compileOnly("org.spigotmc:spigot-api:1.21.2-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:24.1.0")

    // Hikari, used for SQL.
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")

    implementation("com.mysql:mysql-connector-j:8.4.0") {
        exclude("com.google.protobuf", "protobuf-java")
    }
}

// We don't do any sort of remapping here,
// because everything SHOULD be remapped before it even reaches here.
// The modules handle their own remapping if required.
tasks {
    // configuring shadow to output where we want.
    shadowJar {
        archiveBaseName.set(rootProject.name)

        dependencies {
            include(dependency("com.itsschatten.portablecrafting:.*"))
            include(dependency("com.zaxxer:HikariCP"))
            include(dependency("org.mariadb.jdbc:mariadb-java-client"))
            include(dependency("com.mysql:mysql-connector-j"))
            include(dependency("org.bstats:bstats-bukkit"))
        }

        if (ext.get("isDev") as Boolean) {
            // We provide a classifier here because it's an in-development version.
            // Not strictly required, but helps me in terms of file organization.
            archiveClassifier.set("dev")

            if (project.property("dev-path").toString().isNotBlank()) {
                destinationDirectory.set(file(System.getProperty("user.home") + File.separator + project.property("dev-path")))
            }
        } else {
            // We don't want any classifiers on the complete jar,
            // the base file name should already be different from the module name.
            archiveClassifier.set("")

            if (project.property("build-path").toString().isNotBlank()) {
                destinationDirectory.set(file(System.getProperty("user.home") + File.separator + project.property("build-path")))
            }
        }

        relocate("org.bstats", "com.itsschatten.libs.bstats")
        relocate("com.zaxxer.hikari", "com.itsschatten.libs.hikari")
        relocate("com.mysql", "com.itsschatten.libs.drivers.mysql")
        relocate("org.mariadb.jdbc", "com.itsschatten.libs.drivers.mariadb")
    }

    assemble {
        dependsOn(shadowJar)
    }

    // Processes resources and replaces the version to the current version.
    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to "${project.version}"
            )
        }
    }

}

