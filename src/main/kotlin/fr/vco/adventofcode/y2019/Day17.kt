package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader

fun main() {


    val input = getInputReader("/2019/inputDay17.txt")

    val intCode = input.readText().split(",").map{it.toLong()}

    val opCode = OpCode(intCode, OpCodeStream())

    while(!opCode.isEnded()){
        opCode.exec()
    }


    val maze = opCode.stream.output.map{it.toChar()}.joinToString ("").lines()
   maze.forEach { println(it) }

    var sum = 0

    repeat(maze.size) {y->
        repeat(maze[y].length) { x ->
            if (x <= 0 || x >= maze[y].length -1 ) return@repeat
            if (y <= 0 || y >= maze.size-1) return@repeat
            if (maze[y][x] != '#') return@repeat

            if(maze[y][x-1] == '#' &&
                maze[y][x+1] == '#' &&
                maze[y-1][x] == '#' &&
                maze[y+1][x] == '#' ){
                sum += x*y
            }


        }
    }


    println("part 1 : $sum")


}
