package fr.vco.aoc.y2022

fun main() {

    val input = readLines("Day22")

    val (map, instructions) = input.split { it.isBlank() }

    println("Part 1 : ${MonkeyMap(map).play(instructions.joinToString(""))}")

}

enum class Direction(val dir: MonkeyMap.Position) {
    UP(MonkeyMap.Position(0, -1)),
    DOWN(MonkeyMap.Position(0, 1)),
    RIGHT(MonkeyMap.Position(1, 0)),
    LEFT(MonkeyMap.Position(-1, 0))
}

class MonkeyMap(private val rawMap: List<String>) {
    private val height = rawMap.size
    private val width = rawMap.firstOrNull()?.length ?: 0
    private val tileMap = rawMap.mapIndexed { y, line ->
        line.padEnd(width,' ').mapIndexed { x, it -> Tile(Position(x, y), it == '#') }
    }.flatten()

    init {
        println("$width $height")
        println ("${tileMap.size}")
        tileMap.forEach { tile ->
            tile.neighbours = Direction.values().associateWith { getTile(getNextValidPos(tile.position, it)) }
        }
    }

    fun getRaw(pos: Position) = rawMap.getOrNull(pos.y)?.getOrNull(pos.x)?:' '
    fun isValid(pos: Position) = getRaw(pos) != ' '

    fun getTile(pos: Position) = tileMap[pos.y * width + pos.x]

    private fun getNextPos(pos: Position, direction: Direction): Position {
        val next = pos + direction.dir
        val nextX = next.x.mod(width)
        val nextY = next.y.mod(height)
        return Position(nextX, nextY)
    }

    private fun getNextValidPos(pos: Position, direction: Direction): Position {
        var next = getNextPos(pos, direction)
        while (getRaw(next) == ' ') {
            next = getNextPos(next, direction)
        }
        return next
    }


    fun getFirstState() = State(tileMap.first { isValid(it.position) })


    fun String.toInstructionList(): List<String> {
        return buildList {
            var instruction = """(\d+|[RL])""".toRegex().find(this@toInstructionList)
            while (instruction != null) {
                this.add(instruction.value)
                instruction = instruction.next()
            }
        }
    }

    fun play(instructions: String) : Int {
        val state = getFirstState()
        instructions.toInstructionList().forEach {
            when (it) {
                "R" -> state.rotate(1)
                "L" -> state.rotate(-1)
                else -> state.move(it.toInt())
            }
        }
        return state.getPassword()
    }

    data class Position(val x: Int, val y: Int) {
        operator fun plus(position: Position) = Position(x + position.x, y + position.y)
    }

    class Tile(val position: Position, val isWall: Boolean) {
        lateinit var neighbours: Map<Direction, Tile>
    }

    class State(
        var tile: Tile,
        var directionIndex: Int = 0,
    ) {
        private val directions = listOf(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP)
        fun move(step: Int) {
            repeat(step) {
                val next = tile.neighbours[directions[directionIndex]]!!
                if (!next.isWall) {
                    tile = next
                }
            }
        }

        fun rotate(rotate: Int) {
            directionIndex = (directionIndex + rotate).mod(directions.size)
        }

        fun getPassword() = 1000 * (tile.position.y+1) + 4 * (tile.position.x+1) + directionIndex
    }

}