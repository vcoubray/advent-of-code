package fr.vco.adventofcode.y2020

import kotlin.system.measureTimeMillis

fun main() {

    val input = getInputReader("/2020/inputDay23.txt")
    val line = input.readLines().first().map { "$it".toInt() }

    println("Part 1 : ${CupCircle(line).move(100).printPart1()}")

    measureTimeMillis {
        println("Part 2 : ${CupCircle(line, 1_000_000).move(10_000_000).printPart2()}")
    }.let { println("in ${it}ms") }

}

class CupCircle(startCups: List<Int>, totalCups: Int = startCups.size) {
    private val cups: MutableList<Int> = MutableList(totalCups + 1) { 0 }
    private var current = cups.first()
    private val pickup = mutableListOf<Int>()

    init {
        cups[0] = startCups.first()
        for (i in 1 until totalCups) {
            when {
                i < startCups.size -> cups[startCups[i - 1]] = startCups[i]
                i == startCups.size -> cups[startCups[i - 1]] = i + 1
                else -> cups[i] = i + 1
            }
        }
        if (totalCups <= startCups.size) {
            cups[startCups.last()] = startCups.first()
        } else {
            cups[totalCups] = startCups.first()
        }
    }

    private fun nextCurrent() {
        current = cups[current]
    }

    private fun getDest(): Int {
        var dest = current
        do {
            if (--dest == 0) dest = cups.size - 1
        } while (pickup.contains(dest))
        return dest
    }

    private fun pickup(size: Int = 3) {
        var pick = current
        repeat(size) {
            pickup.add(cups[pick])
            pick = cups[pick]
        }
        cups[current] = cups[pick]
    }

    private fun pushPickup(dest: Int) {
        val destLink = cups[dest]
        cups[dest] = pickup.first()
        cups[pickup.last()] = destLink
        pickup.clear()
    }

    private fun move() {
        pickup()
        val dest = getDest()
        pushPickup(dest)
        nextCurrent()
    }

    fun move(turn: Int) = apply {
        repeat(turn) { move() }
    }

    fun printPart1() =
        StringBuffer().apply {
            var index = cups[1]
            while (index != 1) {
                append(index)
                index = cups[index]
            }
        }.toString()

    fun printPart2(): Long {
        val a1 = cups[1]
        val a2 = cups[a1]
        return a1.toLong() * a2.toLong()
    }

}
