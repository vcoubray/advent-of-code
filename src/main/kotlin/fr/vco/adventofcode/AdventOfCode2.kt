package fr.vco.adventofcode


fun main() {
    val input = getInputReader("/inputAoC2.txt")

    var opCode = input.readText().trim().split(",").map{it.toInt()}



    println("Part 1 : ${opCode.toMutableList().exec(12,2)}")

    println(opCode)



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

