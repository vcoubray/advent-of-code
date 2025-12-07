package fr.vco.aoc.y2025

import kotlin.text.mapIndexedNotNull

fun main() {
    val input = readLines("Day07")
    val tachyonTree = TachyonTree(input)

    println("Part 1: ${tachyonTree.countSplits()}")
    println("Part 2: ${tachyonTree.countTimelines()}")
}

class TachyonTree(input: List<String>) {
    val start: Int
    val splitters: Map<Int, List<Int>>

    init {
        val width = input.first().length
        splitters = input
            .flatMapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (c == '^') y * width + x else null } }
            .associateWith { id ->
                val x = id % width
                val y = id / width
                listOfNotNull(
                    input.findNextSplitter(x - 1, y),
                    input.findNextSplitter(x + 1, y)
                )
            }
        start = input.findNextSplitter(input.first().indexOf("S"), 0) ?: throw IllegalStateException("No Start found")
    }

    private fun List<String>.findNextSplitter(x: Int, y: Int): Int? {
        val width = this.first().length
        val child = this.drop(y).indexOfFirst { it[x] == '^' }
        return if (child > -1) (child + y) * width + x else null
    }

    fun countSplits(): Int {
        val toVisit = ArrayDeque<Int>().apply { add(start) }
        val visited = mutableSetOf(start)

        while (toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()
            splitters[curr]!!
                .filter { it !in visited }
                .forEach {
                    toVisit.addLast(it)
                    visited.add(it)
                }
        }
        return visited.size
    }

    val timelineCache = HashMap<Int, Long>()
    fun countTimelines(start: Int = this.start): Long {
        val cached = timelineCache[start]
        if(cached != null) {
            return cached
        }
        val children = splitters[start]!!
        val timelines = children.sumOf(::countTimelines) + 2 - children.size
        timelineCache[start] = timelines
        return timelines
    }
}