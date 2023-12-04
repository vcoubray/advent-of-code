package fr.vco.aoc.y2023

import kotlin.math.pow

fun main() {
    val input = readLines("Day04")
    val scratchcards = input.map { it.countWinningNumbers() }

    println("Part 1: ${scratchcards.sumOf { it.pointsToScore() }}")
    println("Part 2: ${scratchcards.countTotalTicket()}")
}

val REGEX = """^.*: (.*) \| (.*)$""".toRegex()
fun String.countWinningNumbers(): Int {
    val (winning, mine) = REGEX.find(this)!!.destructured
    return winning.toNumberList().count { it in mine.toNumberList() }
}

fun String.toNumberList() = split(" ").filter { it != "" }
fun Int.pointsToScore() = 2.0.pow((this - 1).toDouble()).toInt()

fun List<Int>.countTotalTicket() : Int {
    val wonTickets = MutableList(size) { 0 }
    for (i in indices.reversed()) {
        wonTickets[i] = this[i]
        repeat(this[i]) {
            wonTickets[i] += wonTickets[it + i]
        }
    }
    return size + wonTickets.sum()
}


