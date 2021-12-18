package fr.vco.aoc.y2021

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readFirstLine("Day17")
    val target = input.toTarget()

    println("Part 1 : ${target.maxY()}")
    println("Part 2 : ${target.countValidInitialVelocity()}")
}

private data class Probe(val vx: Int, val vy: Int, val x: Int = 0, val y: Int = 0) {

    fun move() = copy(
        vx = when {
            vx == 0 -> 0
            vx < 0 -> vx + 1
            else -> vx - 1
        },
        vy = vy - 1,
        x = x + vx,
        y = y + vy
    )
}

private data class Target(val xRange: IntRange, val yRange: IntRange) {

    private fun minInitialVelocityY() = min(0, yRange.first)
    private fun maxInitialVelocityY() = max(yRange.first.absoluteValue, yRange.last.absoluteValue) - 1
    private fun minInitialVelocityX() = min(0, yRange.first)
    private fun maxInitialVelocityX() = max(0, xRange.last)

    private fun validInitialVelocityXRange() = minInitialVelocityX()..maxInitialVelocityX()
    private fun validInitialVelocityYRange() = minInitialVelocityY()..maxInitialVelocityY()

    private operator fun contains(probe: Probe) = probe.x in xRange && probe.y in yRange

    fun maxY(): Int {
        val maxVelocityY = maxInitialVelocityY()
        return (maxVelocityY + 1) * maxVelocityY / 2
    }

    private fun isValidInitialVelocity(vx: Int, vy: Int): Boolean {
        var probe = Probe(vx, vy)
        while (probe.vy >= 0 || probe.y >= yRange.first) {
            if (probe in this) return true
            probe = probe.move()
        }
        return false
    }

    fun countValidInitialVelocity(): Int {
        return validInitialVelocityXRange().sumOf { vx ->
            validInitialVelocityYRange().count { vy -> (isValidInitialVelocity(vx, vy)) }
        }
    }
}

private fun String.getRange(name: String) = """.*$name=(-?\d+)..(-?\d+).*""".toRegex()
    .find(this)!!.destructured
    .let { (start, end) -> start.toInt()..end.toInt() }

private fun String.toTarget() = Target(this.getRange("x"), this.getRange("y"))
