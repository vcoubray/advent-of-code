package fr.vco.aoc.y2025

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class Tile(val x: Long, val y: Long)

data class Segment(val a: Tile, val b: Tile) {
    val isHorizontal = a.y == b.y

    val xRange = min(a.x, b.x)..max(a.x, b.x)
    val yRange = min(a.y, b.y)..max(a.y, b.y)
}

data class Rectangle(val a: Tile, val b: Tile) {
    val area = ((a.x - b.x).absoluteValue + 1) * ((a.y - b.y).absoluteValue + 1)

    val xRange = min(a.x, b.x)..max(a.x, b.x)
    val yRange = min(a.y, b.y)..max(a.y, b.y)

    fun isCrossing(segment: Segment): Boolean {
        return if (segment.isHorizontal) {
            when {
                segment.a.y <= yRange.first -> false
                segment.a.y >= yRange.last -> false
                segment.xRange.last <= xRange.first -> false
                segment.xRange.first >= xRange.last -> false
                else -> true
            }
        } else {
            when {
                segment.a.x <= xRange.first -> false
                segment.a.x >= xRange.last -> false
                segment.yRange.last <= yRange.first -> false
                segment.yRange.first >= yRange.last -> false
                else -> true
            }
        }
    }
}

fun main() {
    val input = readLines("Day09")

    val tiles = input
        .map { it.split(",").map { n -> n.toLong() } }
        .map { (x, y) -> Tile(x, y) }

    val shape = tiles.toShape()
    val rectangles = tiles.getAllRectangles()

    println("Part 1 : ${rectangles.maxOf { it.area }}")
    println("Part 2 : ${rectangles.filter { it in shape }.maxOf { it.area }}")
}

fun List<Tile>.getAllRectangles(): List<Rectangle> {
    return (0..<lastIndex).flatMap { i ->
        (i + 1..lastIndex).map { j ->
            Rectangle(this[i], this[j])
        }
    }
}

fun List<Tile>.toShape(): List<Segment> {
    return this.windowed(2).map { (a, b) -> Segment(a, b) } + Segment(this.last(), this.first())
}

operator fun List<Segment>.contains(rectangle: Rectangle) = this.none { rectangle.isCrossing(it) }
