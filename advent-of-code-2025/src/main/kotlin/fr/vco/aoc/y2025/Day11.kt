package fr.vco.aoc.y2025

fun main() {
    val input = readLines("Day11")

    val devices = input.associate {
        val (source, outputs) = it.split(": ")
        source to outputs.split(" ")
    }

    println("Part 1: ${devices.getPathCount("you", "out")}")
    println("Part 1: ${devices.getPathCountWithMandatory("svr", "out", listOf("dac", "fft"))}")
}

val DEVICE_PATH_CACHE = HashMap<String, Long>()
fun Map<String, List<String>>.getPathCount(start: String, dest: String): Long {
    if (start == dest) return 1L
    if (DEVICE_PATH_CACHE.containsKey("$start-$dest")) {
        return DEVICE_PATH_CACHE["$start-$dest"]!!
    }
    val pathCount = (this[start] ?: emptyList()).sumOf { output -> this.getPathCount(output, dest) }
    DEVICE_PATH_CACHE["$start-$dest"] = pathCount
    return pathCount
}

fun Map<String, List<String>>.getPathCountWithMandatory(
    start: String,
    dest: String,
    mandatoryNodes: List<String>,
): Long {
    return mandatoryNodes.permutations()
        .map { nodes -> listOf(start) + nodes + dest }
        .sumOf { nodes ->
            nodes.windowed(2).map { (a, b) -> getPathCount(a, b) }.reduce { a, b -> a * b }
        }
}

fun List<String>.permutations(): List<List<String>> {
    return if (this.size <= 1) listOf(this)
    else this.flatMap {
        this.subtract(setOf(it)).toList().permutations().map { combination -> listOf(it) + combination }
    }
}