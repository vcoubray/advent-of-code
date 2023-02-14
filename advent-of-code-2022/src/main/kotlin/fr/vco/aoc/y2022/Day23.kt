package fr.vco.aoc.y2022

import kotlin.math.abs

fun main() {
    val input = readLines("Day23")

    println("Part 1 : ${ElvesMap(input).execute(10).getEmptyCell()}")
    println("Part 2 : ${ElvesMap(input).executeUntilEnd()}")
}

class ElvesMap(input: List<String>) {

    private val directions = listOf(0 to -1, 0 to 1, -1 to 0, 1 to 0)
    private val directionZone = directions.map { (x, y) -> (-1..1).map { x + y * it to y + x * it } }
    private val neighbours = directionZone.flatten().toSet()

    private var startDirection = 0

    private var positions = buildMap {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, tile ->
                if (tile == '#') this[x to y] = true
            }
        }
    }

    fun getEmptyCell(): Int {
        val width = abs(positions.keys.maxOf { it.first } - positions.keys.minOf { it.first }) + 1
        val height = abs(positions.keys.maxOf { it.second } - positions.keys.minOf { it.second }) + 1
        return width * height - positions.size
    }

    fun execute(steps: Int) = apply { repeat(steps) { round() } }

    fun executeUntilEnd(): Int {
        var round = 0
        do {
            val prev = positions.keys
            round()
            round++
        } while (prev != positions.keys)
        return round
    }

    private fun round() {
        val targets = positions.keys.associateWith { (x, y) -> getTarget(x, y) }
        val targetsCount = targets.values.groupingBy { it }.eachCount()
        positions = targets.map { (origin, target) -> if ((targetsCount[target] ?: 0) == 1) target else origin }
            .associateWith { true }
        startDirection = (startDirection + 1).mod(directions.size)
    }

    private fun isIsolated(x: Int, y: Int) = neighbours.map { (nx, ny) -> (nx + x) to (ny + y) }.none(positions::contains)
    private fun getTarget(x: Int, y: Int) =
        if (isIsolated(x, y)) x to y else getTargetDir(x, y).let { (xd, yd) -> (x + xd) to (y + yd) }

    private fun getTargetDir(x: Int, y: Int) = getOrderedDirections().firstOrNull { canMove(x, y, it) }?.let(directions::get)
        ?: (0 to 0)

    fun getOrderedDirections() = (startDirection..startDirection + directions.size).map { it % directions.size }


    private fun canMove(x: Int, y: Int, direction: Int): Boolean {
        return directionZone[direction].map { (dx, dy) -> dx + x to dy + y }
            .none(positions::contains)
    }
}