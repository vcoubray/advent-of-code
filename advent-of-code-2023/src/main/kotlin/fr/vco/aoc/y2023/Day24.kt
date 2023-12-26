package fr.vco.aoc.y2023

import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val input = readLines("Day24")
    val hailStones = input.map { it.toHailStone() }
    println("Part 1: ${hailStones.countIntersectXY(200_000_000_000_000L..400_000_000_000_000L)}")
    println("Part 2: ${hailStones.findRockPos()}")
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

    fun moveToRelativeReferential(vx: Int = 0, vy: Int = 0, vz: Int = 0) = copy(
        vx = this.vx - vx.toBigInteger(),
        vy = this.vy - vy.toBigInteger(),
        vz = this.vz - vz.toBigInteger()
    )

    fun getHailStoneAt(timing: BigInteger) = copy(
        x = x + (vx * timing),
        y = y + (vy * timing),
        z = z + (vz * timing)
    )
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

fun List<HailStone>.findRockPos(): BigInteger {

    for (vx in -1000..1000) {
        for (vy in -1000..1000) {

            val (a, b) = this.take(2).map { it.moveToRelativeReferential(vx, vy) }
            val rPos = a.intersectXY(b)

            if (rPos != null && this.drop(2).all { rPos == a.intersectXY(it.moveToRelativeReferential(vx, vy)) }) {
                val timingA = (rPos.first.toBigInteger() - a.x) / a.vx
                val timingB = (rPos.first.toBigInteger() - b.x) / b.vx

                val interA = first().getHailStoneAt(timingA)
                val interB = get(1).getHailStoneAt(timingB)

                val rvx = (interA.x - interB.x) / (timingA - timingB)
                val rvy = (interA.y - interB.y) / (timingA - timingB)
                val rvz = (interA.z - interB.z) / (timingA - timingB)

                val rx = interA.x - (rvx * timingA)
                val ry = interA.y - (rvy * timingA)
                val rz = interA.z - (rvz * timingA)

                return rx + ry + rz
            }
        }
    }
    return -BigInteger.ONE
}

fun String.toHailStone(): HailStone {
    val (pos, vector) = this.split(" @ ")
    val (x, y, z) = pos.split(""", """.toRegex()).map { it.trim().toBigInteger() }
    val (vx, vy, vz) = vector.split(""", """.toRegex()).map { it.trim().toBigInteger() }
    return HailStone(x, y, z, vx, vy, vz)
}