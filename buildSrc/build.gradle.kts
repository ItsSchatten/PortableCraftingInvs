plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Maven local is used for the nms-remap-plugin.
    mavenLocal()
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
}


dependencies {
    // Automatic lombok and delombok configuration
    implementation("io.freefair.gradle:lombok-plugin:8.6")

    // Shade libraries into one "UberJar"
    implementation("io.github.goooler.shadow:shadow-gradle-plugin:8.1.7")
    //implementation("com.github.johnrengelman:shadow:8.1.1")

    // NMS remapper.
    implementation("com.itsschatten:nms-remap-plugin:1.0.0")
}
