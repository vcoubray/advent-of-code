package fr.vco.aoc.y2021

import kotlin.math.absoluteValue

fun main() {
    val input = readFirstLine("Day07")
    val crabs = input.split(",").map { it.toInt() }

    println("Part 1 : ${crabs.getSimpleFuelConsumption(crabs.median())}")

    val crabsRange = crabs.minOrNull()!!..crabs.maxOrNull()!!
    println("Part 2 : ${crabsRange.map { crabs.getLinearFuelConsumption(it) }.minOrNull()}")
}

private fun List<Int>.median() = this.sorted()[size / 2]
private fun List<Int>.getSimpleFuelConsumption(position: Int) = this.sumOf { (position - it).absoluteValue }
private fun List<Int>.getLinearFuelConsumption(position: Int) = this.sumOf { linearFuelConsumption((position - it).absoluteValue) }
private fun linearFuelConsumption(value: Int) = (value + 1) * (value) / 2


