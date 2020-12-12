package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import kotlin.math.*

fun main() {
    val input = getInputReader("/2020/inputDay12.txt")
    val lines = input.readLines()

    val boat = Boat()
    lines.forEach(boat::move)
    println("Part 1 : ${ORIGIN.dist(boat.position)}")


    val boatPart2 = Boat(waypoint = Position(10,1))
    lines.forEach(boatPart2::moveWaypoint)
    println("Part 2 : ${ORIGIN.dist(boatPart2.position)}")
}


fun Int.toRadian() = this * PI / 180
data class Position(val x: Int, val y: Int) {
    operator fun plus(pos: Position) = Position(x + pos.x, y + pos.y)
    operator fun times(factor: Int) = Position(x * factor, y * factor)
    fun dist(pos: Position) = abs(x - pos.x) + abs(y - pos.y)
    fun turn(radian: Double) = Position(
        (x * cos(radian) - y * sin(radian)).roundToInt(),
        (x * sin(radian) + y * cos(radian)).roundToInt()
    )
}

val ORIGIN = Position(0, 0)
val EAST = Position(1, 0)
val WEST = Position(-1, 0)
val NORTH = Position(0, 1)
val SOUTH = Position(0, -1)

class Boat(
    var position: Position = ORIGIN,
    var waypoint: Position = EAST,
) {

    fun move(instruction: String) {
        val ins = instruction.first()
        val number = instruction.drop(1).toInt()
        when (ins) {
            'N' -> position = NORTH * number + position
            'S' -> position = SOUTH * number + position
            'E' -> position = EAST * number + position
            'W' -> position = WEST * number + position
            'F' -> position = waypoint * number + position
            'L' -> waypoint = waypoint.turn(number.toRadian())
            'R' -> waypoint = waypoint.turn(-number.toRadian())
            else -> error("Should not append")
        }
    }

    fun moveWaypoint(instruction: String) {
        val ins = instruction.first()
        val number = instruction.drop(1).toInt()
        when (ins) {
            'N' -> waypoint = NORTH * number + waypoint
            'S' -> waypoint = SOUTH * number + waypoint
            'E' -> waypoint = EAST * number + waypoint
            'W' -> waypoint = WEST * number + waypoint
            'F' -> position = waypoint * number + position
            'L' -> waypoint = waypoint.turn(number.toRadian())
            'R' -> waypoint = waypoint.turn(-number.toRadian())
            else -> error("Should not append")
        }
    }
}

