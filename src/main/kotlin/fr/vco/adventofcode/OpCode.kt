package fr.vco.adventofcode

import java.lang.IllegalArgumentException
import java.util.*


class OpCode(private val opCodeOrigin: List<Long>, val stream: OpCodeStream) {
    private var cursor = 0L
    private var opCode = generateOpCode()
    private var paused = false
    private var relativeBase = 0L
    private var isEnded = false


    private fun generateOpCode() =
        opCodeOrigin.mapIndexed{index, it -> index.toLong() to it}.toMap().toMutableMap()

    private fun get(index :Long) : Long {
        if(!opCode.containsKey(index)) opCode[index] = 0
        return opCode[index]!!
    }


    fun restart() {
        cursor = 0
        opCode = generateOpCode()
        stream.clear()
        isEnded = false
    }

    fun isEnded() = isEnded

    fun exec() {
        paused = false
        while (!paused && !isEnded()) {
            val ins = getInstruction()
            execInstuction(ins)
        }
    }


    private fun execInstuction(ins: Instruction) {
        when (ins.ins) {
            99 -> isEnded = true
            1 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = get(param1) + get(param2)
            }
            2 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = get(param1) * get(param2)
            }
            3 -> {
                val (param) = getParamIndexes(1, ins.modes)
                opCode[param] = stream.read()
            }
            4 -> {
                val (param) = getParamIndexes(1, ins.modes)
                stream.write(get(param))
                paused = true
            }
            5 -> {
                val (param1, param2) = getParamIndexes(2, ins.modes)
                if (get(param1) != 0L) cursor = get(param2)
            }
            6 -> {
                val (param1, param2) = getParamIndexes(2, ins.modes)
                if (get(param1) == 0L) cursor = get(param2)
            }
            7 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = if (get(param1) < get(param2)) 1L else 0L
            }
            8 -> {
                val (param1, param2, param3) = getParamIndexes(3, ins.modes)
                opCode[param3] = if (get(param1) == get(param2)) 1L else 0L
            }
            9 -> {
                val (param1) = getParamIndexes(1, ins.modes)
                relativeBase += get(param1)
            }
            else -> throw IllegalStateException("Unknown instruction : ${get(cursor)}  ${ins}")
        }
    }

    private fun getInstruction(): Instruction {
        val insStr = get(cursor++).toString().reversed().map { it.toString().toInt() }
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
            0 -> get(cursor++)
            1 -> cursor++
            2 -> get(cursor++) + relativeBase
            else -> throw IllegalArgumentException("Unknown parameter mode : $paramMode")
        }


}

data class Instruction(
    val ins: Int,
    val modes: List<Int>
)


class OpCodeStream {
    val input = LinkedList<Long>()
    val output = LinkedList<Long>()

    fun read() = input.poll()!!

    fun write(value: Long) {
        output.add(value)
    }

    fun clear() {
        input.removeIf { true }
        output.removeIf { true }
    }

}