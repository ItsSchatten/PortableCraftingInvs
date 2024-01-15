plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}


dependencies {
    // Automatic lombok and delombok configuration
    implementation("io.freefair.gradle:lombok-plugin:8.0.1")

    // Shade libraries into one "UberJar"
    implementation("com.github.johnrengelman:shadow:8.1.1")

    // NMS remapper.
    implementation("me.tagavari:nms-remap:1.0.0")
}
