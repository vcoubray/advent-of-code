package fr.vco.aoc.y2023

typealias EngineSchematic = List<String>

fun main() {
    val schema = readLines("Day03")
    val numbers = schema.flatMapIndexed { i, it -> it.getPartNumbers(i) }

    println("Part 1: ${numbers.filter { schema.isPartNumber(it) }.sumOf { it.number }}")
}


data class PartNumber(
    val number: Int,
    val line: Int,
    val range: IntRange,
)

fun EngineSchematic.isPartNumber(number: PartNumber): Boolean {
    return (number.line - 1..number.line + 1).any { y ->
        (number.range.first - 1..number.range.last + 1).any { x ->
            isSymbol(x, y)
        }
    }
}

fun EngineSchematic.isSymbol(x: Int, y: Int): Boolean {
    val c = this.getOrNull(y)?.getOrNull(x)
    return c != null && !c.isDigit() && c != '.'
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