package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day08")
    val instructions = input.first()
    val map = DesertMap(input.drop(2))

    println("Part 1: ${map.countStep("AAA", "ZZZ", instructions)}")
    println("Part 2: ${map.countStepForGhost("A", "Z", instructions)}")
}

fun String.toNode(): Pair<String, Pair<String, String>> {
    val (id, left, right) = """(.*) = \((.*), (.*)\)""".toRegex().find(this)!!.destructured
    return id to (left to right)
}

class DesertMap(private val nodes: Map<String, Pair<String, String>>) {
    constructor(input: List<String>) : this(input.associate { it.toNode() })

    fun countStep(start: String, end: String, instructions: String): Int {
        var step = 0
        var current = start

        while (!current.endsWith(end)) {
            val instruction = instructions[step.mod(instructions.length)]
            val (left, right) = nodes[current]!!
            current = if (instruction == 'R') right else left
            step++
        }
        return step
    }

    fun countStepForGhost(start: String, end: String, instructions: String): Long {
        return nodes.keys.filter { it.endsWith(start) }
            .map { countStep(it, end, instructions) }
            .map { it.toLong() }
            .ppcm()
    }

}


