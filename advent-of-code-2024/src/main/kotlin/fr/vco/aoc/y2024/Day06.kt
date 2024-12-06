package fr.vco.aoc.y2024


fun main() {
    val input = readLines("Day06")

    val startY = input.indexOfFirst { it.contains("^") }
    val startX = input[startY].indexOfFirst { it == '^' }
    val start = Position(startX, startY)

    val visitedPositions = GuardState(start).play(input)
    println("Part 1 : ${visitedPositions.size} ")
    println("Part 2 : ${GuardState(start).playWithTrickyObstacle(input, visitedPositions)} ")

}

class GuardState(
    private val start: Position,
    private val customObstacle: Position? = null
) {
    companion object {
        val DIRECTIONS = listOf(Position(0, -1), Position(1, 0), Position(0, 1), Position(-1, 0))
    }

    private var position = start
    private var direction: Int = 0

    private fun List<String>.get(p: Position) = this.getOrNull(p.y)?.getOrNull(p.x)

    private fun turn() {
        direction = (direction + 1) % 4
    }

    private fun move(grid: List<String>) {
        val target = position + DIRECTIONS[direction]

        if (grid.get(target) == '#' || target == customObstacle) {
            turn()
        } else {
            position = target
        }
    }

    fun play(grid: List<String>): Set<Position> {
        val visited = mutableMapOf<Pair<Position, Int>, Boolean>()
        while (grid.get(position) != null && !visited.contains(position to direction)) {
            visited[position to direction] = true
            move(grid)
        }
        return visited.keys.map { (pos, _) -> pos }.toSet()
    }

    fun playWithTrickyObstacle(grid: List<String>, obstacles: Set<Position>): Int {
        return obstacles.map { obstacle -> GuardState(start, obstacle) }
            .count { guard ->
                guard.play(grid)
                grid.get(guard.position) != null
            }
    }
}