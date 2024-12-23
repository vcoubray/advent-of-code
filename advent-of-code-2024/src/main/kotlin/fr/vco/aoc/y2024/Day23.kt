package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day23")

    val network = mutableMapOf<String, MutableList<String>>()
    input.forEach { line ->
        val (a, b) = line.split("-")
        network.computeIfAbsent(a) { mutableListOf() }.add(b)
        network.computeIfAbsent(b) { mutableListOf() }.add(a)
    }

    val subNetworks = network.findSubNetworks()
    println("Part 1: ${subNetworks.filter{it.size == 3}.count{ it.any{id -> id.startsWith('t')}}}")
    println("Part 2: ${subNetworks.maxBy { it.size }.joinToString(",") }")
}

fun Map<String, List<String>>.findSubNetworks(): Set<List<String>> {
    var subNetworks = this.map { listOf(it.key) }.toSet()
    var currentSubNetworks = subNetworks
    while(currentSubNetworks.isNotEmpty()) {
        currentSubNetworks = expandSubNetworks(currentSubNetworks)
        subNetworks = subNetworks + currentSubNetworks
    }
    return subNetworks
}

fun Map<String, List<String>>.expandSubNetworks(subNetworks: Set<List<String>>): Set<List<String>> {
    return subNetworks.flatMap { subNetwork ->
        val subSubNetworks = mutableSetOf<List<String>>()
        val firstPc = subNetwork.first()
        this[firstPc]!!.forEach { pc ->
            if (pc !in subNetwork && subNetwork.all { this[it]!!.contains(pc) }) {
                subSubNetworks.add((subNetwork + pc).sorted())
            }
        }
        subSubNetworks
    }.toSet()
}
