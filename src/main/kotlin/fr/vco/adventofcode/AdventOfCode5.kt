package fr.vco.adventofcode

fun main() {

    val input = getInputReader("/inputAoC5.txt")
    val opCodeStr = input.readText().trim().split(",").map { it.toInt() }

    val stream = OpCodeStream()
    val opCode = OpCode(opCodeStr,stream)
    stream.input.add(1)
    val resultPart1 = StringBuilder()
    while(!opCode.isEnded()) {
        opCode.exec()
        resultPart1.append("${stream.output.poll()?:""} ")
    }
    println("Part 1 : $resultPart1")



    opCode.restart()
    stream.input.add(5)
    opCode.exec()
    println("Part 2 : ${stream.output.poll()}")

}



