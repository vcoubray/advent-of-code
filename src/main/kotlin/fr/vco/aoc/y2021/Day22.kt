package fr.vco.aoc.y2021

const val RANGE_REGEX = """-?\d+..-?\d+"""
val CUBOID_REGEX = """(on|off) x=($RANGE_REGEX),y=($RANGE_REGEX),z=($RANGE_REGEX)""".toRegex()

fun main() {
    val input = readLines("Day22")

    val cuboids = input.map {
        val (on, x, y, z) = CUBOID_REGEX.find(it)!!.destructured
        Cuboid(on == "on", x.toRange(), y.toRange(), z.toRange())
    }
    cuboids.filter { it in -50..50 }.reversed()

}

private fun String.toRange() = this.split("..").map { it.toInt() }.let { it.first()..it.last() }

data class Cuboid(
    val on: Boolean,
    val x: IntRange,
    val y: IntRange,
    val z: IntRange
)

private operator fun IntRange.contains(c: Cuboid) = c.x in this && c.y in this && c.z in this
private operator fun IntRange.contains(range: IntRange) = range.first in this && range.last in this
