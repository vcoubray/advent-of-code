package fr.vco.adventofcode.y2019

import java.util.PriorityQueue

fun main() {
    val input = getInputReader("/inputDay18.txt")

    val maze = Maze(input.readLines())
    println("Part 1 : ${resolve(maze)}")
}

class Maze(grid: List<String>) {

    companion object {
        private val directions = listOf(
            Position(1, 0),
            Position(-1, 0),
            Position(0, 1),
            Position(0, -1),
        )
    }

    val height = grid.size
    val width = grid.first().length
    val cells = grid.joinToString("")
    val neighbors = MutableList(width * height) { emptyList<Int>() }
    val entities = mutableMapOf<Char, Int>()
    val distances: Map<Char, MutableMap<Char, Int>>
    val reachable: Map<Char, Set<Char>>

    init {
        cells.forEachIndexed { i, cell ->
            if (cell.isEntity()) entities[cell] = i
            if (cell != '#') neighbors[i] = getNeighbors(i).map { it.y * width + it.x }
        }
        distances = entities.keys.associateWith { entities.keys.associateWith { -1 }.toMutableMap() }
        reachable = entities.map { (entity, pos) -> entity to computeReachableEntities(pos) }.toMap()

        entities.forEach { (entity, id) ->
            computeDistance(entity, id)
        }
    }

    private fun getNeighbors(id: Int): List<Position> {
        val pos = Position(id % width, id / width)
        return directions.map { pos + it }
            .filter { it.x in 0 until width && it.y in 0 until height }
            .filter { cells[id] != '#' }
    }

    private fun Char.isEntity() = this != '#' && this != '.'

    private fun computeReachableEntities(startId: Int): Set<Char> {
        val toVisit = ArrayDeque<Int>().apply { add(startId) }
        val visited = MutableList(neighbors.size) { false }
        visited[startId] = true

        val reachable = mutableSetOf<Char>()
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (cells[current].isEntity() && current != startId) {
                reachable.add(cells[current])
                continue
            }
            neighbors[current]
                .filter { !visited[it] }
                .forEach {
                    visited[it] = true
                    toVisit.add(it)
                }
        }
        return reachable
    }

    private fun computeDistance(startEntity: Char, startId: Int ) {
        val toVisit = ArrayDeque<Int>().apply { add(startId) }
        val visited = MutableList(neighbors.size) { -1 }
        visited[startId] = 0
        distances[startEntity]!![startEntity] = 0
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (cells[current].isEntity() && current != startId) {
                distances[startEntity]!![cells[current]] = visited[current]
            }
            neighbors[current]
                .filter { visited[it] == -1 }
                .forEach {
                    visited[it] = visited[current] + 1
                    toVisit.add(it)
                }
        }
    }

    fun getStartState() = State(
        this,
        '@',
        reachable['@']!!,
        entities.keys.associateWith { false }.toMutableMap().apply { this['@'] = true }
    )
}

class State(
    val maze : Maze,
    val current: Char,
//    val distances: Map<Char, MutableMap<Char, Int>>,
    val reachable: Set<Char>,
    val visited: Map<Char, Boolean>,
    val steps: Int = 0,
    val path: String = "$current",
) {

    val id = current + "_" + visited.filter { (k, v) -> v && k.isLowerCase() }.keys.joinToString("")

    fun nextStates(): List<State> {

        return reachable
            .filter { entity -> visited[entity] == false}
            .filter { entity -> entity.isLowerCase() || visited[entity.toLowerCase()] == true }
            .map { target ->
                State(
                    maze,
                    target,
                    reachable + maze.reachable[target]!!,
                    visited.toMutableMap().apply { this[target] = true },
                    steps + maze.distances[current]!![target]!!,
                    path + target
                )
            }

    }

    fun isFinal() = visited.none { (k, v) -> k.isLowerCase() && !v }
}

fun resolve(maze: Maze): Int {

    val start = maze.getStartState()
    val toVisit = PriorityQueue(compareBy<State> { it.steps }).apply { add(start) }
    val visited = mutableMapOf(start.id to start)

    while (toVisit.isNotEmpty()) {
        val current = toVisit.poll()
        if (current.isFinal()) {
            return current.steps
        }
        current.nextStates().forEach { state ->
            if ((visited[state.id]?.steps ?: Int.MAX_VALUE) > state.steps) {
                toVisit.add(state)
                visited[state.id] = state
            }
        }
    }
    return -1
}

