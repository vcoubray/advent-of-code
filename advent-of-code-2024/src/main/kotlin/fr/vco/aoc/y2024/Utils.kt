package fr.vco.aoc.y2024

import java.io.File
import kotlin.math.abs

private fun String.getInputFile() = File("advent-of-code-2024/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readText(fileName: String) = fileName.getInputFile().readText()

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun minus(other: Position) = Position(x - other.x, y - other.y)
    operator fun times(value: Int) = Position(x * value, y * value)
    fun mod(other: Position) = Position(x.mod(other.x), y.mod(other.y))
    fun distanceManhattan(other: Position) = abs(x - other.x) + abs(y - other.y)
}

val EAST = Position(1, 0)
val WEST = Position(-1, 0)
val NORTH = Position(0, -1)
val SOUTH = Position(0, 1)

val DIRECTION_MANHATTAN = listOf(
    EAST,
    SOUTH,
    WEST,
    NORTH
)

val DIRECTIONS = listOf(
    Position(-1, -1),
    Position(-1, 0),
    Position(-1, 1),
    Position(0, -1),
    Position(0, 1),
    Position(1, -1),
    Position(1, 0),
    Position(1, 1),
)


operator fun <T> List<List<T>>.get(p: Position) = this[p.y][p.x]
operator fun List<String>.get(p: Position) = this[p.y][p.x]

fun <T> List<List<T>>.getOrNull(p: Position) = this.getOrNull(p.y)?.getOrNull(p.x)
fun List<String>.getOrNull(p: Position) = this.getOrNull(p.y)?.getOrNull(p.x)

operator fun <T> List<MutableList<T>>.set(p: Position, value: T) {
    this[p.y][p.x] = value
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