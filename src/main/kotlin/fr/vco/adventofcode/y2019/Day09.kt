package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader

fun main() {

    val test1 = execDay9(
        "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99".split(
            ","
        ).map { it.toLong() })
    val test2 =
        execDay9("1102,34915192,34915192,7,4,7,99,0".split(",").map { it.toLong() })
    val test3 =
        execDay9("104,1125899906842624,99".split(",").map { it.toLong() })
    println("Test 1 : $test1")
    println("Test 2 : $test2")
    println("Test 3 : $test3")

    val input = getInputReader("/2019/inputDay09.txt")
    val intCode = input.readText().trim().split(",").map { it.toLong() }
    println("Part 1 : ${execDay9(intCode, 1)}")
    println("Part 2 : ${execDay9(intCode, 2)}")

}


fun execDay9(intCode : List<Long>, input : Long? = null) : List<Long> {
    val stream = OpCodeStream()
    val opCode = OpCode(intCode, stream)

    input?.let{stream.input.add(it)}
    while(!opCode.isEnded()) {
        opCode.exec()
    }
    return stream.output
}

