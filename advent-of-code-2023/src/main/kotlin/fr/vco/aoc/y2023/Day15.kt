package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day15")
    val instructions = input.first().split(",")

    println("Part 1 : ${instructions.sumOf { it.toHash() }}")
    println("Part 2 : ${Boxes().exec(instructions).focusingPower()}")
}

fun String.toHash() = fold(0) { acc, a -> ((acc + a.code) * 17) % 256 }

class Boxes {
    companion object {
        val INSTRUCTION_REGEX = """(.+)([-=])(\d*)""".toRegex()
        val REMOVE = "-"
        val ADD = "="
    }

    private val boxes = List(256) { mutableMapOf<String, Int>() }

    fun exec(instructions: List<String>) = apply {
        instructions.forEach { instruction ->
            val (label, op, lens) = INSTRUCTION_REGEX.find(instruction)!!.destructured
            val boxId = label.toHash()
            when (op) {
                REMOVE -> boxes[boxId].remove(label)
                ADD -> boxes[boxId][label] = lens.toInt()
            }
        }
    }

    fun focusingPower() = boxes.foldIndexed(0) { i, acc, a ->
        acc + (a.takeIf { it.isNotEmpty() }?.focusingPower(i + 1) ?: 0)
    }

    private fun Map<String, Int>.focusingPower(boxNumber: Int) =
        this.values.foldIndexed(0) { i, acc, a -> acc + boxNumber * (i + 1) * a }
}