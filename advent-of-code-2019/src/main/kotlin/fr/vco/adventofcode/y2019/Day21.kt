package fr.vco.adventofcode.y2019

fun main() {
    val input = readLines("Day21").first()

    val springDroid = SpringDroid(input.toOpCode())

    val walkInstructions = listOf(
        "NOT A J",
        "NOT B T",
        "OR T J",
        "NOT C T",
        "OR T J",
        "AND D J",
    )

    val runInstructions = listOf(
        "NOT A J",
        "NOT B T",
        "OR T J",
        "NOT C T",
        "OR T J",
        "NOT E T",
        "NOT T T",
        "OR H T",
        "AND T J",
        "AND D J",
    )

    println("Part 1 : ${springDroid.walk(walkInstructions)}")
    println("Part 2 : ${springDroid.run(runInstructions)}")

}


class SpringDroid(val opcode: OpCode) {

    companion object {
        private val ASCII_RANGE = 0..127
    }

    fun walk(instructions: List<String>) = execute(instructions + "WALK\n")
    fun run(instructions: List<String>) = execute(instructions + "RUN\n")

    private fun execute(instruction: List<String>): String {
        opcode.restart()
        opcode.stream.input.addAll(
            instruction.joinToString("\n").map { it.code.toLong() }
        )
        opcode.exec()
        return opcode.stream.output.joinToString("") { if (it in ASCII_RANGE) "${it.toInt().toChar()}" else "$it" }
    }

}