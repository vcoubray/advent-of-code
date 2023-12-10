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
}
enum class Direction(val vector: Position){
    NORTH (Position(0,-1)),
    EAST (Position(1,0)),
    SOUTH (Position(0,1)),
    WEST (Position(-1,0)),
}


fun ppcm(a: Long, b: Long) = (a * b).absoluteValue / pgcd(a, b)
fun List<Long>.ppcm() = reduce { a, acc -> ppcm(a, acc) }

