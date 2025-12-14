package fr.vco.aoc.y2025

import kotlin.math.max

fun main() {
    val input = readLines("Day05")

    val freshIdRanges = input.takeWhile { it.isNotBlank() }
        .map { it.split("-") }
        .map { (a, b) -> a.toLong()..b.toLong() }

    val ingredientIds = input.drop(freshIdRanges.size + 1)
        .map { it.toLong() }

    println("Part 1 : ${ingredientIds.count { id -> freshIdRanges.any { id in it } }}")
    println("Part 2 : ${freshIdRanges.reduced().sumOf { it.countLong() }}")
}

fun List<LongRange>.reduced(): List<LongRange> {
    val reducedList = mutableListOf<LongRange>(this.minByOrNull { it.first }!!)
    this.sortedBy { it.first }.drop(1).forEach {
        val lastAddedRange = reducedList.last()
        if (lastAddedRange.overlaps(it)) {
            reducedList[reducedList.size - 1] = lastAddedRange.first..max(lastAddedRange.last, it.last)
        } else {
            reducedList.add(it)
        }
    }
    return reducedList
}

fun LongRange.overlaps(other: LongRange): Boolean {
    return other.first in first..last || other.last in first..last || last in other.first..other.last
}
fun LongRange.countLong() = last - first + 1

