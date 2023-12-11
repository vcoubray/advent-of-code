package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day11")
    val galaxies = input.toGalaxies()

    println("Part 1: ${galaxies.expand().mapToMinDistance()}")
    println("Part 2: ${galaxies.expand(1000000).mapToMinDistance()}")
}

fun List<String>.toGalaxies() = flatMapIndexed { y, line ->
    line.mapIndexed { x, c -> Position(x, y).takeIf { c == '#' } }
        .filterNotNull()
}

fun List<Position>.expand(expansion: Int = 1): List<Position> {
    val expandX = expandAxis(expansion) { it.x }
    val expandY = expandAxis(expansion) { it.y }
    return this.map { Position(expandX[it.x], expandY[it.y]) }
}

fun List<Position>.expandAxis(expansion: Int, coordinate: (Position) -> Int): List<Int> {
    val max = maxOf(coordinate)
    var shift = 0
    return List(max + 1) { i ->
        shift += if (none { coordinate(it) == i }) expansion else 1
        shift
    }
}

fun List<Position>.mapToMinDistance() = sumOf { pos ->
    this.filterNot { pos == it }.sumOf { pos.distanceManhattan(it).toLong() }
} / 2
