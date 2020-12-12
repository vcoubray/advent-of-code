package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import kotlin.math.*

fun main() {
    val input = getInputReader("/2020/inputDay12.txt")
    val lines = input.readLines()

    val boat = Boat()

    lines.forEach(boat::move)
    println("Part 1 : ${ORIGIN.dist(boat.position)}")

}


fun Int.toRadian() = this * PI / 180
data class Position(val x: Int, val y: Int) {
    operator fun plus(pos: Position) = Position(x + pos.x, y + pos.y)
    operator fun times(factor: Int) = Position(x * factor, y * factor)
    fun dist(pos: Position) = abs(x - pos.x) + abs(y - pos.y)
    fun turn(radian: Double) = Position(
        (x * cos(radian) - y * sin(radian)).toInt(),
        (x * sin(radian) + y * cos(radian)).toInt()
    )
}

val ORIGIN = Position(0, 0)
val EAST = Position(1, 0)
val WEST = Position(-1, 0)
val NORTH = Position(0, 1)
val SOUTH = Position(0, -1)

class Boat(
    var position: Position = ORIGIN,
    var direction: Position = EAST
) {

    fun move(instruction: String) {
        val ins = instruction.first()
        val number = instruction.drop(1).toInt()
        when (ins) {
            'N' -> position = NORTH * number + position
            'S' -> position = SOUTH * number + position
            'E' -> position = EAST * number + position
            'W' -> position = WEST * number + position
            'F' -> position = direction * number + position
            'L' -> direction = direction.turn(number.toRadian())
            'R' -> direction = direction.turn(-number.toRadian())
            else -> error("Should not append")
        }
    }
}

