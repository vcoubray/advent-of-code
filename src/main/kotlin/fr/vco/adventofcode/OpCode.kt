package fr.vco.adventofcode

import java.util.*


class OpCode(private val opCodeOrigin: List<Int>) {
    private var cursor = 0
    private lateinit var opCode : MutableList<Int>

    fun exec(stream : OpCodeStream) {
        cursor = 0
        opCode = opCodeOrigin.toMutableList()
        while (cursor < opCode.size) {
            val ins= getInstruction()
            execInstuction(ins,stream)
        }
    }

    fun execInstuction(ins : Instruction, stream : OpCodeStream) {
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
                opCode[param] = stream.read()
            }
            4 -> {
                val param = getParamIndex(ins.mode1)
                stream.write(opCode[param])
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


class OpCodeStream(input: List<Int>)  {
    private val input = LinkedList(input)
    private val output = LinkedList<Int>()

    fun read() = input.poll()!!

    fun write(value: Int) {
        output.add(value)
    }

    fun getOutput() = output

}