package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day23")
    val hike = HikeTrail(input)

    println("Part 1 : ${findLongestPath(hike.crossingGraph)}")
    println("Part 2 : ${findLongestPath(hike.crossingGraphCyclic)}")
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

    val cyclicNeighbours = List(height * width) {
        val pos = Position(it % width, it / height)
        when (input[pos.y][pos.x]) {
            '#' -> emptyList()
            else -> Direction.entries.map { d -> pos + d.vector }
                .filter { (x, y) -> x in 0..<width && y in 0..<height }
                .filter { (x, y) -> input[y][x] != '#' }
        }.map { p -> p.toId() }
    }

    val crossingGraph = findCrossingGraph(neighbours)
    val crossingGraphCyclic = findCrossingGraph(cyclicNeighbours)

    private fun Position.toId() = y * width + x

    private fun findCrossingGraph(neighbours: List<List<Int>>): List<Map<Int, Int>> {
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
}

fun findLongestPath(crossingGraph: List<Map<Int, Int>>) =
    findLongestPathRecursively( crossingGraph, 0, 0, listOf(0))


fun findLongestPathRecursively(crossingGraph: List<Map<Int, Int>>, current: Int, distance: Int, visited: List<Int>): Int {
    if (current == crossingGraph.lastIndex) {
        return distance
    }
    val neighbours = crossingGraph[current].filter { (n, _) -> n !in visited }
    if (neighbours.isEmpty()) return 0

    return neighbours.maxOf { (n, d) ->
        findLongestPathRecursively(crossingGraph, n, d + distance, visited + n)
    }
}