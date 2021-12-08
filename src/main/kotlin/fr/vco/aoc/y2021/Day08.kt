package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day08")

    val numbers = input.map { line ->
        val (inputNumbers, outputNumbers) = line.split(" | ").map { numbers ->
            numbers.split(" ").map { it.toSortedString() }
        }
        DigitDisplay(inputNumbers, outputNumbers)
    }

    println("Part 1 : ${numbers.sumOf { it.countNumbersWithUniqueSize() }}")
    println("Part 2 : ${numbers.sumOf { it.outputToInt() }}")
}

private fun String.toSortedString() = this.toSortedSet().joinToString("")

private class DigitDisplay(
    val input: List<String>,
    val output: List<String>
) {
    val numbersMap = MutableList(10) { "" }
    val reverseMap: Map<String, String>

    private companion object {
        val UNIQUE_SIZES = listOf(2, 3, 4, 7)
    }

    init {
        findNumbersWithUniqueSize()
        findNumbersWith5Segments()
        findNumbersWith6Segments()
        reverseMap = numbersMap.mapIndexed { i, it -> it to i.toString() }.toMap()
    }

    fun countNumbersWithUniqueSize() = output.count { it.length in UNIQUE_SIZES }

    fun findNumbersWithUniqueSize() {
        numbersMap[1] = input.first { it.length == 2 }
        numbersMap[4] = input.first { it.length == 4 }
        numbersMap[7] = input.first { it.length == 3 }
        numbersMap[8] = input.first { it.length == 7 }
    }

    fun findNumbersWith6Segments() {
        val (n0Andn9, n6) = input.filter { it.length == 6 }.partition { numbersMap[1].all { segment -> segment in it } }
        numbersMap[6] = n6.first()

        val (n9, n0) = n0Andn9.partition { numbersMap[4].all { segment -> segment in it } }
        numbersMap[0] = n0.first()
        numbersMap[9] = n9.first()
    }

    fun findNumbersWith5Segments() {
        val (n3, n2and5) = input.filter { it.length == 5 }.partition { numbersMap[1].all { segment -> segment in it } }
        numbersMap[3] = n3.first()

        val topLeftSegment = numbersMap[4] - numbersMap[3]
        val (n5, n2) = n2and5.partition { topLeftSegment in it }
        numbersMap[2] = n2.first()
        numbersMap[5] = n5.first()
    }

    fun outputToInt() = output.joinToString("") { reverseMap[it]!! }.toInt()
}

private operator fun String.minus(string: String): String {
    return (this.toList() - string.toSet()).joinToString("")
}