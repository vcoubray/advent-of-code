package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day12")

    val map = HeightMap(input)
    println("Part 1 : ${map.findShortestAscendingPath()}")
    println("Part 2 : ${map.findShortestDescendingPath()}")
}

class HeightMap(input: List<String>) {

    data class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    }

    private val directions = listOf(
        Position(1, 0),
        Position(-1, 0),
        Position(0, 1),
        Position(0, -1)
    )

    private lateinit var start: Position
    private lateinit var end: Position

    private val map = input.mapIndexed { y, line ->
        line.mapIndexed { x, it ->
            when (it) {
                'S' -> {
                    start = Position(x, y)
                    'a'.code
                }
                'E' -> {
                    end = Position(x, y)
                    'z'.code
                }
                else -> it.code
            }
        }
    }

    fun findShortestAscendingPath() = findShortestPath(this.start, { it == this.end }) { current, nextHeight ->
        nextHeight <= map[current.y][current.x] + 1
    }

    fun findShortestDescendingPath() = findShortestPath(this.end, { map[it.y][it.x] == 'a'.code  }) { current, nextHeight ->
        nextHeight >= map[current.y][current.x] - 1
    }

    private fun findShortestPath(
        start: Position,
        endPredicate: (current: Position) -> Boolean,
        stepPredicate: (current: Position, nextHeight: Int) -> Boolean
    ): Int {
        val toVisit = ArrayDeque<Pair<Position, Int>>().apply { add(start to 0) }
        val visited = mutableListOf<Position>()

        while (toVisit.isNotEmpty()) {
            val (current, depth) = toVisit.removeFirst()
            if (current in visited) continue

            visited.add(current)
            if (endPredicate(current)) return depth
            directions.map { current + it }
                .filterNot(visited::contains)
                .forEach { next ->
                    map.getOrNull(next.y)?.getOrNull(next.x)?.let {
                        if (stepPredicate(current, it)) {
                            toVisit.addLast(next to depth + 1)
                        }
                    }
                }
        }
        throw Exception("No path found")
    }
}
