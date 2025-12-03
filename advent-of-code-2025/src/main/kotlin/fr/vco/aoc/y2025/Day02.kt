package fr.vco.aoc.y2025

fun main() {
    val input = readLines("Day02").first()

    val ranges = input.split(",")
        .map { it.split("-") }
        .map { (start, end) -> start.toLong()..end.toLong() }

    println("Part 1: ${ranges.flatMap { getInvalidIds(it, 2) }.sum()}")
    println("Part 2: ${ranges.flatMap { getInvalidIds(it) }.sum()}")
}

fun getInvalidIds(range: LongRange, maxDivisor: Int? = null): List<Long> {
    return (range).filter {
        val value = it.toString()
        (2..(maxDivisor ?: value.length)).any { divisor ->
            if (value.length % divisor != 0) false
            else value.chunked(value.length / divisor).toSet().size == 1
        }
    }
}