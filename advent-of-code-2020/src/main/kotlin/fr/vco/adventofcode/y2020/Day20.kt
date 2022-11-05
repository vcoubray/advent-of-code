package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("/2020/inputDay20.txt")
    val lines = input.readLines()

    val tiles = lines.split { it.isBlank() }.filter { it.isNotEmpty() }.map {
        val (_, id) = "Tile (\\d+):".toRegex().find(it.first())?.groupValues ?: listOf("", "")
        Tile(id.toInt(), TileImage(it.drop(1)))
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

    val bigTileImage = bigTile.toTileImage()

    val monster = TileImage(
        listOf(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
        )
    )

    println("Part 2 : ${bigTileImage.findPattern(monster).count('#')}")
}


data class TileLocation(val x: Int, val y: Int, val tile: Tile)

enum class Side(val x: Int, val y: Int) {
    LEFT(-1, 0),
    RIGHT(1, 0),
    TOP(0, -1),
    BOT(0, 1)
}

data class Tile(val id: Int, val image: TileImage) {

    fun variations() = image.variations().map{Tile(id, it)}

    fun isAdjacent(tile: Tile, side: Side) = when (side) {
        Side.LEFT -> this.image.getBorder(Side.LEFT) == tile.image.getBorder(Side.RIGHT)
        Side.RIGHT -> this.image.getBorder(Side.RIGHT) == tile.image.getBorder(Side.LEFT)
        Side.TOP -> this.image.getBorder(Side.TOP) == tile.image.getBorder(Side.BOT)
        Side.BOT -> this.image.getBorder(Side.BOT) == tile.image.getBorder(Side.TOP)
    }

    override fun toString() = "Tile $id:\n$image"

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
                    sb.append(tiles[y][x]?.image?.image?.get(k) ?: "          ")
                }
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    fun toTileImage(): TileImage {

        val image = mutableListOf<String>()
        val images = tiles.map { it.map {tile-> tile?.image?.crop() } }

        for (y in 0 until height) {
            for (k in 0 until 8) {
                val sb = StringBuffer()
                for (x in 0 until width) {
                    sb.append(images[y][x]?.image?.get(k) ?: "          ")
                }
                image.add(sb.toString())
            }
        }
        return TileImage(image)
    }

}


data class TileImage(val image: List<String>) {
    val height  = image.size
    val width = image.first().length

    fun count(ch: Char ='#') = image.joinToString("").count{it == ch}

    fun flip() = TileImage(image.map { it.reversed() })
    fun rotate() = TileImage(
        List(image.size) { i ->
            List(image.size) { j -> image[image.size - j - 1][i] }.joinToString("")
        }
    )

    fun crop(cropSize: Int = 1) = TileImage(
        image.drop(cropSize)
            .dropLast(cropSize)
            .map {
                it.drop(cropSize).dropLast(cropSize)
            }
    )

    fun getBorder(side: Side) = when (side) {
        Side.LEFT -> this.image.map { it.first() }.joinToString("")
        Side.RIGHT -> this.image.map { it.last() }.joinToString("")
        Side.TOP -> this.image.first()
        Side.BOT -> this.image.last()
    }

    fun variations(): List<TileImage> {
        val variations = mutableListOf(
            this,
            this.flip()
        )
        var rotated = this
        repeat(3) {
            rotated = rotated.rotate()
            variations.add(rotated)
            variations.add(rotated.flip())
        }
        return variations
    }


    fun findPattern(pattern: TileImage) : TileImage{
        if(pattern.height > height || pattern.width > width) return this

        val patternPositions = mutableListOf<Pair<Int,Int>>()
        variations().forEach {
            for (y in 0 .. height - pattern.height+1) {
                for (x in 0 .. width - pattern.width+1) {
                    if (it.hasPattern(x, y, pattern)) patternPositions.add(Pair(x,y))
                }
            }
            if (patternPositions.isNotEmpty()) return it.drawPatterns(patternPositions, pattern)
        }
        return this
    }

    private fun hasPattern(x : Int, y : Int, pattern : TileImage): Boolean{
        pattern.image.forEachIndexed{ i, line ->
            line.forEachIndexed{j, ch->
                if(ch == '#' && image[y+i][x+j] != '#') return false
            }
        }
        return true
    }

    private fun drawPatterns(positions: List<Pair<Int,Int>>, pattern: TileImage, char : Char ='0') : TileImage {
        val result = image.map{it.toCharArray()}

        positions.forEach{(x,y)->
            pattern.image.forEachIndexed{ i, line ->
                line.forEachIndexed{j, ch->
                    if(ch == '#') result[y+i][x+j] = char
                }
            }
        }
        return TileImage(result.map{String(it)})
    }

    override fun toString() = image.joinToString("\n")

}