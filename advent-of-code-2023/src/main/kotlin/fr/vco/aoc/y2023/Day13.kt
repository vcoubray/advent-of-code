package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day13")
    val patterns = input.split { it.isBlank() }
        .map(::MirrorPattern)

    println("Part 1: ${patterns.sumOf { it.reflectionScore() }}")
    println("Part 2: ${patterns.sumOf { it.reflectionScore(1) }}")
}

class MirrorPattern(mirrors: List<String>) {

    private val horizontalDiffs = mirrors.mapToHorizontalReflectionDiffs()
    private val verticalsDiffs = mirrors.mapToVerticalReflectionDiffs()

    fun reflectionScore(diff: Int = 0): Int {
        return (horizontalDiffs.indexOf(diff) + 1) * 100 + (verticalsDiffs.indexOf(diff) + 1)
    }

    private fun List<String>.mapToHorizontalReflectionDiffs(): List<Int> {
        return (0..this.size - 2).map { i ->
            var currentDiff = 0
            for (j in 0..<(i + 1).coerceAtMost(size - i - 1)) {
                currentDiff += this[i + j + 1].countDiff(this[i - j])
            }
            currentDiff
        }
    }

    private fun List<String>.mapToVerticalReflectionDiffs(): List<Int> {
        return transpose(this).mapToHorizontalReflectionDiffs()
    }

    private fun String.countDiff(other: String) = this.filterIndexed { i, it -> it != other[i] }.length
}
