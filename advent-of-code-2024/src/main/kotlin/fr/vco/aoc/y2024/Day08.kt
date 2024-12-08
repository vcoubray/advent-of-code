package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day08")
    val height = input.size
    val width = input.first().length

    val antennas = input.toAntennasPositions()

    fun getAntinodes(a: Position, b: Position): Set<Position> {
        val vector = a - b
        val antinode = vector + a
        return if (antinode.inGrid(height,width)) setOf(antinode)
        else setOf()
    }

    fun getAntinodesWithHarmonics(a: Position, b: Position): Set<Position> {
        val vector = a - b
        var i = 0
        val antipodes = mutableSetOf<Position>()
        while ((a + vector * i).inGrid(width, height)) {
            antipodes.add(a + vector * i)
            i++
        }
        return antipodes
    }

    println("Part 1: ${antennas.getAntinodes(::getAntinodes).size}")
    println("Part 2: ${antennas.getAntinodes(::getAntinodesWithHarmonics).size}")
}

fun List<String>.toAntennasPositions(): MutableMap<Char, MutableList<Position>> {
    val antennas = mutableMapOf<Char, MutableList<Position>>()
    this.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c != '.') {
                antennas.computeIfAbsent(c) { mutableListOf() }.add(Position(x, y))
            }
        }
    }
    return antennas
}

fun MutableMap<Char, MutableList<Position>>.getAntinodes(computeAntinodes: (Position, Position) -> Set<Position>): Set<Position> {
    return this.flatMap { (_, positions) -> positions.getAntinodes(computeAntinodes) }.toSet()
}

fun List<Position>.getAntinodes(computeAntinodes: (Position, Position) -> Set<Position>): Set<Position> {
    val antinodes = mutableSetOf<Position>()
    for (i in 0..<size - 1) {
        for (j in i+1 ..<size) {
            antinodes.addAll(computeAntinodes(this[i],this[j]))
            antinodes.addAll(computeAntinodes(this[j],this[i]))
        }
    }
    return antinodes
}

fun Position.inGrid(width: Int, height: Int) = x in 0..<width && y in 0..<height