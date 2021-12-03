package fr.vco.aoc.y2021

fun main() {

    val input = readLines("Day03")
    val bitLength = input.first().length

    val gammaRate = MutableList(bitLength, input::getMostCommonBit).joinToString("").toInt(2)
    val epsilonRate = MutableList(bitLength, input::getLessCommonBit).joinToString("").toInt(2)
    println("Part 1 : ${gammaRate * epsilonRate}")

    val oxygenRating = calcPart2Rate(input) { list, id -> list.getMostCommonBit(id) }
    val co2Rating = calcPart2Rate(input) { list, id -> list.getLessCommonBit(id) }
    println("Part 2 : ${oxygenRating * co2Rating}")
}

private fun List<String>.countBits(index: Int) = this.count { it[index] == '1' }
private fun List<String>.getMostCommonBit(index: Int) = if (this.countBits(index) >= this.size / 2.0) '1' else '0'
private fun List<String>.getLessCommonBit(index: Int) = if (this.countBits(index) < this.size / 2.0) '1' else '0'

private fun calcPart2Rate(
    input: List<String>,
    index: Int = 0,
    filter: (List<String>, id: Int) -> Char
): Int {
    return if (input.size == 1) input.first().toInt(2)
    else calcPart2Rate(
        input.filter { it[index] == filter(input, index) },
        index + 1,
        filter
    )
}