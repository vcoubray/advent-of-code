package fr.vco.aoc.y2023

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private fun String.getInputFile() = File("advent-of-code-2023/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()

inline fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf())
        else list.last().add(it)
    }
    return list
}


fun pgcd(a: Long, b: Long): Long {
    if (a == 0L && b == 0L) return 0
    var max = max(a, b)
    var min = min(a, b)

    while (min != 0L) {
        val tmp = min
        min = max % min
        max = tmp
    }
    return max
}


data class Position(val x: Int, val y: Int) {
    operator fun plus(p: Position) = Position(x + p.x, y + p.y)
    fun distanceManhattan(p: Position) = (p.x - x).absoluteValue + (p.y - y).absoluteValue
}

enum class Direction(val vector: Position) {
    NORTH(Position(0, -1)),
    EAST(Position(1, 0)),
    SOUTH(Position(0, 1)),
    WEST(Position(-1, 0)),
}


fun ppcm(a: Long, b: Long) = (a * b).absoluteValue / pgcd(a, b)
fun List<Long>.ppcm() = reduce { a, acc -> ppcm(a, acc) }


fun <T> List<List<T>>.transposed(): List<List<T>> {
    val transposedList = List<MutableList<T>>(this.first().size) { mutableListOf() }
    this.forEach { line ->
        line.forEachIndexed { i, it -> transposedList[i].add(it) }
    }
    return transposedList
}

fun transpose(strings: List<String>): List<String> = strings.map { it.toList() }.transposed().map { it.joinToString() }
