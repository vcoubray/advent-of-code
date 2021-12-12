package fr.vco.aoc.y2021

private const val START = "start"
private const val END = "end"

fun main() {
    val input = readLines("Day12")

    val caves = mutableMapOf<String, Cave>()
    input.map { it.split("-") }
        .forEach { (name1, name2) ->
            caves.putIfAbsent(name1, Cave(name1))
            caves.putIfAbsent(name2, Cave(name2))
            val cave1 = caves[name1]!!
            val cave2 = caves[name2]!!
            cave1.neighbors.add(cave2)
            cave2.neighbors.add(cave1)
        }


    val start = caves[START]!!
    val canBeVisitedPart1: (Cave) -> Boolean = { cave -> cave.name != START && (!cave.isSmall || cave.visited < 1) }
    println("Part 1 : ${findPaths(start, canBeVisitedPart1)}")

    val canBeVisitedPart2: (Cave) -> Boolean = { cave ->
        when {
            cave.name == START -> false
            !cave.isSmall -> true
            cave.visited < 1 -> true
            cave.visited < 2 && caves.none { (_, v) -> v.isSmall && v.visited == 2 } -> true
            else -> false
        }
    }
    println("Part 2 : ${findPaths(start, canBeVisitedPart2)}")
}

private fun findPaths(
    cave: Cave,
    canBeVisited: (Cave) -> Boolean,
): Long {
    if (cave.name == END) return 1L

    var pathCount = 0L
    cave.visited++
    cave.neighbors.forEach {
        if (canBeVisited(it)) pathCount += findPaths(it, canBeVisited)
    }
    cave.visited--
    return pathCount
}

private data class Cave(val name: String) {
    val isSmall: Boolean = name.first().isLowerCase()
    var visited = 0
    val neighbors = mutableListOf<Cave>()
}