package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day23")
    val hike = HikeTrail(input)

    println("Part 1 : ${hike.findLongestPath()}")
}


class HikeTrail(input: List<String>) {

    val height = input.size
    val width = input.first().length

    val start = input.first().indexOf('.')
    val end = input.last().indexOf('.') + width * (height - 1)
    val neighbours = List(height * width) {
        val pos = Position(it % width, it / height)
        when (input[pos.y][pos.x]) {
            '#' -> emptyList()
            '>' -> listOf(pos + Direction.EAST.vector)
            '<' -> listOf(pos + Direction.WEST.vector)
            'v' -> listOf(pos + Direction.SOUTH.vector)
            '^' -> listOf(pos + Direction.NORTH.vector)
            else -> Direction.entries.map { d -> pos + d.vector }
                .filter { (x, y) -> x in 0..<width && y in 0..<height }
                .filter { (x, y) -> input[y][x] != '#' }
        }.map { p -> p.toId() }
    }

    val crossingGraph = findCrossingGraph()

    private fun Position.toId() = y * width + x

    fun findCrossingGraph(): List<Map<Int, Int>> {
        val crossings = mutableListOf(start)
        neighbours.forEachIndexed { i, it -> if (it.size >= 3) crossings.add(i) }
        crossings.add(end)
        val distances = List(crossings.size) { mutableMapOf<Int, Int>() }

        for (i in crossings) {
            val toVisit = ArrayDeque<Int>().apply { add(i) }
            val visited = MutableList(neighbours.size) { -1 }
            visited[i] = 0

            while (toVisit.isNotEmpty()) {
                val current = toVisit.removeFirst()

                val crossingIndex = crossings.indexOf(current)
                if (crossingIndex != -1 && current != i) {
                    distances[crossings.indexOf(i)][crossingIndex] = visited[current]
                    continue
                }
                neighbours[current].filter { visited[it] == -1 }
                    .forEach {
                        visited[it] = visited[current] + 1
                        toVisit.add(it)
                    }
            }
        }

        return distances
    }

    fun findLongestPath() : Int{
        val start = 0
        val toVisit = ArrayDeque<Int>().apply { add(start) }
        val visited = MutableList(crossingGraph.size) { -1 }
        visited[start] = 0

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            crossingGraph[current].forEach{n, d ->
                val dist = d + visited[current]
                if(visited[n] < dist) {
                    visited[n] = dist
                    toVisit.add(n)
                }
            }
        }
        return visited.last()
    }

}
