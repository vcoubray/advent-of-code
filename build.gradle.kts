plugins {
    kotlin("jvm") version "1.9.21"
    `aoc-inputs`
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val aocSession: String by project

subprojects {
    apply<Aoc_inputs_gradle.AocPlugin>()

    configure<Aoc_inputs_gradle.AocExtension> {
        dest = "${project.projectDir}/src/main/resources/inputs"
        session = aocSession
    }
}