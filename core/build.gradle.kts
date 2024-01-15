plugins {
    id("pci-conventions")
}

dependencies {
    implementation(project(":common", configuration = "shadow"))
    implementation(project(":api", configuration = "shadow"))

    implementation(project(":v1_19_R1", configuration = "shadow"))
    implementation(project(":v1_19_R2", configuration = "shadow"))
    implementation(project(":v1_19_R3", configuration = "shadow"))
    implementation(project(":v1_20_R1", configuration = "shadow"))
    implementation(project(":v1_20_R2", configuration = "shadow"))
    implementation(project(":v1_20_R3", configuration = "shadow"))

    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(shadowJar)
        layout.buildDirectory.set(file(System.getProperty("user.home") + File.separator + project.property("build-path")))
    }
}

