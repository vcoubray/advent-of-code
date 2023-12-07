package fr.vco.adventofcode.y2019

import java.util.PriorityQueue

fun main() {
    val input = getInputReader("Day18")

    val mazeBuilder = MazeBuilder(input.readLines())

    println("Part 1 : ${mazeBuilder.build().resolve()}")
    println("Part 2 : ${mazeBuilder.buildPart2().resolve()}")
}

class MazeBuilder(grid: List<String>) {
    companion object {
        private val directions = listOf(
            Position(1, 0),
            Position(-1, 0),
            Position(0, 1),
            Position(0, -1),
        )
    }

    private val height = grid.size
    private val width = grid.first().length
    private var cells = grid.joinToString("")
    private val startId = cells.indexOf('@')
    private val neighbors = MutableList(width * height) { emptyList<Int>() }

    fun build(): Maze {
        val entities = mutableMapOf<Char, Int>()
        cells.forEachIndexed { i, cell ->
            if (cell.isEntity()) entities[cell] = i
            if (cell.isWall()) neighbors[i] = emptyList()
            else neighbors[i] = getNeighbors(i).map { it.y * width + it.x }
        }

        val reachable = entities.map { (entity, pos) -> entity to computeReachableEntities(pos) }.toMap()
        val distances = entities.map { (entity, pos) -> entity to computeDistance(entity, pos) }.toMap()

        return Maze(setOf('@'), entities, distances, reachable)
    }
    
    fun buildPart2(): Maze {
        val cells = this.replaceEntrance()

        val entities = mutableMapOf<Char, Int>()
        cells.forEachIndexed { i, cell ->
            if (cell.isEntity()) entities[cell] = i
            if (cell.isWall()) neighbors[i] = emptyList()
            else neighbors[i] = getNeighbors(i).map { it.y * width + it.x }
        }

        val reachable = entities.map { (entity, pos) -> entity to computeReachableEntities(pos) }.toMap()
        val distances = entities.map { (entity, pos) -> entity to computeDistance(entity, pos) }.toMap()

        return Maze(setOf('1', '2', '3', '4'), entities, distances, reachable)
    }


    private fun getNeighbors(id: Int): List<Position> {
        val pos = id.toPosition()
        return directions.map { pos + it }
            .filter { it.x in 0 until width && it.y in 0 until height }
            .filter { !cells[id].isWall() }
    }

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

    private fun computeDistance(startEntity: Char, startId: Int): Map<Char, Int> {
        val toVisit = ArrayDeque<Int>().apply { add(startId) }
        val visited = MutableList(neighbors.size) { -1 }
        visited[startId] = 0

        val distances = mutableMapOf(startEntity to 0)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (cells[current].isEntity() && current != startId) {
                distances[cells[current]] = visited[current]
            }
            neighbors[current]
                .filter { visited[it] == -1 }
                .forEach {
                    visited[it] = visited[current] + 1
                    toVisit.add(it)
                }
        }
        return distances
    }
    
    private fun replaceEntrance() = cells
        .replaceRange(startId - width - 1..startId - width + 1, "1#2")
        .replaceRange(startId - 1..startId + 1, "###")
        .replaceRange(startId + width - 1..startId + width + 1, "3#4")


    private fun Int.toPosition() = Position(this % width, this / width)
    private fun Char.isEntity() = this != '#' && this != '.'
    private fun Char.isWall() = this == '#'
}


class Maze(
    val starts: Set<Char>,
    val entities: Map<Char, Int>,
    val distances: Map<Char, Map<Char, Int>>,
    val reachable: Map<Char, Set<Char>>,
) {

    private fun getStartState() = MazeState(
        starts.toList(),
        starts.map { reachable[it]!! },
        entities.keys.associateWith { false }.toMutableMap().apply { this['@'] = true }
    )
    
    fun resolve(): Int {
        val start = getStartState()
        val toVisit = PriorityQueue(compareBy<MazeState> { it.steps }).apply { add(start) }
        val visited = mutableMapOf(start.id to start)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.poll()
            if (current.isFinal()) {
                return current.steps
            }
            nextStates(current).forEach { state ->
                if ((visited[state.id]?.steps ?: Int.MAX_VALUE) > state.steps) {
                    toVisit.add(state)
                    visited[state.id] = state
                }
            }
        }
        return -1
    }

    private fun nextStates(state: MazeState): List<MazeState> {
        return state.reachable.mapIndexed { i, reachable ->
            reachable.filter { entity -> state.visited[entity] == false }
                .filter { entity -> entity.isLowerCase() || state.visited[entity.toLowerCase()] == true }
                .map { target ->
                    MazeState(
                        state.current.toMutableList().apply { this[i] = target },
                        state.reachable.toMutableList().apply { this[i] = this[i] + this@Maze.reachable[target]!! },
                        state.visited.toMutableMap().apply { this[target] = true },
                        state.steps + distances[state.current[i]]!![target]!!,
                        state.path + target
                    )
                }
        }.flatten()
    }

}

class MazeState(
    val current: List<Char>,
    val reachable: List<Set<Char>>,
    val visited: Map<Char, Boolean>,
    val steps: Int = 0,
    val path: String = "$current",
) {
    val id = current.joinToString("") + "_" + visited.filter { (k, v) -> v && k.isLowerCase() }.keys.joinToString("")
    fun isFinal() = visited.none { (k, v) -> k.isLowerCase() && !v }
}




