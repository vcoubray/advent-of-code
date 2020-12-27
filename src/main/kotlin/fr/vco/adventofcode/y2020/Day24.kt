package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay24.txt")
    val lines = input.readLines()

    val tiles = mutableMapOf<Hexagon, Boolean>()
    lines.map { "(se|sw|nw|ne|w|e)".toRegex().findAll(it).map { dir -> dir.value }.toList() }
        .map { it.fold(CENTER) { acc, s -> acc + direction[s]!! } }
        .forEach { tiles[it] = !tiles.computeIfAbsent(it) { true } }

    println("Part 1 : ${tiles.count { !it.value }}")

}

data class Hexagon(val x: Int, val y: Int) {
    operator fun plus(dir: Hexagon) = Hexagon(this.x + dir.x, this.y + dir.y)
}

val CENTER = Hexagon(0, 0)
val E = Hexagon(1, 0)
val NE = Hexagon(0, 1)
val SE = Hexagon(1, -1)
val W = Hexagon(-1, 0)
val NW = Hexagon(-1, 1)
val SW = Hexagon(0, -1)

val direction = mapOf(
    "se" to SE,
    "sw" to SW,
    "e" to E,
    "ne" to NE,
    "nw" to NW,
    "w" to W
)

