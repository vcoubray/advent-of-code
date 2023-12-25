package fr.vco.aoc.y2023

import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val input = readLines("Day24")

    println("Part 1: ${input.map { it.toHailStone() }.countIntersectXY(200_000_000_000_000L..400_000_000_000_000L)}")

}

data class HailStone(
        val x: BigInteger, val y: BigInteger, val z: BigInteger,
        val vx: BigInteger, val vy: BigInteger, val vz: BigInteger
    ) {

    fun intersectXY(h: HailStone): Pair<BigDecimal, BigDecimal>? {
        if (vx * h.vy == vy * h.vx) return null

        val s = (-vy * (x - h.x) + vx * (y - h.y)).toBigDecimal() / (-h.vx * vy + vx * h.vy).toBigDecimal()
        val t = (-h.vy * (x - h.x) + h.vx * (y - h.y)).toBigDecimal() / (-h.vx * vy + vx * h.vy).toBigDecimal()

        return if (s >= BigDecimal.ZERO && t >= BigDecimal.ZERO) {
            x.toBigDecimal() + (t * vx) to y.toBigDecimal() + (t * vy)
        } else null
    }
}

fun List<HailStone>.countIntersectXY(area: LongRange): Int {
    var crossingCount = 0
    for (i in 0 until size - 1) {
        for (j in i + 1 until size) {
            this[i].intersectXY(this[j])?.let { (x, y) ->
                if (x.toLong() in area && y.toLong() in area) {
                    crossingCount++
                }
            }
        }
    }
    return crossingCount
}

fun String.toHailStone(): HailStone {
    val (pos, vector) = this.split(" @ ")
    val (x, y, z) = pos.split(""", """.toRegex()).map { it.trim().toBigInteger() }
    val (vx, vy, vz) = vector.split(""", """.toRegex()).map { it.trim().toBigInteger() }
    return HailStone(x, y, z, vx, vy, vz)
}