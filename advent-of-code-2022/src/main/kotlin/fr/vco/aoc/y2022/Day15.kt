package fr.vco.aoc.y2022

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day15")

    val sensors = input.map { """^.*x=(-?\d+), y=(-?\d+):.*x=(-?\d+), y=(-?\d+)$""".toRegex().find(it)!!.destructured }
        .map { (sx, sy, bx, by) -> Sensor(sx.toInt(), sy.toInt(), bx.toInt(), by.toInt()) }

    println("Part 1 : ${sensors.countCoveredTiles(2_000_000)}")
    println("Part 2: ${sensors.findDistressBeacon(4_000_000).tuningFrequency()}")
}

class Sensor(private val sx: Int, private val sy: Int, val bx: Int, val by: Int) {
    private val closestBeacon = dist(bx, by)

    fun dist(x: Int, y: Int) = abs(sx - x) + abs(sy - y)

    fun coverForLine(y: Int): IntRange? {
        val distX = closestBeacon - abs(sy - y)
        return if (distX >= 0) (sx - distX)..(sx + distX)
        else null
    }
}

class LineCover(ranges: List<IntRange>) {
    private var ranges = mutableListOf<IntRange>()

    init {
        ranges.forEach(::addRange)
    }

    private fun addRange(range: IntRange) {
        var current = range
        val toRemove = mutableListOf<IntRange>()
        ranges.forEach {
            if (it.first in current || it.last in current || current.first in it || current.last in it) {
                current = min(it.first, current.first)..max(it.last, current.last)
                toRemove.add(it)
            }
        }
        ranges.removeAll(toRemove)
        ranges.add(current)
    }

    fun count() = ranges.sumOf { it.count() }

    fun getUncoveredRanges(range: IntRange): List<IntRange> {
        val filteredRanges = ranges.filter { it.first <= range.last  &&  it.last >= range.first }
        return if (filteredRanges.size == 1) emptyList()
        else filteredRanges.windowed(2).map { (a, b) -> a.last..b.first }
    }
}

fun List<Sensor>.countBeams(y: Int) = this.map { it.bx to it.by }.toSet().count { (_, by) -> by == y }
fun List<Sensor>.toLineCover(y: Int) = LineCover(this.mapNotNull { it.coverForLine(y) })
fun List<Sensor>.countCoveredTiles(y: Int) = toLineCover(y).count() - countBeams(y)

fun List<Sensor>.findDistressBeacon(range: Int): Pair<Int, Int> {
    for (y in 0..range) {
        val ranges = this.toLineCover(y).getUncoveredRanges(0.. range)
        if (ranges.isNotEmpty()) {
            return ranges.first().first+1 to y
        }
    }
    return 0 to 0
}

fun Pair<Int, Int>.tuningFrequency() = first * 4_000_000L + second