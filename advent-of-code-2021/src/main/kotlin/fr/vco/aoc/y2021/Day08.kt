package fr.vco.aoc.y2021

typealias SegmentDigit = Set<Char>

private fun emptySegmentDigit() = emptySet<Char>()

fun main() {
    val input = readLines("Day08")

    val displays = input.map { line ->
        val (inputDigits, outputDigits) = line.split(" | ").map { it.toSegmentDigitList() }
        DigitDisplay(inputDigits, outputDigits)
    }

    println("Part 1 : ${displays.sumOf { it.countDigitsWithUniqueSize() }}")
    println("Part 2 : ${displays.sumOf { it.outputToInt() }}")
}

private fun String.toSegmentDigitList() = this.split(" ").map { it.toSet() }
private operator fun SegmentDigit.contains(digit: SegmentDigit) = digit.all { it in this }

private class DigitDisplay(
    val input: List<SegmentDigit>,
    val output: List<SegmentDigit>
) {
    val digitsMap = MutableList(10) { emptySegmentDigit() }
    val reverseMap: Map<SegmentDigit, String>

    private companion object {
        val UNIQUE_SIZES = listOf(2, 3, 4, 7)
    }

    init {
        val digitsBySize = input.groupBy { it.size }

        // Digit with unique Size (1, 4, 7 and 8)
        digitsMap[1] = digitsBySize[2]!!.first()
        digitsMap[4] = digitsBySize[4]!!.first()
        digitsMap[7] = digitsBySize[3]!!.first()
        digitsMap[8] = digitsBySize[7]!!.first()

        // Digit with Size 5 (2, 3 and 5)
        val (n3, n2and5) = digitsBySize[5]!!.partition { digitsMap[1] in it }
        digitsMap[3] = n3.first()
        val topLeftSegment = digitsMap[4] - digitsMap[3]
        val (n5, n2) = n2and5.partition { topLeftSegment in it }
        digitsMap[2] = n2.first()
        digitsMap[5] = n5.first()

        // Digit with Size 6 (0, 6 and 9)
        val (n0And9, n6) = digitsBySize[6]!!.partition { digitsMap[1] in it }
        digitsMap[6] = n6.first()
        val (n9, n0) = n0And9.partition { digitsMap[4] in it }
        digitsMap[0] = n0.first()
        digitsMap[9] = n9.first()

        reverseMap = digitsMap.mapIndexed { i, it -> it to i.toString() }.toMap()
    }

    fun countDigitsWithUniqueSize() = output.count { it.size in UNIQUE_SIZES }
    fun outputToInt() = output.joinToString("") { reverseMap[it]!! }.toInt()
}
