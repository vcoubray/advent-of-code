package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day09")
    val subSequences = input.map { it.split(" ").map { n -> n.toInt() } }
        .map { it.toSubSequences() }

    println("Part 1: ${subSequences.sumOf { it.extrapolateLast() }}")
    println("Part 2: ${subSequences.sumOf { it.extrapolateFirst() }}")
}

fun List<Int>.toSubSequence() = this.windowed(2).map { (a, b) -> b - a }
fun List<Int>.toSubSequences(): List<List<Int>> {
    val subSequences = mutableListOf(this)
    var subSequence = this.toSubSequence()
    while (subSequence.any { it != 0 }) {
        subSequences.add(subSequence)
        subSequence = subSequence.toSubSequence()
    }
    return subSequences
}

fun List<List<Int>>.extrapolateLast() = this.sumOf { it.last() }
fun List<List<Int>>.extrapolateFirst() = this.reversed().fold(0) { acc, a -> a.first() - acc }