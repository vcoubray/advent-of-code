package fr.vco.aoc.y2024

import fr.vco.aoc.y2024.StoneCounter as StoneCounter

fun main() {
    val input = readLines("Day11").first()
    val stones = input.split(" ").map { it.toLong() }

    println("Part 1: ${StoneCounter.countStonesAfter(stones, 25)}")
    println("Part 2: ${StoneCounter.countStonesAfter(stones, 75)}")
}

object StoneCounter {
    private val stoneCountMap = mutableMapOf<Pair<Long, Int>, Long>()

    fun countStonesAfter(stones: List<Long>, blinks: Int): Long {
        return stones.fold(0L) { acc, a -> acc + countStones(a, blinks) }
    }

    private fun countStones(value: Long, blinks: Int): Long {
        val key = value to blinks
        if (key in stoneCountMap) return stoneCountMap[key]!!

        val nextState = nextStoneState(value)
        val stoneCount = if (blinks == 1) nextState.size.toLong()
        else nextState.fold(0L) { acc, a -> acc + countStones(a, blinks - 1) }
        stoneCountMap[key] = stoneCount
        return stoneCount
    }

    private fun nextStoneState(value: Long) = when {
        value == 0L -> listOf(1L)
        value.toString().length % 2 == 0 -> value.toString().splitMiddle()
        else -> listOf(value * 2024)
    }

    private fun String.splitMiddle() = listOf(this.take(length / 2).toLong(), this.drop(length / 2).toLong())
}
