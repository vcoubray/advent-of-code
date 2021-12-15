package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day15")

    val cavern = input.toCellList()
    println("Part 1 : ${cavern.dijkstra()}")

}

private fun List<String>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
private fun List<String>.convertCoord(x: Int, y: Int) = this.get(x, y)?.let { this.first().length * y + x }
private fun List<String>.getNeighbors(x: Int, y: Int) = listOfNotNull(
    this.convertCoord(x - 1, y),
    this.convertCoord(x + 1, y),
    this.convertCoord(x, y - 1),
    this.convertCoord(x, y + 1),
)

private data class Tile(val risk: Int, val neighborsId: List<Int>)

private fun List<String>.toCellList() = this.flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Tile(c.digitToInt(), this.getNeighbors(x, y))
    }
}

private fun List<Tile>.dijkstra(start: Int = 0, end: Int = this.size - 1): Int {
    val toVisit = LinkedList<Pair<Int, Int>>().apply { addFirst(0 to 0) }
    val visited = mutableMapOf<Int, Int>()

    while (!toVisit.isEmpty()) {
        val (tileId, cost) = toVisit.pop()
        val current = this[tileId]
        if (visited.getOrDefault(tileId, Int.MAX_VALUE) > cost + current.risk) {
            visited[tileId] = cost + current.risk
            current.neighborsId.forEach {
                toVisit.addLast(it to cost + current.risk)
            }
        }
    }
    return visited[end]!! - this[start].risk
}