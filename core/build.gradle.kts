plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    id("kotlin")
}



java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

}

tasks.withType(JavaCompile::class.java) {
    targetCompatibility = "1.8"
    sourceCompatibility = "1.8"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)
}