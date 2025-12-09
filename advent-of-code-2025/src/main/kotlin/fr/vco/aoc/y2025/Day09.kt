package fr.vco.aoc.y2025

import kotlin.math.absoluteValue


class Tile(val x: Long, val y: Long) {
    fun rectangleArea(other: Tile) = (x - other.x + 1).absoluteValue * (y - other.y + 1).absoluteValue
}

fun main() {
    val input = readLines("Day09")

//    val input = listOf(
//        "7,1",
//        "11,1",
//        "11,7",
//        "9,7",
//        "9,5",
//        "2,5",
//        "2,3",
//        "7,3",
//    )
    val tiles = input
        .map { it.split(",").map { n -> n.toLong() } }
        .map { (x, y) -> Tile(x, y) }

    println("Part 1 : ${tiles.maxArea()}")
}

fun List<Tile>.maxArea(): Long {
    var maxArea = 0L
    for (i in (0..<lastIndex)) {
        for (j in (i + 1..lastIndex)) {
            val area = this[i].rectangleArea(this[j])
            if (area > maxArea) {
                maxArea = area
            }
        }
    }
    return maxArea
}