package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import kotlin.math.max
import kotlin.math.min

enum class Mode { FIELD, MY_TICKET, TICKET }

fun main() {
    val input = getInputReader("/2020/inputDay16.txt")
    val lines = input.readLines()

    val fields = mutableListOf<Field>()
    val tickets = mutableListOf<List<Int>>()
    var mode = Mode.FIELD
    lines.filterNot{it.isBlank()}
        .filterNot ( "((your ticket)|(nearby tickets)):".toRegex()::matches)
        .forEach {
            if (Field.REGEX.matches(it))
                Field.REGEX.find(it)?.groupValues?.let { groups ->
                    fields.add(
                        Field(
                            name = groups[1],
                            ranges = listOf(
                                groups[2].toInt()..groups[3].toInt(),
                                groups[4].toInt()..groups[5].toInt()
                            )
                        )
                    )
                }
             else it.split(",").map { n -> n.toInt() }.let(tickets::add)
        }

    val myTicket = tickets.first()!!
    tickets.removeFirst()

    val multiRange = fields.map{it.ranges}.flatten().fold(MutliRange()){multiRange, it -> multiRange.addRange(it)}

    println("Part 1 : ${tickets.flatten().filterNot(multiRange::contains).sum()}")

}

fun IntRange.isNear(range: IntRange) = this.last+1 == range.first || range.last+1 == this.first
fun IntRange.isCollapsing(range :IntRange) :Boolean = this.first in range || range.first in this || isNear(range)
fun IntRange.merge (range : IntRange) : IntRange = min(this.first, range.first)..max(this.last, range.last)

class MutliRange{
    private val ranges = mutableSetOf<IntRange>()

    fun addRange(range: IntRange) = apply {
        val collapsingRange = ranges.filter{ it.isCollapsing(range)}
        ranges.removeAll(collapsingRange)
        collapsingRange.fold(range){mergedRange, it -> mergedRange.merge(it)}.let(ranges::add)
    }

    fun contains(int : Int) = ranges.any{it.contains(int)}

}


data class Field(val name: String, val ranges : List<IntRange> ) {
    companion object {
        val REGEX = "(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex()
    }

}
