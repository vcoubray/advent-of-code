package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("tDay06")
    val lines = input.readLines()

    val groups = mutableListOf(mutableListOf<Set<Char>>())
    lines.forEach {
        if (it.isBlank()) groups.add(mutableListOf())
        else groups.last().add(it.toSet())
    }

    println("Part 1 : ${groups.sumBy { it.reduce { a, b -> a union b }.size }}")
    println("Part 2 : ${groups.sumBy { it.reduce { a, b -> a intersect b }.size }}")

}