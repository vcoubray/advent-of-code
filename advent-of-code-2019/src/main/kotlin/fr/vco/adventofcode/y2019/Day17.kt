package fr.vco.adventofcode.y2019

fun main() {
    val input = getInputReader("Day17")

    val opCode = input.readText().toOpCode()
    while (!opCode.isEnded()) {
        opCode.exec()
    }

    val maze = opCode.stream.output.map { it.toChar() }.joinToString("").lines()
    opCode.stream.clear()
    println("Part 1 : ${maze.getIntersectionSum()}")

    val routine = RoutineBuilder(maze.getVacuumRobotPath()).build()

    opCode.restart()
    opCode.editOpCode(0, 2)
    opCode.stream.readln(routine.mainRoutine)
    routine.subRoutines.forEach { opCode.stream.readln(it.toString()) }
    opCode.stream.readln("n")
    opCode.exec()
    println("Part 2 : ${opCode.stream.output.last}")

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

private fun List<String>.getVacuumRobotPath() : List<Move>{
    val robot = this.getVacuumRobot()
    while (!robot.hasFinish) {
        when {
            get(robot.nextPosition()) == '#' -> robot.moveForward()
            get(robot.leftPosition()) == '#' -> robot.turnLeft()
            get(robot.rightPosition()) == '#' -> robot.turnRight()
            else -> robot.hasFinish = true
        }
    }
    return robot.path
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

private data class Move(val dir: String, var distance: Int) {
    override fun toString() = "$dir,$distance"
}

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


private class SubRoutine(val name: String, val path: List<Move>) {
    override fun toString() = path.joinToString(",")
}

private class Routine(val initialPath: List<Move>, val subRoutines: List<SubRoutine>) {
    val mainRoutine = subRoutines.fold(initialPath.joinToString(",")) { acc, a -> acc.replace(a.toString(), a.name) }
}

private class RoutineBuilder(
    val initialPath: List<Move>,
    val maxMoveBySubRoutine: Int = 4,
    val subRoutinesName: List<String> = listOf("A", "B", "C"),
) {
    val maxSubRoutine: Int = subRoutinesName.size
    fun build(): Routine {
        findValidSubRoutines(initialPath, emptyList())?.let { subRoutines ->
            return Routine(initialPath, subRoutines)
        } ?: throw Exception("No valid subRoutines found")
    }

    private fun findValidSubRoutines(path: List<Move>, subRoutines: List<SubRoutine>): List<SubRoutine>? {
        if (subRoutines.size == maxSubRoutine) {
            return if (isValid(subRoutines)) subRoutines
            else null
        }

        var newPath = path
        do {
            val r = subRoutines.firstOrNull { newPath.take(it.path.size) == it.path }
            r?.let { newPath = newPath.drop(it.path.size) }
        } while (r != null)

        if (newPath.isEmpty()) return subRoutines

        for (i in 1..maxMoveBySubRoutine) {
            val subRoutine = SubRoutine(subRoutinesName[subRoutines.size], newPath.take(i))
            val result = findValidSubRoutines(path, subRoutines + subRoutine)
            if (result != null) return result
        }
        return null
    }

    private fun isValid(subRoutines: List<SubRoutine>): Boolean {
        if (subRoutines.isEmpty() || subRoutines.any { it.path.isEmpty() }) return false

        var path = initialPath
        do {
            val r = subRoutines.firstOrNull { path.take(it.path.size) == it.path }
            r?.let { path = path.drop(it.path.size) }
        } while (r != null)
        return path.isEmpty()
    }
}
