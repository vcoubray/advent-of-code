package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader


fun main() {
    val input = getInputReader("/2020/inputDay10.txt")
    val adapters = input.readLines().map { it.toInt() }.sorted()

    val diffs = mutableListOf(0, 0, 1)
    var last = 0
    adapters.forEach {
        diffs[it - last - 1]++
        last = it
    }
    println("Part 1 : ${diffs.first() * diffs.last()}")

}