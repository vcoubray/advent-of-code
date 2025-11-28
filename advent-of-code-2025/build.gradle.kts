plugins {
    kotlin("jvm") version "2.2.21"
}

configure<Aoc_inputs_gradle.AocExtension> {
    year = 2025
    maxDay = 12
}