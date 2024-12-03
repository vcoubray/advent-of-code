package fr.vco.aoc.y2024

val MUL = """mul\((\d{1,3}),(\d{1,3})\)"""
val DO = """do\(\)"""
val DONT = """don't\(\)"""

fun main() {
    val input = readLines("Day03").joinToString("\n")

    println("Part 1 : ${input.extractInstructions(MUL.toRegex()).execute() } ")
    println("Part 2 : ${input.extractInstructions("""(${MUL})|(${DO})|(${DONT})""".toRegex()).execute() } ")
}

fun String.extractInstructions(regex: Regex) = regex.findAll(this).map { it.value }.toList()
fun List<String>.execute(): Int {
    var enabled = true
    var result = 0
    for (it in this) {
        when {
            it == "don't()" -> enabled = false
            it == "do()" -> enabled = true
            enabled -> {
                val (a, b) = MUL.toRegex().find(it)!!.destructured
                result += (a.toInt() * b.toInt())
            }
        }
    }
    return result
}