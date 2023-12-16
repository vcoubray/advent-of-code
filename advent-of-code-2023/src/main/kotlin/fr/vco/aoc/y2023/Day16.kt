package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day16")

    println("Part 1: ${ContraptionMaze(input).countEnergized()}")
    println("Part 2: ${ContraptionMaze(input).findBestEnergizing()}")
}

class ContraptionMaze(val grid: List<String>) {
    companion object {
        const val NORTH = 0
        const val SOUTH = 1
        const val EAST = 2
        const val WEST = 3

        val DIRECTIONS = listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)

        val mirrorTypes = mapOf(
            '\\' to mapOf(
                NORTH to listOf(WEST),
                SOUTH to listOf(EAST),
                WEST to listOf(NORTH),
                EAST to listOf(SOUTH)
            ),
            '/' to mapOf(
                NORTH to listOf(EAST),
                SOUTH to listOf(WEST),
                WEST to listOf(SOUTH),
                EAST to listOf(NORTH)

            ),
            '-' to mapOf(
                NORTH to listOf(WEST, EAST),
                SOUTH to listOf(WEST, EAST),
                WEST to listOf(WEST),
                EAST to listOf(EAST)
            ),
            '|' to mapOf(
                NORTH to listOf(NORTH),
                SOUTH to listOf(SOUTH),
                WEST to listOf(NORTH, SOUTH),
                EAST to listOf(NORTH, SOUTH)
            ),
            '.' to mapOf(
                NORTH to listOf(NORTH),
                SOUTH to listOf(SOUTH),
                WEST to listOf(WEST),
                EAST to listOf(EAST)
            )
        )
    }

    val width = grid.first().length
    val height = grid.size

    val neighbours = List(4 * width * height) { id ->
        val pos = id.toBeamPosition()
        val symbol = grid[pos.position.y][pos.position.x]
        getNeighbors(symbol, pos).map { it.toId() }
    }

    data class BeamPosition(val position: Position, val direction: Int) {
        operator fun plus(direction: Int) = BeamPosition(
            position = position + DIRECTIONS[direction].vector,
            direction = direction
        )
    }

    fun BeamPosition.toId() = direction * height * width + position.y * width + position.x
    fun Int.toBeamPosition() = BeamPosition(
        Position(this % width, (this / width) % height),
        this / (height * width)
    )

    fun getNeighbors(symbol: Char, state: BeamPosition): List<BeamPosition> {
        return mirrorTypes[symbol]!![state.direction]!!
            .map { direction -> state + direction }
            .filter { it.position.x in 0..<width && it.position.y in 0..<height }
    }

    fun countEnergized(start: BeamPosition = BeamPosition(Position(0, 0), EAST)): Int {
        val toVisit = ArrayDeque<Int>().apply { addLast(start.toId()) }
        val visited = MutableList(4 * height * width) { false }
        val energized = MutableList(height * width) { false }
        visited[start.toId()] = true
        energized[start.position.x + start.position.y * width] = true

        while (toVisit.isNotEmpty()) {

            val current = toVisit.removeFirst()
            neighbours[current].filter { !visited[it] }
                .forEach {
                    toVisit.addLast(it)
                    visited[it] = true
                    val pos = it.toBeamPosition().position
                    energized[pos.y * width + pos.x] = true
                }

        }
        return energized.count { it }
    }

    fun findBestEnergizing(): Int {
        return List(4 * width * height) { it.toBeamPosition() }
            .filter { (pos) -> pos.x == 0 || pos.x == width - 1 || pos.y == 0 || pos.y == height - 1 }
            .maxOf(::countEnergized)
    }
}

