package fr.vco.aoc.y2024

const val WIDTH = 70
const val HEIGHT = 70

fun main() {
    val input = readLines("Day18")

    val fallingBytes = input.map { it.toFallingBytes() }
    val start = Position(0, 0)
    val end = Position(WIDTH, HEIGHT)

    println("Part 1: ${bfs(start, end, fallingBytes.take(1024).associateWith { true })}")
    println("Part 2: ${findBlockingByte(start, end, fallingBytes).let { (x, y) -> "$x,$y" }}")
}

fun bfs(start: Position, end: Position, fallenBytes: Map<Position, Boolean>): Int {
    val toVisit = ArrayDeque<Position>().apply { add(start) }
    val visited = mutableMapOf(start to 0)

    while (toVisit.isNotEmpty()) {
        val curr = toVisit.removeFirst()
        if (curr == end) {
            return visited[curr]!!
        }

        DIRECTION_MANHATTAN.map { curr + it }.forEach { neighbor ->
            if (!fallenBytes.containsKey(neighbor) && !visited.containsKey(neighbor)) {
                if (neighbor.x in 0..WIDTH && neighbor.y in 0..HEIGHT) {
                    toVisit.add(neighbor)
                    visited[neighbor] = visited[curr]!! + 1
                }
            }
        }

    }
    return -1
}

fun findBlockingByte(start: Position, end: Position, fallingBytes: List<Position>): Position {
    for (i in fallingBytes.indices) {
        val size = bfs(start, end, fallingBytes.take(i).associateWith { true })
        if (size == -1) {
            return fallingBytes[i - 1]
        }
    }
    return Position(-1, -1)
}

fun String.toFallingBytes(): Position {
    val (x, y) = this.split(",").map { it.toInt() }
    return Position(x, y)
}