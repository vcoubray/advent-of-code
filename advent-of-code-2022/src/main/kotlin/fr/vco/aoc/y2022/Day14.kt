package fr.vco.aoc.y2022

import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day14")

    println("Part 1 : ${Cave(input).countSandPart1()}")
    println("Part 2 : ${Cave(input).countSandPart2()}")
}

data class RockPos(val x: Int, val y: Int) {
    operator fun plus(pos: RockPos) = RockPos(pos.x + x, pos.y + y)
}

val rockDirections = listOf(
    RockPos(1, 1),
    RockPos(-1, 1),
    RockPos(0, 1)
)

fun getLine(a: RockPos, b: RockPos): List<RockPos> {
    return when {
        a.x == b.x -> (min(a.y, b.y)..max(a.y, b.y)).map { RockPos(a.x, it) }
        a.y == b.y -> (min(a.x, b.x)..max(a.x, b.x)).map { RockPos(it, a.y) }
        else -> listOf(a)
    }
}

class Cave(rockPaths: List<String>) {
    val grid = mutableMapOf<Int, MutableMap<Int, Boolean>>()
    var maxDepth = 0
    var floor = 0

    init {
        rockPaths.map {
            it.split(" -> ")
                .map { coords ->
                    coords.split(",").let { (x, y) -> RockPos(x.toInt(), y.toInt()) }
                }
                .windowed(2)
                .flatMap { (a, b) -> getLine(a, b) }
                .forEach(::put)
        }
        maxDepth = grid.maxOf { it.key }
        floor = maxDepth + 2
    }

    operator fun get(pos: RockPos) = grid[pos.y]?.get(pos.x) ?: false

    private fun put(pos: RockPos) {
        if (pos.y !in grid) {
            grid[pos.y] = mutableMapOf(pos.x to true)
        } else {
            grid[pos.y]!![pos.x] = true
        }
    }

    fun countSandPart1(): Long {
        return countSand(
            endPredicate = {current -> current.y > maxDepth} ,
            childPredicate = {child -> this[child]}
        )
    }

    fun countSandPart2(): Long {
        return countSand(
            endPredicate = {_ -> false} ,
            childPredicate = {child -> this[child] || child.y == floor}
        )
    }

    private fun countSand(endPredicate: (RockPos) -> Boolean, childPredicate: (RockPos)-> Boolean): Long {
        val start = RockPos(500, 0)
        var count = 0L
        val toVisit = ArrayDeque<RockPos>()
        toVisit.add(start)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (endPredicate(current)) return count
            val next = rockDirections.map { dir -> current + dir }
                .filterNot(childPredicate)

            if (next.isNotEmpty()) {
                toVisit.addFirst(current)
                next.forEach { toVisit.addFirst(it) }

            } else {
                put(current)
                count++
            }
        }

        return count
    }
}