package fr.vco.aoc.y2021

import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    val input = readLines("Day05")

    val floor = mutableMapOf<Int, MutableMap<Int, Int>>()
    val (diagonalSegments, nonDiagonalSegments) = input.map { it.toSegment() }.partition { it.isDiagonal() }

    nonDiagonalSegments.forEach(floor::addSegment)
    println("Part 1 : ${floor.countValueGreaterThan(1)}")

    diagonalSegments.forEach(floor::addSegment)
    println("Part 2 : ${floor.countValueGreaterThan(1)}")
}

private fun List<Int>.toPoint() = Point(this.component1(), this.component2())
private fun List<Point>.toSegment() = Segment(this.component1(), this.component2())
private fun String.toPoint() = this.split(",").map { it.toInt() }.toPoint()
private fun String.toSegment() = this.split(" -> ").map { it.toPoint() }.toSegment()

private fun MutableMap<Int, MutableMap<Int, Int>>.addSegment(segment: Segment) =
    segment.getPoints().forEach { point ->
        val line = this.getOrPut(point.y, ::mutableMapOf)
        line[point.x] = line.getOrPut(point.x) { 0 } + 1
    }

private fun MutableMap<Int, MutableMap<Int, Int>>.countValueGreaterThan(value: Int) =
    this.values.sumOf { it.count { (_, v) -> v > value } }

private data class Point(val x: Int, val y: Int)

private data class Segment(val start: Point, val end: Point) {
    fun isDiagonal() = start.x != end.x && start.y != end.y

    fun getPoints(): List<Point> {
        val vX = start.x - end.x
        val vY = start.y - end.y
        val segmentSize = max(vX.absoluteValue, vY.absoluteValue) + 1
        val vector = Point(vX.reducedVector(), vY.reducedVector())

        return List(segmentSize) {
            Point(
                start.x + vector.x * it,
                start.y + vector.y * it
            )
        }
    }

    fun Int.reducedVector() = when {
        this == 0 -> 0
        this > 0 -> -1
        else -> 1
    }
}
