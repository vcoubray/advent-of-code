package fr.vco.aoc.y2023

import kotlin.math.absoluteValue

fun main() {
    val input = readLines("Day18")

    println("Part 1 : ${input.map { it.toDigInstruction() }.dig().area()}")
    println("Part 2 : ${input.map { it.toRealDigInstruction() }.dig().area()}")
}

data class DigInstruction(val direction: Direction, val distance: Long)

fun String.toDigInstruction(): DigInstruction {
    val (direction, distance) = """([UDRL]) (\d+) \(#(.*)\)""".toRegex().find(this)!!.destructured
    return DigInstruction(DIRECTION_MAP[direction]!!, distance.toLong())
}

fun String.toRealDigInstruction(): DigInstruction {
    val (_, _, color) = """([UDRL]) (\d+) \(#(.*)\)""".toRegex().find(this)!!.destructured
    val direction = DIRECTION_MAP.values.toList()[color.takeLast(1).toInt(16)]
    val distance = color.take(5).toLong(16)
    return DigInstruction(direction, distance)
}

val DIRECTION_MAP = mapOf(
    "R" to Direction.EAST,
    "D" to Direction.SOUTH,
    "L" to Direction.WEST,
    "U" to Direction.NORTH
)

fun List<DigInstruction>.dig(start: PositionLong = PositionLong(1, 1)): List<PositionLong> {
    var current = start
    return map { (direction, distance) ->
        current = (direction.vector.toPositionLong() * distance) + current
        current
    }
}

fun List<PositionLong>.area(): Long {
    val polygon = (this + first()).windowed(2)
    val perimeter = polygon.fold(0L) { acc , (p1, p2) -> acc + p1.distanceManhattan(p2) }
    val shoelaceArea = polygon.fold(0L) { acc, (p1, p2) -> acc + (p1.x * p2.y - p1.y * p2.x) }.absoluteValue /2L
    return shoelaceArea + perimeter/2L +1
}

