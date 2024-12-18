package fr.vco.aoc.y2024

import kotlin.math.pow

fun main() {
    val input = readLines("Day17")
    val (registers, intCodeString) = input.split { it.isBlank() }

    val intCode = intCodeString.first().substringAfter(" ").split(",").map { it.toInt() }
    val (a, b, c) = registers.toRegisterList()

    val opCode = OpCode(intCode)
    println("Part 1: ${opCode.execute(a, b, c).joinToString(",")}")
    println("Part 2: ${opCode.findRegisterA(b, c)}")

}

class OpCode(
    private val intCode: List<Int>
) {
    private var registerA = 0L
    private var registerB = 0L
    private var registerC = 0L
    private var cursor = 0
    private val out = mutableListOf<Int>()

    private fun getComboOperand(value: Int): Long {
        return when (value) {
            in 0..3 -> value.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw Exception("WTF ??")
        }
    }

    private val operations = listOf<(Int) -> Unit>(
        { value -> registerA = (registerA / 2.0.pow(getComboOperand(value).toDouble())).toLong() },
        { value -> registerB = registerB.xor(value.toLong()) },
        { value -> registerB = (getComboOperand(value) % 8) },
        { value -> if (registerA != 0L) cursor = value - 2 },
        { _ -> registerB = registerB.xor(registerC) },
        { value -> out.add((getComboOperand(value) % 8).toInt()) },
        { value -> registerB = (registerA / 2.0.pow(getComboOperand(value).toDouble())).toLong() },
        { value -> registerC = (registerA / 2.0.pow(getComboOperand(value).toDouble())).toLong() }
    )

    fun execute(a: Long, b: Long, c: Long): List<Int> {
        registerA = a
        registerB = b
        registerC = c
        cursor = 0
        out.clear()
        while (cursor < intCode.size) {
            operations[intCode[cursor]](intCode[cursor + 1])
            cursor += 2
        }
        return out
    }

    fun findRegisterA(b: Long, c: Long): Long {
        var a = 0L
        intCode.indices.forEach { i ->
            var j = 0L
            while (intCode.takeLast(i + 1) != this.execute(a * 8 + j, b, c)) {
                j++
            }
            a = a * 8 + j
        }
        return a
    }
}

fun List<String>.toRegisterList(): List<Long> {
    return this.map { """Register .: (\d+)""".toRegex().find(it)!!.destructured.toList().first().toLong() }
}