package fr.vco.adventofcode.y2019

import java.lang.StringBuilder

const val EMPTY = 0
const val WALL = 1
const val BLOCK = 2
const val PADDLE = 3
const val BALL = 4

data class Tile(val x: Int, val y: Int, val type: Int)

fun main() {
    val input = getInputReader("Day13")
    val intCode = input.readText().trim().split(",").map { it.toLong() }.toMutableList()


    val game = Game(intCode)

    game.start()
    println("Part 1 : ${game.grid.flatten().filter { it.type == BLOCK }.size}")

    game.start(2)
    println("Part 2 : ${game.score}")


}

class Game(val originalIntCode: List<Long>) {

    val grid = mutableListOf<MutableList<Tile>>()
    lateinit var opCode: OpCode
    var score = 0
    lateinit var ball : Tile
    lateinit var paddle : Tile

    fun start(coin: Int? = null,print: Boolean = false) {
        score = 0
        grid.clear()
        val intCode = originalIntCode.toMutableList()
        coin?.let { intCode[0] = it.toLong() }
        opCode = OpCode(intCode, OpCodeStream())
        initGrid()
        if (print)printScreen()

        while (!opCode.isEnded()) {
            play()
            if (print)printScreen()
        }
    }

    fun play() {
        opCode.stream.input.add(movePaddle(ball.x).toLong())
        opCode.exec()
        updateGrid()


    }

    fun initGrid() {
        opCode.exec()
        val tiles = getTilesFromOutput()
        val map = tiles.groupBy { it.y }
        map.toSortedMap().values.forEach {
            grid.add(it.sortedBy { panel -> panel.x }.toMutableList())
        }
    }

    private fun getTilesFromOutput(): List<Tile> {
        val tiles = mutableListOf<Tile>()
        val output = opCode.stream.output
        while (output.isNotEmpty()) {
            val x = output.poll()?.toInt()
            val y = output.poll()?.toInt()
            val type = output.poll()?.toInt()
            if (x != null && y != null && type != null) {
                val tile = Tile(x, y, type)
                if (type == BALL) ball = tile
                if (type == PADDLE) paddle = tile
                if (x < 0) score = type
                else tiles.add(tile)
            }
        }
        return tiles
    }


    fun updateGrid() {
        val tiles = getTilesFromOutput()
        tiles.forEach{
            grid[it.y][it.x] = it
        }
    }


    fun printScreen() {
        val sb = StringBuilder()
        repeat(grid.size) { y ->
            repeat(grid[y].size) { x ->
                sb.append(drawTile(grid[y][x]))
            }
            sb.append("\n")
        }
        println(sb)

    }


    private fun drawTile(tile: Tile) =
        when (tile.type) {
            WALL -> "#"
            PADDLE -> "="
            BLOCK -> "X"
            BALL -> "O"
            else -> " "

        }

    private fun movePaddle (target: Int) =
        when {
            paddle.x < target -> 1
            paddle.x > target -> -1
            else -> 0
        }

}



