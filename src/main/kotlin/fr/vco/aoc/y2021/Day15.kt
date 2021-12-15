package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day15").map { it.map { c -> c.digitToInt() } }

    val cavern = input.toTileList()
    println("Part 1 : ${cavern.dijkstra()}")

    val bigCavern = input.multiply(5).toTileList()
    println("Part 2 : ${bigCavern.dijkstra()}")

}

private fun List<List<Int>>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
private fun List<List<Int>>.convertCoord(x: Int, y: Int) = this.get(x, y)?.let { this.first().size * y + x }
private fun List<List<Int>>.getNeighbors(x: Int, y: Int) = listOfNotNull(
    this.convertCoord(x - 1, y),
    this.convertCoord(x + 1, y),
    this.convertCoord(x, y - 1),
    this.convertCoord(x, y + 1),
)

private fun List<List<Int>>.multiply(times: Int) = List(this.size * times) { this[it % this.size].increaseRisk(it / this.size).multiplyLine(times) }
private fun List<Int>.multiplyLine(times: Int) = List(times) { this.increaseRisk(it) }.flatten()
private fun List<Int>.increaseRisk(increase: Int) = this.map { ((it - 1 + increase) % 9) + 1 }

private data class Tile(val risk: Int, val neighborsId: List<Int>)

private fun List<List<Int>>.toTileList() = this.flatMapIndexed { y, line ->
    line.mapIndexed { x, c -> Tile(c, this.getNeighbors(x, y)) }
}

private fun List<Tile>.dijkstra(start: Int = 0, end: Int = this.size - 1): Int {
    val costComparator = compareBy <Pair<Int, Int>>{ it.second }
    val toVisit = PriorityQueue(costComparator).apply { add(0 to 0) }
    val visited = mutableMapOf<Int, Int>()

    while (!toVisit.isEmpty()) {
        val (tileId, cost) = toVisit.poll()
        val current = this[tileId]
        if (visited.getOrDefault(tileId, Int.MAX_VALUE) > cost + current.risk) {
            visited[tileId] = cost + current.risk
            current.neighborsId.forEach {
                toVisit.add(it to cost + current.risk)
            }
        }
    }
    return visited[end]!! - this[start].risk
}