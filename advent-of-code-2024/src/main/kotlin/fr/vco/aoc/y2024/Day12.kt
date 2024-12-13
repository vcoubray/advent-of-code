package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day12")

    val visited = input.map { line -> MutableList(line.length) { false } }
    val regions = mutableListOf<GardenRegion>()

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (!visited[y][x]) {
                regions.add(input.findRegion(Position(x, y), visited))
            }
        }
    }

    println("Part 1: ${regions.sumOf { it.priceByPerimeter }}")
    println("Part 2: ${regions.sumOf { it.priceBySide(input) }}")
}

data class GardenRegion(val plotType: Char, val plots: List<Position>, val perimeter: Int) {
    private val area = plots.size
    val priceByPerimeter = area * perimeter

    fun priceBySide(garden: List<String>) = area * plots.sumOf { it.countCorner(garden) }

    private fun Position.countCorner(garden: List<String>): Int {
        val diag = listOf(Position(-1, -1), Position(1, 1), Position(1, -1), Position(-1, 1))
            .map { p -> Triple(p, Position(p.x, 0), Position(0, p.y)) }

        return diag.map { (diag, b1, b2) -> Triple(this + diag, this + b1, this + b2) }
            .count { (diag, b1, b2) -> isCorner(garden.getOrNull(diag), garden.getOrNull(b1), garden.getOrNull(b2)) }
    }


    private fun isCorner(diagonal: Char?, border1: Char?, border2: Char?): Boolean {
        if (border1 != plotType && border2 != plotType) return true
        if (border1 == plotType && border2 == plotType && diagonal != plotType) return true
        return false
    }
}

fun List<String>.findRegion(start: Position, visited: List<MutableList<Boolean>>): GardenRegion {
    val plotType = this[start.y][start.x]
    val toVisit = ArrayDeque<Position>().apply { add(start) }
    val plots = mutableListOf<Position>().apply { add(start) }
    visited[start] = true

    var perimeter = 0
    while (toVisit.isNotEmpty()) {
        val curr = toVisit.removeFirst()

        DIRECTION_MANHATTAN.map { it + curr }.forEach { neighbor ->
            if (plotType == this.getOrNull(neighbor)) {
                if (visited.getOrNull(neighbor) == false) {
                    toVisit.addLast(neighbor)
                    plots.add(neighbor)
                    visited[neighbor] = true
                }
            } else {
                perimeter++
            }
        }
    }
    return GardenRegion(plotType, plots, perimeter)
}