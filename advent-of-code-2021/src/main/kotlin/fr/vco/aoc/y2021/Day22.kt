package fr.vco.aoc.y2021

import kotlin.math.max
import kotlin.math.min

const val RANGE_REGEX = """-?\d+..-?\d+"""
val CUBOID_REGEX = """(on|off) x=($RANGE_REGEX),y=($RANGE_REGEX),z=($RANGE_REGEX)""".toRegex()
val PART1_ZONE = Cuboid(-50..50, -50..50, -50..50)

fun main() {
    val input = readLines("Day22")

    val instructions = input.map {
        val (on, x, y, z) = CUBOID_REGEX.find(it)!!.destructured
        Instruction(on == "on", Cuboid(x.toRange(), y.toRange(), z.toRange()))
    }

    println("Part 1: ${instructions.filter { it.cuboid in PART1_ZONE }.fold(Reactor()) { reactor, cuboid -> reactor.execute(cuboid) }.size()}")
    println("Part 2: ${instructions.fold(Reactor()) { reactor, cuboid -> reactor.execute(cuboid) }.size()}")
}

private fun String.toRange() = this.split("..").map { it.toInt() }.let { it.first()..it.last() }

data class Instruction(val on: Boolean, val cuboid: Cuboid)

data class Cuboid(
    val x: IntRange,
    val y: IntRange,
    val z: IntRange
) {
    val size = x.count().toLong() * y.count().toLong() * z.count().toLong()

    operator fun contains(cuboid: Cuboid) = cuboid.x in x && cuboid.y in y && cuboid.z in z


    operator fun minus(cuboid: Cuboid): List<Cuboid> {
        if (this in cuboid) return emptyList()
        val intersect = Cuboid(x.intersectRange(cuboid.x), y.intersectRange(cuboid.y), z.intersectRange(cuboid.z))
        if (intersect.size == 0L) return listOf(this)

        val xRanges = getChunkedRanges(intersect) { it.x }
        val yRanges = getChunkedRanges(intersect) { it.y }
        val zRanges = getChunkedRanges(intersect) { it.z }

        return xRanges.flatMap { xRange ->
            yRanges.flatMap { yRange ->
                zRanges.map { zRange ->
                    Cuboid(xRange, yRange, zRange)
                }
            }
        }.filterNot { it == intersect }

    }

    private fun getChunkedRanges(subCuboid: Cuboid, axis: (Cuboid) -> IntRange) = listOf(
        axis(this).first until axis(subCuboid).first,
        (axis(subCuboid).last + 1)..axis(this).last,
        axis(subCuboid)
    ).filterNot { it.isEmpty() }
}

class Reactor(private val cuboids: List<Cuboid> = emptyList()) {

    fun execute(instruction: Instruction) =
        if (instruction.on) Reactor(cuboids.map { it - instruction.cuboid }.flatten() + instruction.cuboid)
        else Reactor(cuboids.map { it - instruction.cuboid }.flatten())

    fun size() = cuboids.sumOf { it.size }
}

private operator fun IntRange.contains(range: IntRange) = range.first in this && range.last in this
private fun IntRange.intersectRange(range: IntRange) = max(first, range.first)..min(last, range.last)