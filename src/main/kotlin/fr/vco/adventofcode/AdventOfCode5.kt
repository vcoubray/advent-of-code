package fr.vco.adventofcode

fun main() {

    val input = getInputReader("/inputAoC5.txt")
    val opCodeStr = input.readText().trim().split(",").map { it.toInt() }


    val opCode = OpCode(opCodeStr)
    val streamPart1 = OpCodeStream(listOf(1))
    opCode.exec(streamPart1)
    println("Part 1 : ${streamPart1.getOutput()}")

    val streamPart2 = OpCodeStream(listOf(5))
    opCode.exec(streamPart2)
    println("Part 2 : ${streamPart2.getOutput()}")

}



