package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.Position
import fr.vco.adventofcode.getInputReader
import java.lang.IllegalStateException

fun main() {
    val input = getInputReader("/2019/inputDay17.txt")

    val opCode = input.readText().toOpCode()
    while (!opCode.isEnded()) {
        opCode.exec()
    }

    val maze = opCode.stream.output.map { it.toChar() }.joinToString("").lines()
//    maze.forEach { println(it) }

    println("part 1 : ${maze.getIntersectionSum()}")

    val robot = maze.getVacuumRobot()
    maze.moveVacuumRobot(robot)
    val path = robot.path.joinToString(",") { "${it.dir}${it.distance}" }

    println(path)
    println(path.replace("L10,R8,R8", "A"))
    println(path.replace("L10,R8,R8", "A").replace("L10,L12,R8,R10", "B"))
    println(path.replace("L10,R8,R8", "A").replace("L10,L12,R8,R10", "B").replace("R10,L12,R10", "C"))
    println(path.length)


    val routine = Routine(robot.path)
    routine.functions = listOf(
        listOf(Move("L", 10), Move("R", 8), Move("R", 8)),
        listOf(Move("L", 10), Move("L", 12), Move("R", 8), Move("R", 10)),
        listOf(Move("R", 10), Move("L", 12), Move("R", 10))
    )
    println(routine.isValidRoutine())
}

private class Routine(val initialPath: List<Move>) {
    lateinit var functions: List<List<Move>>

    fun isValidRoutine(): Boolean {
        return if (functions.isEmpty() || functions.any { it.isEmpty() }) false
        else validRoutine(initialPath)
    }

    fun validRoutine(path: List<Move>): Boolean {
        if (path.isEmpty()) return true
        functions.forEach {
            if (path.take(it.size) == it) return validRoutine(path.drop(it.size))
        }
        return false
    }
}

private fun List<String>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
private fun List<String>.get(position: Position) = this.get(position.x, position.y)
private fun List<String>.getIntersectionSum(): Int {
    var sum = 0
    repeat(size) { y ->
        repeat(this[y].length) { x ->
            if (get(x, y) == '#' &&
                get(x - 1, y) == '#' &&
                get(x + 1, y) == '#' &&
                get(x, y - 1) == '#' &&
                get(x, y + 1) == '#'
            ) {
                sum += x * y
            }
        }
    }
    return sum
}

private fun List<String>.getVacuumRobot(): VacuumRobot {
    repeat(size) { y ->
        repeat(this[y].length) { x ->
            val dir = when (get(x, y)) {
                '>' -> LEFT
                '<' -> RIGHT
                'v' -> BOTTOM
                '^' -> TOP
                else -> null
            }
            dir?.let { return VacuumRobot(Position(x, y), it) }
        }
    }
    throw IllegalStateException("No vacuum Robot found")
}

private fun List<String>.moveVacuumRobot(robot: VacuumRobot) {
    while (!robot.hasFinish) {
        when {
            get(robot.nextPosition()) == '#' -> robot.moveForward()
            get(robot.leftPosition()) == '#' -> robot.turnLeft()
            get(robot.rightPosition()) == '#' -> robot.turnRight()
            else -> robot.hasFinish = true
        }
    }
}

private const val TOP = 0
private const val RIGHT = 1
private const val BOTTOM = 2
private const val LEFT = 3

private var DIRECTION = listOf(
    Position(0, -1), // TOP
    Position(1, 0),  // RIGHT
    Position(0, 1),  // BOTTOM
    Position(-1, 0)  // LEFT
)

private data class Move(val dir: String, var distance: Int)
private data class VacuumRobot(var position: Position, var direction: Int) {
    var hasFinish = false
    val path = mutableListOf<Move>()

    fun nextPosition() = position + DIRECTION[direction]
    fun rightPosition() = position + DIRECTION[(direction + 1) % 4]
    fun leftPosition() = position + DIRECTION[(direction + 3) % 4]

    fun moveForward() {
        position = nextPosition()
        path.last().distance++
    }

    fun turnRight() {
        position = rightPosition()
        direction = (direction + 1) % 4
        path.add(Move("R", 1))
    }

    fun turnLeft() {
        position = leftPosition()
        direction = (direction + 3) % 4
        path.add(Move("L", 1))
    }
}