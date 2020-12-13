package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay13.txt")
    val lines = input.readLines()

    val timestamp = lines.first().toInt()
    val bus = lines.last().split(",")

    bus.filterNot { it == "x" }
        .map { it.toInt() }
        .map {
            var i = timestamp / it
            while (it * i <= timestamp) i++
            it to (i * it) - timestamp
        }.minByOrNull { (_, time) -> time }
        ?.let { (id, time) -> println("Part 1 : ${id * time}") }

    bus.asSequence()
        .mapIndexed { i, it -> it to i }
        .filterNot { it.first == "x" }
        .map { it.first.toLong() to it.second % it.first.toLong() }
        .map { (id, offset) -> Bus(id, offset) }
        .reduce(::findBus)
        .let { println("Part 2 : ${it.offset}") }
}

data class Bus(val id: Long, val offset: Long)

fun findBus(a: Bus, b: Bus): Bus {
    var i = 0L
    var timestamp = 0L
    while (timestamp <= a.id * b.id) {
        timestamp = a.id * i++ + a.offset
        if (timestamp % b.id == b.id - b.offset)
            return Bus(a.id * b.id, timestamp)
    }
    error("Should not happen")
}

