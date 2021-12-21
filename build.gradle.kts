import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import  org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "me.chris"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

buildscript {
    repositories { jcenter() }

    dependencies {
        val kotlinVersion = "1.5.10"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
