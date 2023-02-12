package fr.vco.aoc.y2022

fun main() {

    val input = readLines("Day22")
    val (map, instructions) = input.split { it.isBlank() }

    println("Part 1 : ${Mapper(50, map).toFlatMap().execute(instructions.joinToString(""))}")
    println("Part 2 : ${Mapper(50, map).toCubicMap().execute(instructions.joinToString(""))}")
}

enum class Direction(val dir: MonkeyMap.Position) {
    UP(MonkeyMap.Position(0, -1)),
    DOWN(MonkeyMap.Position(0, 1)),
    RIGHT(MonkeyMap.Position(1, 0)),
    LEFT(MonkeyMap.Position(-1, 0))
}

class Mapper(private val panelSize: Int, private val rawMap: List<String>) {
    private val height = rawMap.size
    private val width = rawMap.firstOrNull()?.length ?: 0

    private val tiles = rawMap.mapIndexed { y, line ->
        line.padEnd(width, ' ').mapIndexed { x, it -> MonkeyMap.Tile(MonkeyMap.Position(x, y), it) }
    }.flatten()


    fun toFlatMap() = toMonkeyMap(::wrapFlat)
    fun toCubicMap() = toMonkeyMap(::wrapCubic)

    private fun getRaw(pos: MonkeyMap.Position) = rawMap.getOrNull(pos.y)?.getOrNull(pos.x) ?: ' '
    private fun getTile(pos: MonkeyMap.Position) = tiles[pos.y * width + pos.x]

    private fun toMonkeyMap(wrapper: (MonkeyMap.Position, Direction) -> Pair<MonkeyMap.Position, Int>): MonkeyMap {
        return MonkeyMap(tiles.filter { it.isValid }.map { tile ->
            tile.apply {
                neighbours = Direction.values().associateWith { getNeighbor(tile.position, it, wrapper) }
            }
        })
    }

    private fun getNeighbor(
        pos: MonkeyMap.Position,
        direction: Direction,
        wrapper: (MonkeyMap.Position, Direction) -> Pair<MonkeyMap.Position, Int>,
    ): Pair<MonkeyMap.Tile, Int> {
        val (neighbor, rotation) = (pos + direction.dir to 0).takeIf { getRaw(it.first) != ' ' }
            ?: run { wrapper(pos, direction) }
        return getTile(neighbor) to rotation
    }


    private fun wrapFlat(pos: MonkeyMap.Position, direction: Direction): Pair<MonkeyMap.Position, Int> {
        return when (direction) {
            Direction.UP -> (0 until height).map { MonkeyMap.Position(pos.x, it) }.last { getRaw(it) != ' ' } to 0
            Direction.DOWN -> (0 until height).map { MonkeyMap.Position(pos.x, it) }.first { getRaw(it) != ' ' } to 0
            Direction.LEFT -> (0 until width).map { MonkeyMap.Position(it, pos.y) }.last { getRaw(it) != ' ' } to 0
            Direction.RIGHT -> (0 until width).map { MonkeyMap.Position(it, pos.y) }.first { getRaw(it) != ' ' } to 0
        }
    }

    //   1 2
    //   4
    // 6 7
    // 9
    private fun wrapCubic(pos: MonkeyMap.Position, direction: Direction): Pair<MonkeyMap.Position, Int> {
        val panelX = pos.x % panelSize
        val panelY = pos.y % panelSize
        val cubePos = pos.y / panelSize * 3 + pos.x / panelSize
        return when {
            cubePos == 1 && direction == Direction.UP -> getPanelPos(9, 0, panelX) to 1
            cubePos == 1 && direction == Direction.LEFT -> getPanelPos(6, 0, panelSize - panelY - 1) to 2
            cubePos == 2 && direction == Direction.UP -> getPanelPos(9, panelX, panelSize - 1) to 0
            cubePos == 2 && direction == Direction.RIGHT -> getPanelPos(7, panelSize - 1, panelSize - panelY - 1) to 2
            cubePos == 2 && direction == Direction.DOWN -> getPanelPos(4, panelSize - 1, panelX) to 1
            cubePos == 4 && direction == Direction.LEFT -> getPanelPos(6, panelY, 0) to -1
            cubePos == 4 && direction == Direction.RIGHT -> getPanelPos(2, panelY, panelSize - 1) to -1
            cubePos == 6 && direction == Direction.UP -> getPanelPos(4, 0, panelX) to 1
            cubePos == 6 && direction == Direction.LEFT -> getPanelPos(1, 0, panelSize - panelY - 1) to 2
            cubePos == 7 && direction == Direction.RIGHT -> getPanelPos(2, panelSize - 1, panelSize - panelY - 1) to 2
            cubePos == 7 && direction == Direction.DOWN -> getPanelPos(9, panelSize - 1, panelX) to 1
            cubePos == 9 && direction == Direction.RIGHT -> getPanelPos(7, panelY, panelSize - 1) to -1
            cubePos == 9 && direction == Direction.DOWN -> getPanelPos(2, panelX, 0) to 0
            cubePos == 9 && direction == Direction.LEFT -> getPanelPos(1, panelY, 0) to -1
            else -> throw Exception("Should not happen")
        }
    }

    private fun getPanelPos(panelIndex: Int, x: Int, y: Int) =
        MonkeyMap.Position(panelIndex % 3 * panelSize + x, panelIndex / 3 * panelSize + y)

}

class MonkeyMap(tiles: List<Tile>) {
    private var tile = tiles.first { it.isValid }
    private var directionIndex: Int = 0
    private val directions = listOf(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP)

    fun execute(instructions: String): Int {
        instructions.toInstructionList().forEach {
            when (it) {
                "R" -> rotate(1)
                "L" -> rotate(-1)
                else -> move(it.toInt())
            }
        }
        return getPassword()
    }

    private fun String.toInstructionList(): List<String> {
        return buildList {
            var instruction = """(\d+|[RL])""".toRegex().find(this@toInstructionList)
            while (instruction != null) {
                this.add(instruction.value)
                instruction = instruction.next()
            }
        }
    }

    private fun move(step: Int) {
        repeat(step) {
            val (next, rotate) = tile.neighbours[directions[directionIndex]]!!
            if (!next.isWall) {
                tile = next
                rotate(rotate)
            }
        }
    }

    private fun rotate(rotate: Int) {
        directionIndex = (directionIndex + rotate).mod(directions.size)
    }

    private fun getPassword() = 1000 * (tile.position.y + 1) + 4 * (tile.position.x + 1) + directionIndex

    data class Position(val x: Int, val y: Int) {
        operator fun plus(position: Position) = Position(x + position.x, y + position.y)
    }

    class Tile(val position: Position, type: Char) {
        val isWall = type == '#'
        val isValid = type != ' '
        lateinit var neighbours: Map<Direction, Pair<Tile, Int>>
    }

}