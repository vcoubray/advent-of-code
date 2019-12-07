package fr.vco.adventofcode

import kotlin.math.max

fun main() {
    val str = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0".split(",").map{it.toInt()}
    val opCodeTest = OpCode(str)

    println("Part 1 Test : ${getMaxSignal(opCodeTest)}")


    val input = getInputReader("/inputAoC7.txt")
    val opCodeStr = input.readText().trim().split(",").map { it.toInt() }
    val opCode = OpCode(opCodeStr)

    println("Part 1 Test : ${getMaxSignal(opCode)}")

}

fun getMaxSignal(opCode: OpCode): Int{
    var maxSignal = 0
    repeat (5 ) {a ->
        repeat (5 ) {b ->
            repeat (5 ) {c ->
                repeat (5 ) {d ->
                    repeat (5 ) {e ->
                        val settings = setOf(a,b,c,d,e)
                        if (settings.size == 5) {
                            val result = execWithSettings(opCode, settings.toList())
                            maxSignal = max(result, maxSignal)
                        }
                    }
                }
            }
        }
    }
    return maxSignal
}

fun execWithSettings(opCode : OpCode, settings : List<Int>) : Int {

    var input = 0
    settings.forEach { setting ->
        val stream = OpCodeStream(listOf(setting,input))
        opCode.exec(stream)
        input = stream.getOutput().first!!
    }
    return input
}



