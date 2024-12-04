package fr.vco.aoc.y2024

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2024/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun times(value: Int) = Position(x * value, y * value)
}