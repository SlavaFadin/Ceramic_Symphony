plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

group = "org.example.app"
version = "1.0.0"

repositories{
    mavenCentral()
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    //implementation(project(":utils"))

    // Добавьте BOM в начале
    implementation(platform("io.ktor:ktor-bom:2.3.7"))

    // Теперь указывайте зависимости без версий
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-cors")

    // Остальные зависимости
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.4.1.jre11")

    // Используйте актуальную версию coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

}

tasks.withType<JavaExec>().configureEach {
    isIgnoreExitValue = true
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "org.example.app.AppKt"
}
