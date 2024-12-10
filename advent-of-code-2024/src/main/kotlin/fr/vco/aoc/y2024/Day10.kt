package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day10")

    val grid = input.map { line -> line.map { it.digitToInt() } }

    val hikingHeads =
        grid.flatMapIndexed { y, line -> line.mapIndexedNotNull { x, n -> Position(x, y).takeIf { n == 0 } } }

    println("Part 1: ${hikingHeads.sumOf { grid.findHikingTrails(it).toSet().size }}")
    println("Part 2: ${hikingHeads.sumOf { grid.findHikingTrails(it).size }}")
}

fun List<List<Int>>.findHikingTrails(start: Position): List<Position> {
    val toVisit = ArrayDeque<Position>().apply { add(start) }

    val hikings = mutableListOf<Position>()
    while (toVisit.isNotEmpty()) {

        val curr = toVisit.removeFirst()
        if (this[curr] == 9) {
            hikings.add(curr)
        }

        DIRECTION_MANHATTAN.map { curr + it }.forEach { n ->
            if (this.getOrNull(n) == this[curr] + 1) {
                toVisit.addLast(n)
            }
        }
    }
    return hikings
}