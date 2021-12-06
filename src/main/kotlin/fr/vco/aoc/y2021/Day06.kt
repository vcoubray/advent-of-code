package fr.vco.aoc.y2021

const val MAX_AGE = 9

fun main() {

    val input = readLines("Day06")
    val fishes = input.first().split(",")
        .map { it.toInt() }
        .groupingBy { it }
        .eachCount()

    val fishesByTimer = List(MAX_AGE) { fishes.getOrDefault(it, 0).toLong() }

    println("Part 1 : ${fishesByTimer.simulate(80).sum()}")
    println("Part 2 : ${fishesByTimer.simulate(256).sum()}")
}

fun List<Long>.simulateOneDay(): List<Long> {
    val fishes = MutableList(size) { this[(it + 1) % size] }
    fishes[6] += this[0]
    return fishes
}

fun List<Long>.simulate(days: Int): List<Long> {
    var fishes = this
    repeat(days) {
        fishes = fishes.simulateOneDay()
    }
    return fishes
}
