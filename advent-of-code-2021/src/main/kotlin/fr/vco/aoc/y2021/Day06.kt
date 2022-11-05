package fr.vco.aoc.y2021

const val MAX_AGE = 9

fun main() {

    val input = readFirstLine("Day06")
    val fishes = input.split(",")
        .map { it.toInt() }
        .groupingBy { it }
        .eachCount()

    val fishesByTimer = List(MAX_AGE) { fishes.getOrDefault(it, 0).toLong() }

    println("Part 1 : ${fishesByTimer.simulate(80).sum()}")
    println("Part 2 : ${fishesByTimer.simulate(256).sum()}")
}

private fun List<Long>.simulateOneDay(): List<Long> {
    val fishes = MutableList(size) { this[(it + 1) % size] }
    fishes[6] += this[0]
    return fishes
}

private fun List<Long>.simulate(days: Int): List<Long> {
    return (0 until days).fold(this) { acc, _ -> acc.simulateOneDay() }
}