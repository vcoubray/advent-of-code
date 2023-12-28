package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day21")

    println("Part 1: ${GardenMaze(input).bfs(64)}")
    println("Part 2: ${GardenMaze(input).getPolynomial().resolve(26501365)}")
}

class GardenMaze(private val maze: List<String>) {
    private val width = maze.first().length
    private val height = maze.size
    private val start = findStart()

    private fun findStart(): Position {
        val y = maze.indexOfFirst { 'S' in it }
        val x = maze[y].indexOfFirst { it == 'S' }
        return Position(x, y)
    }

    private fun Position.getNeighbours() =
        Direction.entries.map { this + it.vector }
            .filter { (x, y) -> maze[y.mod(height)][x.mod(width)] != '#' }

    fun bfs(maxStep: Int): Int {
        val toVisit = ArrayDeque<Position>().apply { addLast(start) }
        val visited = mutableMapOf(start to 0)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (visited[current]!! >= maxStep) continue

            current.getNeighbours().filter { it !in visited }
                .forEach {
                    visited[it] = visited[current]!! + 1
                    toVisit.add(it)
                }
        }
        return visited.values.filterNot { it == -1 }
            .count { it % 2 == maxStep % 2 }
    }
    
    fun getPolynomial(): Polynomial {
        val x = width / 2
        val y = x + width
        val z = y + width
        
        val X = bfs(x).toDouble()
        val Y = bfs(y).toDouble()
        val Z = bfs(z).toDouble()
        
        val a = (x * (Z - Y) + y * (X - Z) + z * (Y - X)) / ((x - y) * (x - z) * (y - z))
        val b = (X - Y) / (x - y) - a * (x + y)
        val c = X - a * x * x - b * x
        return Polynomial(a, b, c)
    }
}

data class Polynomial(private val a: Double, private val b: Double, private val c: Double) {
    fun resolve(x: Long): Long = (a * x * x + b * x + c).toLong()
} 