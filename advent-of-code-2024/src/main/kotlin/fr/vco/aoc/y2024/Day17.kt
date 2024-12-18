package fr.vco.aoc.y2024

import kotlin.math.pow

fun main() {
    val input = readLines("Day17")
    val (registers, intCodeString) = input.split { it.isBlank() }

    val intCode = intCodeString.first().substringAfter(" ").split(",").map { it.toInt() }
    val (a, b, c) = registers.toRegisterList()

    println("Part 1: ${OpCode(a, b, c).execute(intCode).joinToString(",")}")
}


class OpCode(
    private var registerA: Int,
    private var registerB: Int,
    private var registerC: Int,
) {

    var cursor = 0
    val out = mutableListOf<Int>()

    private fun getComboOperand(value: Int): Int {
        return when (value) {
            in 0..3 -> value
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw Exception("WTF ??")
        }
    }

    private val operations = listOf<(Int) -> Unit>(
        { value -> registerA = (registerA / 2.0.pow(getComboOperand(value))).toInt() },
        { value -> registerB = registerB.xor(value) },
        { value -> registerB = (getComboOperand(value) % 8) },
        { value -> if (registerA != 0) cursor = value - 2 },
        { _ -> registerB = registerB.xor(registerC) },
        { value -> out.add(getComboOperand(value) % 8) },
        { value -> registerB = (registerA / 2.0.pow(getComboOperand(value))).toInt() },
        { value -> registerC = (registerA / 2.0.pow(getComboOperand(value))).toInt() }
    )

    fun execute(opCode: List<Int>): List<Int> {
        cursor = 0

        while (cursor < opCode.size) {
            operations[opCode[cursor]](opCode[cursor + 1])
            cursor += 2
        }
        return out
    }
}

fun List<String>.toRegisterList(): List<Int> {
    return this.map { """Register .: (\d+)""".toRegex().find(it)!!.destructured.toList().first().toInt() }
}