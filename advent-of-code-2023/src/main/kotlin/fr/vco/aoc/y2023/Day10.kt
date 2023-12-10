package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day10")
    val pipeMaze = PipeMaze(input)

    println("Part 1: ${pipeMaze.getFarthestDistance()}")
    println("Part 2: ${pipeMaze.getInsideCount()}")
}

val pipeTypes = mapOf(
    '|' to listOf(Direction.NORTH, Direction.SOUTH),
    '-' to listOf(Direction.EAST, Direction.WEST),
    'L' to listOf(Direction.NORTH, Direction.EAST),
    'J' to listOf(Direction.NORTH, Direction.WEST),
    '7' to listOf(Direction.SOUTH, Direction.WEST),
    'F' to listOf(Direction.SOUTH, Direction.EAST),
    '.' to listOf(),
    'S' to listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
)

class PipeMaze(input: List<String>) {
    private val width = input.first().length
    private val height = input.size
    private val maze = input.joinToString("")
    private val start = maze.indexOf('S')
    private val nodes = maze.mapIndexed(::getNeighbors)
    private val loop = getLoop()
    
    fun getFarthestDistance() = getLoop().count { it != ' ' } / 2
    fun getInsideCount() = loop.chunked(width).sumOf { it.countInsideLoop() }

    private fun Position.toId() = y * width + x
    private fun Int.toPosition() = Position(this % width, this / width)
    private fun getNeighbors(id: Int, symbol: Char) = pipeTypes[symbol]!!
        .map { it.vector + id.toPosition() }
        .filter { it.x in 0..<width && it.y in 0..<height }
        .map { it.toId() }

    private fun getLoop(): String {
        val toVisit = ArrayDeque<Int>().apply { addLast(start) }
        val visited = IntArray(width * height) { -1 }
        visited[start] = 0

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            nodes[current].filter { visited[it] == -1 }
                .filter { current in nodes[it] }
                .forEach {
                    visited[it] = visited[current] + 1
                    toVisit.addLast(it)
                }
        }
        return maze.mapIndexed { i, it -> if (visited[i] >= 0) it else " " }.joinToString("")
    }

    private fun String.countInsideLoop(): Int {
        var insideCount = 0
        var verticalPipeCount = 0

        this.filterNot { it == '-' }
            .replace("L7", "|")
            .replace("FJ", "|")
            .forEach {
                when {
                    it == '|' -> verticalPipeCount++
                    it == ' ' && verticalPipeCount % 2 != 0 -> insideCount++
                }
            }
        return insideCount
    }
}