package fr.vco.adventofcode.y2019

fun main() {
    val input = readLines("Day20")
    val maze = DonutMaze(input)
    
    println("Part 1 : ${maze.countStep()}")
}


class DonutMaze(input: List<String>) {

    val directions = listOf(
        Position(1, 0),
        Position(-1, 0),
        Position(0, 1),
        Position(0, -1)
    )
    val width = input.first().length
    val height = input.size
    
    val maze = input.joinToString("")
    val neighbors = List(width * height) { getNeighbors(it) }
    val start: Int
    val end: Int

    init {
        val portals = mutableListOf<Pair<String, Int>>()
        maze.forEachIndexed { i, c ->
            if (c.isLetter()) {
                val neighborIds = neighbors[i]
                if (neighborIds.size == 2) {
                    val coord = neighborIds.first { maze[it] == '.' }
                    val coordName = neighborIds.first { maze[it].isLetter() }
                    val name = if (coordName < i) "${maze[coordName]}$c" else "$c${maze[coordName]}"

                    portals.add(name to coord)

                }
            }
        }

        start = portals.first{it.first == "AA"}.second
        end = portals.first{it.first == "ZZ"}.second
        
        portals.groupBy { (name, _) -> name }
            .forEach { (_, coords) ->
                if (coords.size == 2) {
                    val (c1, c2) = coords.map { it.second }
                    neighbors[c1].add(c2)
                    neighbors[c2].add(c1)
                }
            }
        
    }

    private fun Position.toId() = y * width + x
    private fun Int.toPosition() = Position(this % width, this / width)
    private fun getNeighbors(id: Int) = directions.asSequence()
        .map { id.toPosition() + it }
        .filter { (x, y) -> x in 0..<width && y in 0..<height }
        .map { it.toId() }
        .filter { !maze[it].isWall() }
        .toMutableList()

    private fun Char.isWall() = this in " #"
    
    fun countStep(): Int {
        val toVisit = ArrayDeque<Int>().apply { this.addLast(start) }
        val visited = MutableList(maze.length) { -1 }
        visited[start] = 0

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current == end) {
                return visited[current]
            }
            neighbors[current].filter { visited[it] == -1 }
                .forEach { n ->
                    visited[n] = visited[current] + 1
                    toVisit.addLast(n)
                }
        }
        return 0
    }
        
}