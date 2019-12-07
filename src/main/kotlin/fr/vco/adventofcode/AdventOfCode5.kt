package fr.vco.adventofcode

fun main() {

    val input = getInputReader("/inputAoC5.txt")
    val opCodeStr = input.readText().trim().split(",").map { it.toInt() }

    val opCodePart1 = OpCode(opCodeStr.toMutableList())
    val inputPart1 = "1"
    println("Part 1 : ${opCodePart1.exec(inputPart1)}")

    val opCodePar2 = OpCode(opCodeStr.toMutableList())
    val inputPart2 = "5"
    println("Part 2 : ${opCodePar2.exec(inputPart2)}")

}


