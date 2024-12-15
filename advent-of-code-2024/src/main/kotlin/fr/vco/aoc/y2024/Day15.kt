package fr.vco.aoc.y2024

const val EMPTY = -1
const val WALL = -2

fun main() {
    val input = readLines("Day15")
    val (grid, inputInstructions) = input.split { it.isBlank() }

    val instructions = inputInstructions.joinToString("")

    println("Part 1: ${grid.toWarehouse().apply{execute(instructions)}.getScore()}")
    println("Part 2: ${grid.toWarehouse(2).apply{
        execute(instructions)
        boxes.sortedBy { it.first().y }.forEach(::println)
    
    }.getScore()}")
}

fun List<String>.toWarehouse(wide: Int = 1): WarehouseState {
    var start = Position(0, 0)
    val boxes = mutableListOf<List<Position>>()
    val grid = this.mapIndexed { y, line ->
        line.flatMapIndexed { x, c ->
            when (c) {
                '#' -> List(wide) { WALL }
                '.' -> List(wide) { EMPTY }
                'O' -> {
                    boxes.add( List(wide) { Position(wide * x + it, y ) })
                    List(wide) { boxes.size - 1 }
                }
                else -> {
                    start = Position(wide * x , y)
                    List(wide) { EMPTY }
                }
            }
        }.toMutableList()
    }

    return WarehouseState(start, grid, boxes)
}


class WarehouseState(
    private var robot: Position,
    private val grid: List<MutableList<Int>>,
    val boxes: MutableList<List<Position>>,
) {

    companion object {
        val INSTRUCTIONS = mapOf(
            '<' to Position(-1, 0),
            '>' to Position(1, 0),
            '^' to Position(0, -1),
            'v' to Position(0, 1)
        )
    }

    fun execute(instructions: String) = instructions.forEach { execute(INSTRUCTIONS[it]!!) }

    fun execute(instruction: Position) {
        if (tryMove(robot, instruction)) {
            robot += instruction
        }
    }

    fun tryMove(robot: Position, direction: Position): Boolean {
        val targetPos = robot + direction
        return when (val targetId = grid.getOrNull(targetPos)) {
            null -> false
            WALL -> false
            EMPTY -> true
            else -> tryMoveBox(targetId, direction)
        }
    }

    fun tryMoveBox(startBoxId: Int, direction: Position): Boolean {
        val toVisit = ArrayDeque<Int>().apply { add(startBoxId) }
        val toMove = mutableSetOf(startBoxId)

        while (toVisit.isNotEmpty()) {
            val boxId = toVisit.removeFirst()

            boxes[boxId].forEach { boxPos ->
                val target = boxPos + direction
                val targetId = grid.getOrNull(target)
                if (targetId == null || targetId == WALL) {
                    return false
                }
                if (targetId >= 0 && targetId !in toMove) {
                    toVisit.add(targetId)
                    toMove.add(targetId)
                }
            }

        }

        toMove.forEach { boxId -> boxes[boxId].forEach { grid[it] = EMPTY } }
        toMove.forEach { boxId -> boxes[boxId] = boxes[boxId].map { it + direction } }
        toMove.forEach { boxId -> boxes[boxId].forEach { grid[it] = boxId } }

        return true
    }

    fun getScore() = boxes.map { it.first() }
        .sumOf { (x, y) -> y * 100 + x }

}
