package fr.vco.adventofcode

fun main() {

    test ("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99".split(",").map { it.toLong() })
    test ("1102,34915192,34915192,7,4,7,99,0".split(",").map { it.toLong() })
    test ("104,1125899906842624,99".split(",").map { it.toLong() })

    val input = getInputReader("/inputAoC9.txt")
    val intCode = input.readText().trim().split(",").map { it.toLong() }

    test (intCode)



}


fun test(intCode : List<Long>){

    val stream = OpCodeStream()
    val opCode = OpCode(intCode, stream)


    stream.input.add(1)
    while(!opCode.isEnded()) {
        opCode.exec()
    }
    println(stream.output)
}