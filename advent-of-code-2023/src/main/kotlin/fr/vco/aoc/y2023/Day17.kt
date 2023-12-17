package fr.vco.aoc.y2023

import java.util.PriorityQueue

fun main() {
    val input = readLines("Day17")

    println("Part 1: ${LavaPool(input).getBestPath()}")
    println("Part 2: ${LavaPool(input, 3, 10).getBestPath()}")
}

class LavaPool(grid: List<String>, private val minStep: Int = 0, private val maxStep: Int = 3) {
    private val width = grid.first().length
    private val height = grid.size
    private val pool = grid.map { it.map { c -> c.digitToInt() } }

    data class CruciblePos(val pos: Position, val direction: Direction)

    private fun getPossibleMoves(pos: CruciblePos): List<Pair<CruciblePos, Int>> {
        var currentLoss = 0
        val turningDirections = Direction.entries
            .filterNot { -it.vector == pos.direction.vector || it.vector == pos.direction.vector }

        return (1..maxStep).asSequence()
            .map { pos.pos + (pos.direction.vector * it) }
            .filter { (x, y) -> x in 0..<width && y in 0..<height }
            .map {
                currentLoss += pool[it.y][it.x]
                it to currentLoss
            }
            .drop(minStep)
            .flatMap { (pos, heatLoss) ->
                turningDirections.map { dir -> CruciblePos(Position(pos.x, pos.y), dir) to heatLoss }
            }
            .toList()
    }

    fun getBestPath(): Int {
        val start = Position(0, 0)
        val starts = listOf(
            CruciblePos(start, Direction.EAST),
            CruciblePos(start, Direction.SOUTH)
        )
        val end = Position(width - 1, height - 1)
        val visited = starts.associateWith { 0 }.toMutableMap()
        val toVisit = PriorityQueue<CruciblePos> { a, b -> visited[a]!! - visited[b]!!}
        toVisit.addAll(starts)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.poll()
            if (current.pos == end) {
                return visited[current]!!
            }
            getPossibleMoves(current).forEach { (pos, heatLoss) ->
                val totalHeatLoss = heatLoss + visited[current]!!
                if (pos !in visited || totalHeatLoss < visited[pos]!!) {
                    visited[pos] = totalHeatLoss
                    toVisit.add(pos)
                }
            }
        }
        return -1
    }
}