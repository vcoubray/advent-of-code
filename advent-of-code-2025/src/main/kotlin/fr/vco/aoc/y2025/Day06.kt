package fr.vco.aoc.y2025

import kotlin.collections.map

fun main() {
    val input = readLines("Day06")
    val problems = input.toCephalopodProblems()

    println("Part 1 : ${problems.sumOf { it.resolve() }}")
    println("Part 2 : ${problems.sumOf { it.resolvePart2() }}")
}

class CephalopodProblem(input: List<String>) {
    val numbers = input.dropLast(1)
    val operation: (Long, Long) -> Long = if (input.last().trim() == "*") { a, b -> a * b } else { a, b -> a + b }

    fun resolve() = numbers.map { it.trim().toLong() }.reduce(operation)
    fun resolvePart2(): Long {
        val columnSize = numbers.first().length
        return (0..<columnSize).map { i -> numbers.joinToString("") { number -> "${number[i]}" } }
            .map { it.trim().toLong() }
            .reduce(operation)
    }
}

fun List<String>.toCephalopodProblems(): List<CephalopodProblem> {
    val operatorsRow = this.last()
    val operatorsIndex = operatorsRow.mapIndexedNotNull { i, c -> if (c != ' ') i else null }
    val columnsIndex = (operatorsIndex + (operatorsRow.length + 1)).windowed(2).map { (start, end) -> start to end - 1 }

    return columnsIndex.map { (start, end) -> this.map { it.substring(start, end) } }
        .map { CephalopodProblem(it) }
}


