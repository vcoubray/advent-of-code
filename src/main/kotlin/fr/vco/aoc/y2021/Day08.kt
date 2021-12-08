package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day08")

    val numbers = input.map {
        val (inputNumbers, outputNumbers) = it.split(" | ").map { numbers -> numbers.split(" ") }
        Numbers(inputNumbers, outputNumbers)
    }

    println("Part 1 : ${numbers.sumOf { it.countUniqueSizeOutputNumbers() }}")
}

private class Numbers(
    val input: List<String>,
    val output: List<String>
) {
    private companion object {
        val UNIQUE_SIZES = listOf(2, 3, 4, 7)
    }
    fun countUniqueSizeOutputNumbers() = output.count { it.length in UNIQUE_SIZES }
}