package fr.vco.aoc.y2024

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2024/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readText(fileName: String) = fileName.getInputFile().readText()

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun minus(other: Position) = Position(x - other.x, y - other.y)
    operator fun times(value: Int) = Position(x * value, y * value)
}

// Lists
inline fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf())
        else list.last().add(it)
    }
    return list
}