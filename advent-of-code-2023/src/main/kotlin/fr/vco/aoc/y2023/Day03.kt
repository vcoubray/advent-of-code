package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day03")
    val schema = EngineSchematic(input)

    println("Part 1: ${schema.partNumberSum()}")
    println("Part 2: ${schema.gearRatio()}")
}

data class PartNumber(
    val number: Int,
    val y: Int,
    val xRange: IntRange,
    var symbols: Map<Char, Int> = emptyMap(),
)

class EngineSchematic(
    private val schema: List<String>,
) {
    private val width = schema.firstOrNull()?.length ?: 0
    private val partNumbers = schema.flatMapIndexed { i, it -> it.getPartNumbers(i) }

    init {
        partNumbers.forEach { it.symbols = it.findSymbols() }
    }

    fun partNumberSum() = partNumbers.filter { it.symbols.isNotEmpty() }.sumOf { it.number }

    fun gearRatio(): Int {
        val gears = mutableMapOf<Int, List<Int>>()
        partNumbers.forEach { number ->
            number.symbols.filter { (k, _) -> k == '*' }
                .map { (_, v) -> v to number.number }
                .forEach { (id, number) ->
                    gears[id] = (gears[id] ?: listOf()) + number
                }
        }

        return gears.filter { (_, numbers) -> numbers.size == 2 }
            .values.sumOf { (a, b) -> a * b }
    }


    private fun PartNumber.findSymbols(): Map<Char, Int> {
        return (y - 1..y + 1).flatMap { y ->
            (xRange.first - 1..xRange.last + 1).mapNotNull { x ->
                schema.get(x, y)?.takeIf { it.isSymbol() }?.let { it to y * width + x }
            }
        }.toMap()
    }

    private fun List<String>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
    private fun Char.isSymbol() = !this.isDigit() && this != '.'
}

fun String.getPartNumbers(lineId: Int): List<PartNumber> {
    val numbers = mutableListOf<PartNumber>()
    var currentNumber = ""
    var numberId = -1

    this.forEachIndexed { i, c ->
        if (c.isDigit()) {
            currentNumber += c
            if (numberId == -1) numberId = i
        } else {
            if (numberId != -1) {
                numbers.add(PartNumber(currentNumber.toInt(), lineId, numberId until i))
                numberId = -1
                currentNumber = ""
            }
        }
    }
    if (numberId != -1) {
        numbers.add(PartNumber(currentNumber.toInt(), lineId, numberId until this.length))
    }
    return numbers
}