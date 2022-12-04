package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day04")

    val pairs = input.map { """^(\d+)-(\d+),(\d+)-(\d+)$""".toRegex().find(it)!!.destructured }
        .map { (a, b, c, d) -> a.toInt()..b.toInt() to c.toInt()..d.toInt() }

    println("Part 1 : ${pairs.count (::overlapCompletely)}")
    println("Part 2 : ${pairs.count (::overlap)}")
}

fun overlapCompletely(pair : Pair<IntRange, IntRange>) = pair.first.include(pair.second) || pair.second.include(pair.first)
fun overlap (pair : Pair<IntRange, IntRange>) = pair.first.overlap(pair.second) || pair.second.overlap(pair.first)

fun IntRange.include(other: IntRange) = first <= other.first && last >= other.last
fun IntRange.overlap(other: IntRange) = first in other || last in other
