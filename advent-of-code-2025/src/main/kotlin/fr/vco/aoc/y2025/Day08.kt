package fr.vco.aoc.y2025

import kotlin.collections.forEach
import kotlin.collections.take
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day08")
    val circuits = JunctionCircuits(input.map { it.toJunctionBox() })

    println("Part 1: ${circuits.getCircuits(1000)}")
    println("Part 2: ${circuits.completeCircuit()}")
}

data class JunctionBox(val x: Long, val y: Long, val z: Long) {
    fun squareDistance(other: JunctionBox) = (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()
}

fun String.toJunctionBox(): JunctionBox {
    val (x, y, z) = this.split(",").map { it.toLong() }
    return JunctionBox(x, y, z)
}

class JunctionCircuits(val junctionBoxes: List<JunctionBox>) {
    val distances = (0..<junctionBoxes.lastIndex).flatMap { i ->
        (i + 1..junctionBoxes.lastIndex).map { j ->
            val dist = junctionBoxes[i].squareDistance(junctionBoxes[j])
            dist to (i to j)
        }
    }.sortedBy { it.first }

    val boxesCircuitId = MutableList(junctionBoxes.size) { it }
    val circuits = MutableList(junctionBoxes.size) { mutableListOf(it) }

    fun getCircuits(linksToAdd: Int): Long {
        distances.take(linksToAdd).forEach { (_, link) -> addLink(link) }
        return circuits.sortedByDescending { it.size }.take(3).fold(1L) { acc, a -> acc * a.size }
    }

    fun addLink(link: Pair<Int, Int>) {
        if (boxesCircuitId[link.first] != boxesCircuitId[link.second]) {
            val circuitId = min(boxesCircuitId[link.first], boxesCircuitId[link.second])
            val removedCircuitId = max(boxesCircuitId[link.first], boxesCircuitId[link.second])
            circuits[removedCircuitId].forEach { boxesCircuitId[it] = circuitId }
            circuits[circuitId].addAll(circuits[removedCircuitId])
            circuits[removedCircuitId] = mutableListOf()
        }
    }

    fun completeCircuit(): Long {
        var linkId = 0
        while (circuits[0].size < junctionBoxes.size) {
            val (_, link) = distances[linkId]
            addLink(link)
            linkId++
        }
        val (_, finalLink) = distances[linkId - 1]
        return junctionBoxes[finalLink.first].x * junctionBoxes[finalLink.second].x
    }
}