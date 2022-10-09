import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.jonathanxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // bytebuddy
    implementation("net.bytebuddy:byte-buddy:1.12.16")
}

tasks.test {
    useJUnitPlatform()
}

// Shadowjar filter out shaded jar manifests
tasks.withType<ShadowJar>() {
    archiveVersion.set("")
    archiveClassifier.set("")
    manifest {
        // from main resources/META-INF
        // resources dir
        from("src/main/resources/META-INF/MANIFEST.MF")
    }
    from("LICENSE")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    relocate("net.bytebuddy", "com.github.jonathanxd.bytebuddy")
}
