package fr.vco.aoc.y2024

import kotlin.math.roundToLong

fun main() {
    val input = readLines("Day13")

    val clawMachines = input.split { it.isBlank() }.map { it.toClawMachine() }

    println("Part 1 : ${clawMachines.sumOf { it.getTokens() }}")
    println("Part 2 : ${clawMachines.map{it.withOffset(10_000_000_000_000)}.sumOf{it.getTokens()}}")
}


data class ClawMachine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val prizeX: Long, val prizeY: Long) {

    private val countB = ((prizeY - (ay * prizeX / ax.toDouble())) / ((-ay * bx / ax.toDouble()) + by)).roundToLong()
    private val countA = ((prizeX - (bx * countB)) / ax.toDouble()).roundToLong()

    fun getTokens(): Long {
        return if(isValid()) countB + countA * 3
        else 0
    }

    private fun isValid(): Boolean {
        return prizeX == countA * ax + countB * bx &&
            prizeY == countA * ay + countB * by
    }

    fun withOffset(offset: Long)= copy(prizeX = this.prizeX + offset, prizeY =  this.prizeY + offset)
}

fun List<String>.toClawMachine(): ClawMachine {
    val (buttonA, buttonB, prize) = this
    val (ax, ay) = """X\+(\d+), Y\+(\d+)""".toRegex().find(buttonA)!!.destructured
    val (bx, by) = """X\+(\d+), Y\+(\d+)""".toRegex().find(buttonB)!!.destructured
    val (prizeX, prizeY) = """X=(\d+), Y=(\d+)""".toRegex().find(prize)!!.destructured
    return ClawMachine(ax.toLong(), ay.toLong(), bx.toLong(), by.toLong(), prizeX.toLong(), prizeY.toLong())
}