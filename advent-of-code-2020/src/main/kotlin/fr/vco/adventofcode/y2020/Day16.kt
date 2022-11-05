package fr.vco.adventofcode.y2020

import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = getInputReader("/2020/inputDay16.txt")
    val lines = input.readLines()

    lateinit var fields : List<Field>
    lateinit var tickets : List<List<Int>>
    lateinit var myTicket: List<Int>
    lines.filterNot { it.isBlank() }
        .filterNot("((your ticket)|(nearby tickets)):".toRegex()::matches)
        .partition { Field.REGEX.matches(it) }
        .let {
            fields = it.first.map(::Field)
            myTicket = it.second.first().split(",").map{it.toInt()}
            tickets = it.second.drop(1).map{t -> t.split(",").map{n -> n.toInt()}}

        }

    val multiRange = fields.map { it.ranges }.flatten().fold(MultiRange()) { multiRange, it -> multiRange.addRange(it) }

    println("Part 1 : ${tickets.flatten().filterNot(multiRange::contains).sum()}")

    val validTickets = tickets.filter { field -> field.all(multiRange::contains) }
    fields.forEach { field ->
        myTicket.indices
            .filter { validTickets.all { ticket -> field.contains(ticket[it]) } }
            .forEach(field.validColumns::add)
    }

    val alreadyUsedColumn = mutableListOf<Int>()
    fields.asSequence()
        .sortedBy { it.validColumns.size }
        .map { it to it.getColumn(alreadyUsedColumn).also(alreadyUsedColumn::add) }
        .filter { it.first.name.startsWith("departure") }
        .map { it.second }
        .fold(1L) { acc, i -> acc * myTicket[i] }
        .let { println("Part 2 : $it") }


}

fun IntRange.isNear(range: IntRange) = this.last + 1 == range.first || range.last + 1 == this.first
fun IntRange.isCollapsing(range: IntRange): Boolean = this.first in range || range.first in this || isNear(range)
fun IntRange.merge(range: IntRange): IntRange = min(this.first, range.first)..max(this.last, range.last)

class MultiRange {
    private val ranges = mutableSetOf<IntRange>()

    fun addRange(range: IntRange) = apply {
        val collapsingRange = ranges.filter { it.isCollapsing(range) }
        ranges.removeAll(collapsingRange)
        collapsingRange.fold(range) { mergedRange, it -> mergedRange.merge(it) }.let(ranges::add)
    }

    fun contains(int: Int) = ranges.any { int in it }

}


class Field(field: String) {
    companion object {
        val REGEX = "(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex()
    }

    init {
        REGEX.find(field)?.groupValues?.let { groups ->
            this.name = groups[1]
            ranges = listOf(
                groups[2].toInt()..groups[3].toInt(),
                groups[4].toInt()..groups[5].toInt()
            )
        }
    }

    lateinit var name: String
    lateinit var ranges: List<IntRange>
    val validColumns = mutableListOf<Int>()

    fun contains(int: Int) = ranges.any { int in it }
    fun getColumn(alreadyUsed: List<Int>) = validColumns.first { !alreadyUsed.contains(it) }
}
