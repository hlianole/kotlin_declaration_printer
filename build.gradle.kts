plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "com.hlianole.kotlin_declaration_printer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}

application {
    mainClass.set("com.hlianole.kotlin_declaration_printer.MainKt")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    from(
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) {
                it
            } else {
                zipTree(it)
            }
        }
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}