package fr.vco.aoc.y2025


import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day01")
    val numbers = input
        .map { it.toRotation() }
        .fold(listOf(50)) { numbers, rotation -> numbers + (rotation + numbers.last()) }

    println("Part 1: ${numbers.count { it % 100 == 0 }}")
    println("Part 2: ${numbers.windowed(2).sumOf {(a, b) -> countMultipleOf100(a, b)} - numbers.count { it % 100 == 0 }}")
}

fun String.toRotation() = this.replace("L", "-")
    .replace("R", "")
    .toInt()

fun countMultipleOf100(a: Int, b: Int) = (min(a, b)..max(a, b)).count{it % 100 == 0}
