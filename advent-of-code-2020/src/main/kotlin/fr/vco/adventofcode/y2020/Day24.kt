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

    var hexagons = tiles.toMap()
    repeat(100){
        hexagons = hexagons.addNeighbors().switch()
    }
    println("Part 2 : ${hexagons.count { !it.value }}")
}

const val WHITE = true
const val BLACK = false

fun Map<Hexagon, Boolean>.switch(): Map<Hexagon, Boolean> {
    val hexagons = mutableMapOf<Hexagon, Boolean>()
    this.forEach { (hexagon, isWhite) ->
        val countBlack = hexagon. neighbors().map { this[it] ?: WHITE }.count { it == BLACK }
        val color = when {
            !isWhite && countBlack !in 1..2 -> WHITE
            isWhite && countBlack == 2 -> BLACK
            else -> isWhite
        }
        hexagons[hexagon] = color
    }
    return hexagons
}

fun Map<Hexagon, Boolean>.addNeighbors(): Map<Hexagon, Boolean> {
    val hexagons = this.toMutableMap()
    this.forEach { (hexagon, _) ->
        hexagon.neighbors().forEach { neighbor ->
            hexagons.computeIfAbsent(neighbor) { WHITE }
        }
    }
    return hexagons
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

data class Hexagon(val x: Int, val y: Int) {
    fun neighbors() = direction.values.map { this + it }
    operator fun plus(dir: Hexagon) = Hexagon(this.x + dir.x, this.y + dir.y)
}