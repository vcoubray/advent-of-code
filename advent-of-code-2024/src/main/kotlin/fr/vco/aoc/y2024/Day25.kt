package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day25")
    val (inputKeys, inputLocks) = input.split { it.isBlank() }
        .partition { it.first().contains("#") }

    val keys = inputKeys.map { it.toColumnSizes() }
    val locks = inputLocks.map { it.toColumnSizes() }
    val columnMaxSize = inputKeys.first().size - 2

    var result = 0
    for (key in keys) {
        result += locks.count { key.fit(it, columnMaxSize) }
    }

    println("Part 1: $result")
}

fun List<String>.toColumnSizes(): List<Int> {
    return this.first().mapIndexed { i, _ ->
        this.count { it[i] == '#' } - 1
    }
}

fun List<Int>.fit(lock: List<Int>, maxSize: Int) = this.indices.all { this[it] + lock[it] <= maxSize }