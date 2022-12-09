package fr.vco.aoc.y2022

import kotlin.math.absoluteValue

fun main() {
    val input = readLines("Day09")
    val instructions = input.map { it.split(" ") }
        .map{(dir, steps)-> RopeMovement(directions[dir]!!, steps.toInt())}

    println("Part 1 : ${ Rope(2).move(instructions).tailLogs.size}")
    println("Part 2 : ${ Rope(10).move(instructions).tailLogs.size}")

}

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    fun isAdjacent(other: Position) = (x - other.x).absoluteValue <= 1 && (y - other.y).absoluteValue <= 1
    fun moveTo(target: Position) = Position (
        x + x.getDirection(target.x) ,
        y + y.getDirection(target.y)
    )

    private fun Int.getDirection(target: Int) = when {
        target < this -> -1
        target > this -> 1
        else -> 0
    }
}



val directions = mapOf(
    "U" to Position(0, 1),
    "D" to Position(0, -1),
    "R" to Position(1, 0),
    "L" to Position(-1, 0)
)

data class RopeMovement(val direction : Position, val steps: Int)

class Rope(knotsCount: Int) {
    private var knots = MutableList(knotsCount) { Position(0, 0) }
    var tailLogs = mutableSetOf(knots.last())

    fun move(movements: List<RopeMovement>) = apply {movements.forEach(::move)}

    private fun move(movement: RopeMovement) {
        repeat(movement.steps) {
            knots[0] += movement.direction
            knots.drop(1).forEachIndexed { i, knot ->
                if (!knot.isAdjacent(knots[i])) {
                    knots[i+1] = knot.moveTo(knots[i])
                }
            }
            tailLogs.add(knots.last())
        }
    }
}
