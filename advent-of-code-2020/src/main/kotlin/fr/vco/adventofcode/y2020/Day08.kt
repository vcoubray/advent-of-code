package fr.vco.adventofcode.y2020

fun main() {
    val input = getInputReader("Day08")
    val lines = input.readLines()

    val code = BootCode(lines.map { it.split(" ") }.map { it.first() to it.last().toInt() })

    println("Part 1 : ${code.exec()}")
    println("Part 2 : ${code.execWithoutLoop()}")
}


class BootCode(private val instructions: List<Pair<String, Int>>) {
    private var acc: Int = 0
    private var cursor: Int = 0
    private var swapIndex: Int = -1

    fun execWithoutLoop() : Int {
        swapIndex = 0
        while (looping()) {
            if (instructions[swapIndex].first != "acc") {
                exec()
            }
            swapIndex++
        }
        return acc
    }


    fun exec() : Int{
        reset()
        val visited = mutableSetOf<Int>()
        while (!visited.contains(cursor) && cursor < instructions.size) {
            visited.add(cursor)
            exec(instructions[cursor])
        }
        return acc
    }

    private fun reset() {
        acc = 0
        cursor = 0
    }

    private fun looping() = cursor != instructions.size

    private fun exec(instruction: Pair<String, Int>) {
        val (ins, n) = instruction
        when {
            ins == "acc" -> { acc += n; cursor++ }
            (ins ==  "jmp" && swapIndex != cursor) ||
            (ins ==  "nop" && swapIndex == cursor) -> cursor += n
            else -> cursor++
        }
    }
}