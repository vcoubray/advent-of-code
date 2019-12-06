package fr.vco.adventofcode

import java.lang.IllegalStateException
import java.util.*

fun main() {

    val input = getInputReader("/inputAoC5.txt")
    val opCodeStr = input.readText().trim().split(",").map { it.toInt() }

    val opCodePart1 = OpCode(opCodeStr.toMutableList())
    val inputPart1 = "1"
    println("Part 1 : ${opCodePart1.exec(inputPart1)}")

    val opCodePar2 = OpCode(opCodeStr.toMutableList())
    val inputPart2 = "5"
    println("Part 2 : ${opCodePar2.exec(inputPart2)}")

}

class OpCode(private val opCode: MutableList<Int>) {
    private var cursor = 0

    fun exec(input: String): String {
        val scanner = Scanner(input)
        val output = StringBuilder()

        while (cursor < opCode.size) {
            val ins= getInstruction()
            execInstuction(ins,scanner,output)
        }
        return output.toString()
    }

    fun execInstuction(ins : Instruction, scanner: Scanner, output : StringBuilder) {
        when (ins.ins) {
            99 -> cursor = opCode.size
            1 -> {
                val input1 = getParamIndex(ins.mode1)
                val input2 = getParamIndex(ins.mode2)
                val output = getParamIndex(ins.mode3)
                opCode[output] = opCode[input1] + opCode[input2]
            }
            2 -> {
                val input1 = getParamIndex(ins.mode1)
                val input2 = getParamIndex(ins.mode2)
                val output = getParamIndex(ins.mode3)
                opCode[output] = opCode[input1] * opCode[input2]
            }
            3 -> {
                val param = getParamIndex(ins.mode1)
                opCode[param] = scanner.nextInt()
            }
            4 -> {
                val param = getParamIndex(ins.mode1)
                output.append("${opCode[param]} ")
            }
            5 -> {
                val param1 = getParamIndex(ins.mode1)
                val param2 = getParamIndex(ins.mode2)
                if(opCode[param1]!=0) cursor = opCode[param2]
            }
            6 -> {
                val param1 = getParamIndex(ins.mode1)
                val param2 = getParamIndex(ins.mode2)
                if(opCode[param1]==0) cursor = opCode[param2]
            }
            7 -> {
                val param1 = getParamIndex(ins.mode1)
                val param2 = getParamIndex(ins.mode2)
                val param3 = getParamIndex(ins.mode3)
                opCode[param3] = if(opCode[param1] < opCode[param2]) 1 else 0
            }
            8 -> {
                val param1 = getParamIndex(ins.mode1)
                val param2 = getParamIndex(ins.mode2)
                val param3 = getParamIndex(ins.mode3)
                opCode[param3] = if(opCode[param1] == opCode[param2]) 1 else 0
            }
            else -> throw IllegalStateException("Unknown instruction : ${opCode[cursor]}  ${ins}")
        }
    }

    fun getInstruction() : Instruction {
        val insStr = opCode[cursor++].toString().reversed().map{it.toString().toInt()}
        val ins = Instruction(
            insStr[0] + (insStr.elementAtOrElse(1){0} * 10),
            insStr.elementAtOrElse(2) { 0 },
            insStr.elementAtOrElse(3) { 0 },
            insStr.elementAtOrElse(4) { 0 }
        )
        return ins
    }

    fun getParamIndex(paramMode : Int) =
        if(paramMode == 1) cursor++
        else opCode[cursor++]

}

data class Instruction(
    val ins: Int,
    val mode1: Int,
    val mode2: Int,
    val mode3: Int
)
