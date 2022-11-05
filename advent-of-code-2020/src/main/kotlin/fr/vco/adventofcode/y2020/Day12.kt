package fr.vco.adventofcode.y2020

import kotlin.math.*

fun main() {
    val input = getInputReader("/2020/inputDay12.txt")
    val lines = input.readLines()

    println("Part 1 : ${lines.fold(Boat()){ boat, it -> boat.move(it)}.dist(ORIGIN)}")
    println("Part 2 : ${lines.fold(Boat(waypoint = Position(10, 1))){ boat, it -> boat.move(it,boat::moveWaypoint)}.dist(ORIGIN)}")
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

data class Boat(
    val position: Position = ORIGIN,
    val waypoint: Position = EAST,
) {

    fun move(instruction: String, move: (Position, Int) -> Boat = ::moveBoat): Boat {
        val ins = instruction.first()
        val number = instruction.drop(1).toInt()
        return when (ins) {
            'N' -> move(NORTH, number)
            'S' -> move(SOUTH, number)
            'E' -> move(EAST, number)
            'W' -> move(WEST, number)
            'F' -> this.copy(position = waypoint * number + position)
            'L' -> this.copy(waypoint = waypoint.turn(number.toRadian()))
            'R' -> this.copy(waypoint = waypoint.turn(-number.toRadian()))
            else -> error("Should not append")
        }
    }

    fun moveBoat(direction: Position, factor: Int) = this.copy( position = direction * factor + position )
    fun moveWaypoint(direction: Position, factor: Int) = this.copy( waypoint = direction * factor + waypoint )

    fun dist(position : Position = ORIGIN) = this.position.dist(position)
}
