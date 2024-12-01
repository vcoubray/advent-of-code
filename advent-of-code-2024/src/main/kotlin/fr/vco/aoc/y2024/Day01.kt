package fr.vco.aoc.y2024

import kotlin.math.absoluteValue

fun main() {
    val input = readLines("Day01")

    val (list1, list2) = input.splitLists()
    println("Part 1 : ${list1.sorted().zip(list2.sorted()).sumOf { (a, b) -> (a - b).absoluteValue }}")

    val list2Occurrence = list2.associateWithOccurrence().toMap()
    println("Part 2 : ${list1.associateWithOccurrence().sumOf { (n, count) -> count * n * (list2Occurrence[n] ?: 0) }}")

}

fun List<String>.splitLists() = this.fold(List(2){ mutableListOf<Int>()}) {  lists, line  ->
        val (a, b) = line.split("   ").map { it.toInt() }
        lists[0].add(a)
        lists[1].add(b)
        lists
    }

fun List<Int>.associateWithOccurrence() = this.groupBy { it }.map { (k, v) -> k to v.size }