package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import fr.vco.adventofcode.split


fun main() {
    val input = getInputReader("/2020/inputDay20.txt")
    val lines = input.readLines()

    val tiles = lines.split { it.isBlank() }.filter { it.isNotEmpty() }.map {
        val (_, id) = "Tile (\\d+):".toRegex().find(it.first())?.groupValues ?: listOf("", "")
        Tile(id.toInt(), it.drop(1))
    }

    val bigImage = mutableListOf(TileLocation(0, 0, tiles.first()))

    val tilesToPlace = tiles.drop(1).toMutableList()

    while (tilesToPlace.isNotEmpty()) {
        val tilesToAdd = mutableSetOf<TileLocation>()
        tilesToPlace.forEach { tile ->
            bigImage.forEach { location ->
                val variations = tile.variations()
                Side.values().forEach { side ->
                    variations.firstOrNull { location.tile.isAdjacent(it, side) }?.let {
                        tilesToAdd.add(TileLocation(location.x + side.x, location.y + side.y, it))
                    }
                }
            }
        }
        bigImage.addAll(tilesToAdd)
        tilesToPlace.removeAll(tilesToAdd.map { it.tile })
        tilesToAdd.clear()
    }

    val bigTile = BigTile(bigImage)

    println("Part 1 : ${bigTile.getCorners().fold(1L) { acc, it -> acc * (it?.id ?: 1) }}")


    // println(bigImage.toPrint())

}


data class TileLocation(val x: Int, val y: Int, val tile: Tile)

enum class Side(val x: Int, val y: Int) {
    LEFT(-1, 0),
    RIGHT(1, 0),
    TOP(0, -1),
    BOT(0, 1)
}

data class Tile(val id: Int, val image: List<String>) {

    private fun flip() = Tile(id, image.map { it.reversed() })
    private fun rotate() = Tile(
        id,
        List(image.size) { i -> List(image.size) { j -> image[image.size - j - 1][i] }.joinToString("") }
    )

    fun variations(): List<Tile> {
        val variations = mutableListOf(this, this.flip())
        var rotated = this
        repeat(3) {
            rotated = rotated.rotate()
            variations.add(rotated)
            variations.add(rotated.flip())
        }
        return variations
    }

    fun isAdjacent(tile: Tile, side: Side) = when (side) {
        Side.LEFT -> this.getBorder(Side.LEFT) == tile.getBorder(Side.RIGHT)
        Side.RIGHT -> this.getBorder(Side.RIGHT) == tile.getBorder(Side.LEFT)
        Side.TOP -> this.getBorder(Side.TOP) == tile.getBorder(Side.BOT)
        Side.BOT -> this.getBorder(Side.BOT) == tile.getBorder(Side.TOP)
    }

    private fun getBorder(side: Side) = when (side) {
        Side.LEFT -> this.image.map { it.first() }.joinToString("")
        Side.RIGHT -> this.image.map { it.last() }.joinToString("")
        Side.TOP -> this.image.first()
        Side.BOT -> this.image.last()
    }


    override fun toString() = "Tile $id:\n" + image.joinToString("\n")

    override fun equals(other: Any?) =
        if (other is Tile) this.id == other.id
        else false

    override fun hashCode() = this.id
}


class BigTile(locations: List<TileLocation>) {

    val xStart = locations.minByOrNull { it.x }?.x ?: 0
    val xEnd = locations.maxByOrNull { it.x }?.x ?: 0
    val yStart = locations.minByOrNull { it.y }?.y ?: 0
    val yEnd = locations.maxByOrNull { it.y }?.y ?: 0
    val height = yEnd - yStart + 1
    val width = xEnd - xStart + 1

    val tiles: List<List<Tile?>> =
        List(height) { y -> List(width) { x -> locations.firstOrNull { it.x == x + xStart && it.y == y + yStart }?.tile } }


    fun getCorners() = listOf(
        tiles.first().first(),
        tiles.first().last(),
        tiles.last().first(),
        tiles.last().last(),
    )

    override fun toString(): String {
        val sb = StringBuffer()
        for (y in 0 until height) {
            for (k in 0 until 10) {
                for (x in 0 until width) {
                    sb.append(tiles[y][x]?.image?.get(k) ?: "          ")
                }
                sb.append("\n")
            }
        }
        return sb.toString()
    }

}
