package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import kotlin.math.max

fun main() {

    val input = getInputReader("3,8,1001,8,10,8,105,1,0,0,21,34,47,72,93,110,191,272,353,434,99999,3,9,102,3,9,9,1001,9,3,9,4,9,99,3,9,102,4,9,9,1001,9,4,9,4,9,99,3,9,101,3,9,9,1002,9,3,9,1001,9,2,9,1002,9,2,9,101,4,9,9,4,9,99,3,9,1002,9,3,9,101,5,9,9,102,4,9,9,1001,9,4,9,4,9,99,3,9,101,3,9,9,102,4,9,9,1001,9,3,9,4,9,99,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,99\n")
    val intCode = input.readText().trim().split(",").map { it.toLong() }

    val settingsPart1 = listOf(0, 1, 2, 3, 4)
    val settingsPart2 = listOf(5, 6, 7, 8, 9)

    println("Part 1 : ${getMaxSignal(intCode, settingsPart1, false)}")
    println("Part 2 : ${getMaxSignal(intCode, settingsPart2, true)}")
}

fun getMaxSignal(intCode: List<Long>, availableSettings: List<Int>, loop: Boolean): Long {
    val amplifiers = List(5) {
        OpCode(
            intCode,
            OpCodeStream()
        )
    }
    val allSettings = getAllSettings(availableSettings)
    var maxSignal = 0L
    allSettings.forEach { settings ->
        amplifiers.forEachIndexed { i, amp ->
            amp.restart()
            amp.stream.input.add(settings[i].toLong())
        }
        val result = if (loop) execAmplifiersLoop(amplifiers, 0)
        else execAmplifiers(amplifiers, 0)
        maxSignal = max(result, maxSignal)
    }
    return maxSignal
}


fun execAmplifiers(amplifiers: List<OpCode>, input: Long): Long {
    var signal = input
    amplifiers.forEachIndexed { i, amp ->
        signal = execAmplifier(amp, signal) ?: signal
    }
    return signal
}

fun execAmplifiersLoop(amplifiers: List<OpCode>, input: Long): Long {
    var signal = input
    while (!amplifiers[0].isEnded()) {
        signal = execAmplifiers(amplifiers, signal)
    }
    return signal
}

fun execAmplifier(opCode: OpCode, input: Long): Long? {
    opCode.stream.input.add(input)
    opCode.exec()
    return opCode.stream.output.poll()
}

fun getAllSettings(settings: List<Int>): List<List<Int>> {
    val allSettings = mutableListOf<List<Int>>()
    permute(settings, listOf(), allSettings)
    return allSettings
}

fun permute(settings: List<Int>, permutation: List<Int>, allSettings: MutableList<List<Int>>) {

    if (settings.isEmpty()) {
        allSettings.add(permutation)
        return
    }
    for (i in settings.indices) {
        val permuteInt = settings[i]
        val ros = settings.take(i) + settings.takeLast(settings.size - (i + 1))
        permute(ros, permutation + permuteInt, allSettings)
    }
}