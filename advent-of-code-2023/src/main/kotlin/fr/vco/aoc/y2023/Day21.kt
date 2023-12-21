package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day21")
        
    println("Part 1: ${GardenMaze(input).bfs(64)}")
}

class GardenMaze(input: List<String>) {
    val width = input.first().length
    val height = input.size

    val maze = input.joinToString("")
    val start = maze.indexOfLast { it == 'S' }

    val neighbors = List(width * height) { i ->
        val pos = Position(i % width, i / width)
        Direction.entries.map { pos + it.vector }
            .filter { it.x in 0..<width && it.y in 0..<height }
            .map { it.y * width + it.x }
            .filter { maze[it] == '.' }
    }

    fun bfs(maxStep: Int): Int {
        val toVisit = ArrayDeque<Int>().apply { addLast(start) }
        val visited = MutableList(width * height) { -1 }
        visited[start] = 0

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (visited[current] >= maxStep) continue

            neighbors[current].filter { visited[it] == -1 }
                .forEach {
                    visited[it] = visited[current] + 1
                    toVisit.add(it)
                }
        }

        return visited.filterNot { it == -1 }
            .count { it % 2 == maxStep % 2 }
    }

}