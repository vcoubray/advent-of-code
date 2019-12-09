package fr.vco.adventofcode

import java.lang.IllegalArgumentException
import java.util.*


class OpCode(private val opCodeOrigin: List<Int>, val stream: OpCodeStream) {
    private var cursor = 0
    private var opCode = opCodeOrigin.toMutableList()
    private var paused = false
    private var relativeBase = 0

    fun restart() {
        cursor = 0
        opCode = opCodeOrigin.toMutableList()
        stream.clear()
    }

    fun isEnded() = cursor >= opCode.size

    fun exec() {
        paused = false
        while (!paused && !isEnded()) {
            val ins = getInstruction()
            execInstuction(ins)
        }
    }


    private fun execInstuction(ins: Instruction) {
        when (ins.ins) {
            99 -> cursor = opCode.size
            1 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = opCode[param1] + opCode[param2]
            }
            2 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = opCode[param1] * opCode[param2]
            }
            3 -> {
                val (param) = getParamIndexes(1, ins.modes)
                opCode[param] = stream.read()
            }
            4 -> {
                val (param) = getParamIndexes(1, ins.modes)
                stream.write(opCode[param])
                paused = true
            }
            5 -> {
                val (param1, param2) = getParamIndexes(2, ins.modes)
                if (opCode[param1] != 0) cursor = opCode[param2]
            }
            6 -> {
                val (param1, param2) = getParamIndexes(2, ins.modes)
                if (opCode[param1] == 0) cursor = opCode[param2]
            }
            7 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = if (opCode[param1] < opCode[param2]) 1 else 0
            }
            8 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = if (opCode[param1] == opCode[param2]) 1 else 0
            }
            9 -> {
                val (param1) = getParamIndexes(1, ins.modes)
                relativeBase += opCode[param1]
            }
            else -> throw IllegalStateException("Unknown instruction : ${opCode[cursor]}  ${ins}")
        }
    }

    private fun getInstruction(): Instruction {
        val insStr = opCode[cursor++].toString().reversed().map { it.toString().toInt() }
        val ins = Instruction(
            insStr[0] + (insStr.elementAtOrElse(1) { 0 } * 10),
            listOf(
                insStr.elementAtOrElse(2) { 0 },
                insStr.elementAtOrElse(3) { 0 },
                insStr.elementAtOrElse(4) { 0 }
            )
        )
        return ins
    }

    private fun getParamIndexes(paramCount: Int, paramsMode: List<Int>) =
        List(paramCount) { getParamIndex(paramsMode[it]) }


    private fun getParamIndex(paramMode: Int) =
        when (paramMode) {
            0 -> opCode[cursor++]
            1 -> cursor++
            2 -> opCode[cursor++] + relativeBase
            else -> throw IllegalArgumentException("Unknown parameter mode : $paramMode")
        }


}

data class Instruction(
    val ins: Int,
    val modes: List<Int>
)


class OpCodeStream {
    val input = LinkedList<Int>()
    val output = LinkedList<Int>()

    fun read() = input.poll()!!

    fun write(value: Int) {
        output.add(value)
    }

    fun clear() {
        input.removeIf { true }
        output.removeIf { true }
    }

}