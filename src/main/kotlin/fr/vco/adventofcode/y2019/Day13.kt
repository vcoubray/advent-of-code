package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import java.lang.StringBuilder

const val EMPTY = 0
const val WALL = 1
const val BLOCK = 2
const val PADDLE = 3
const val BALL = 4

data class Tile(val x : Int, val y :Int, val type : Int)

fun main () {
    val input = getInputReader("/2019/inputDay13.txt")
    val intCode = input.readText().trim().split(",").map{it.toLong()}.toMutableList()

    val stream = OpCodeStream()
    val opCode = OpCode(intCode,stream)
    opCode.exec()


   val tiles = constructGrid(stream)

    println("Part 1 : ${tiles.filter{it.type == BLOCK}.size}")
    printScreen(tiles)
    intCode[0]=2
    val opCode2 = OpCode(intCode,stream)

    while(!opCode2.isEnded()) {
        opCode2.exec()
        stream.input.add(1)
        //println(constructGrid(stream))
        printScreen(constructGrid(stream))
    }

}

fun constructGrid(stream : OpCodeStream) : List<Tile>{

    val tiles = mutableListOf<Tile>()
    while(stream.output.isNotEmpty()) {
        val x = stream.output.poll()?.toInt()
        val y = stream.output.poll()?.toInt()
        val type = stream.output.poll()?.toInt()
        if (x != null && y != null && type != null)
            tiles.add(Tile(x, y, type))
    }
    return tiles
}


fun printScreen(tiles : List<Tile>){

    val sb = StringBuilder()
    val map = tiles.groupBy{it.y}
    map.toSortedMap().values.forEach{
        val line = it.sortedBy{panel -> panel.x}.joinToString(""){t ->
            when(t.type) {
                WALL -> "#"
                PADDLE -> "_"
                BLOCK -> "x"
                BALL -> "O"
                else -> " "
            }
        }
        sb.append(line).append("\n")
    }
    println(sb)
}

