plugins {
//    kotlin("jvm") version "2.1.0"
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
        maxDay = 25
        session = aocSession
    }
}