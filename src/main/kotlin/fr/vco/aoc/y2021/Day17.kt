package fr.vco.aoc.y2021

import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    val input = readFirstLine("Day17")
    val target = input.toTarget()

    println("Part 1 : ${target.maxY()}")
}

private data class Target(val xRange: IntRange, val yRange: IntRange) {

    fun maxY(): Int {
        val maxVelocityY = max(yRange.first.absoluteValue, yRange.last.absoluteValue) - 1
        return (maxVelocityY + 1) * maxVelocityY / 2
    }
}

private fun String.getRange(name: String) =
    """.*$name=(-?\d+)..(-?\d+).*""".toRegex()
        .find(this)!!.destructured
        .let { (start, end) -> start.toInt()..end.toInt() }

private fun String.toTarget() = Target(this.getRange("x"), this.getRange("y"))


