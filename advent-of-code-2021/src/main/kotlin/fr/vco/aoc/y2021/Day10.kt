package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day10")

    val (corruptedScores, completionScores) = input.map { it.errorScore() }.partition { it.corruptionScore != 0L }
    println("Part 1 : ${corruptedScores.sumOf { it.corruptionScore }}")
    println("Part 2 : ${completionScores.medianOf { it.completionScore }.completionScore}")
}

val expectedClosure = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val closurePoints = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val completionPoints = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

private fun <T> List<T>.medianOf(selector : (it: T) -> Long) = this.sortedBy(selector)[this.size / 2]

private data class ErrorScore(val corruptionScore: Long, val completionScore: Long)

private fun String.errorScore(): ErrorScore {
    val stack = LinkedList<Char>()
    var corruptionScore = 0L
    for (c in this) {
        if (c in expectedClosure.keys) stack.addFirst(expectedClosure[c]!!)
        else corruptionScore += if (c != stack.pop()) closurePoints[c]!! else 0
    }

    var completionScore = 0L
    while (!stack.isEmpty()) {
        completionScore = completionScore * 5 + completionPoints[stack.pop()]!!
    }
    return ErrorScore(corruptionScore, completionScore)
}
