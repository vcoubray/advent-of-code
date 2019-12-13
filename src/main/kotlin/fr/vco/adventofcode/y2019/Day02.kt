package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader


fun main() {
    val input = getInputReader("1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,1,19,10,23,2,13,23,27,1,5,27,31,2,6,31,35,1,6,35,39,2,39,9,43,1,5,43,47,1,13,47,51,1,10,51,55,2,55,10,59,2,10,59,63,1,9,63,67,2,67,13,71,1,71,6,75,2,6,75,79,1,5,79,83,2,83,9,87,1,6,87,91,2,91,6,95,1,95,6,99,2,99,13,103,1,6,103,107,1,2,107,111,1,111,9,0,99,2,14,0,0\n")

    val opCode = input.readText().trim().split(",").map{it.toInt()}

    println("Part 1 : ${opCode.toMutableList().exec(12,2)}")

    val expected = 19690720
    repeat (99) { noun ->
        repeat ( 99 ) { verb ->
            if (expected == opCode.toMutableList().exec(noun,verb)){
                println("Part 2 : ${100 * noun + verb}")
                return
            }
        }
    }

}

fun MutableList<Int>.exec(val1 : Int, val2 : Int) : Int {
    this[1] = val1
    this[2] = val2
    var cursor = 0
    while (this[cursor] != 99) {
        val input1 = this[cursor+1]
        val input2 = this[cursor+2]
        val output = this[cursor+3]
        val indent = when (this[cursor] ) {
            1 -> {this[output] =  this[input1] +  this[input2];  4}
            2 -> {this[output] =  this[input1] *  this[input2];  4}
           else -> 1
        }
        cursor += indent
    }
    return this[0]
}

