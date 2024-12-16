package fr.vco.aoc.y2024

import java.util.*

fun main() {
    val input = readLines("Day16")

    var start = Position(0, 0)
    var end = Position(0, 0)
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                'S' -> start = Position(x, y)
                'E' -> end = Position(x, y)
            }
        }
    }

    val (cost, size) = input.findBestPath( Reindeer(start, 0), end)
    println("Part 1: $cost")
    println("Part 2: $size")

}

data class Reindeer(val position: Position, val direction: Int) {

    fun nextStatesWithCost() = listOf(
        moveForward() to 1,
        turn(1) to 1000,
        turn(-1) to 1000
    )

    private fun moveForward() = Reindeer(position + DIRECTION_MANHATTAN[direction], direction)

    private fun turn(turn: Int) = Reindeer(position, (direction + turn).mod(DIRECTION_MANHATTAN.size))
}

// Dijkstra
fun List<String>.findBestPath(start: Reindeer, end: Position): Pair<Int, Int> {
    val costs = mutableMapOf(start to 0)
    val previousState = mutableMapOf<Reindeer, MutableList<Reindeer>>(start to mutableListOf())
    val toVisit = PriorityQueue<Reindeer> { a, b -> costs[a]!! - costs[b]!! }
    toVisit.add(start)

    while (toVisit.isNotEmpty()) {
        val curr = toVisit.poll()
        if (curr.position == end) {
            return costs[curr]!! to previousState.getPath(curr).size
        }

        curr.nextStatesWithCost().forEach { (reindeer, cost) ->
            val nextCost = costs[curr]!! + cost

            if (nextCost == (costs[reindeer] ?: Int.MAX_VALUE)) {
                previousState.computeIfAbsent(reindeer) { mutableListOf() }.add(curr)
            } else if (nextCost < (costs[reindeer] ?: Int.MAX_VALUE)) {
                previousState[reindeer] = mutableListOf(curr)
            }

            if (nextCost < (costs[reindeer] ?: Int.MAX_VALUE)) {
                if (getOrNull(reindeer.position) != null && getOrNull(reindeer.position) != '#') {
                    costs[reindeer] = nextCost
                    toVisit.add(reindeer)
                }
            }
        }
    }

    return -1 to -1
}

fun Map<Reindeer, List<Reindeer>>.getPath(end: Reindeer): Set<Position> {
    val path = mutableSetOf(end.position)

    val toVisit = ArrayDeque<Reindeer>().apply { add(end) }
    while (toVisit.isNotEmpty()) {
        val curr = toVisit.remove()
        this[curr]!!.forEach { parent ->
            toVisit.add(parent)
            path.add(parent.position)
        }
    }

    return path
}
