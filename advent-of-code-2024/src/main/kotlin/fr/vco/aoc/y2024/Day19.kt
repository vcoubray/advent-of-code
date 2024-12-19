package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day19")
    val towels = input.first().split(", ")
    val designs = input.drop(2)

    println("Part 1: ${designs.count { it.countCombinations(towels) > 0 }}")
    println("Part 2: ${designs.sumOf { it.countCombinations(towels) }}")
}

fun String.countCombinations(towels: List<String>): Long {
    val canStartAt = MutableList(this.length + 1) { 0L }
    canStartAt[0] = 1
    val maxTowelSize = towels.maxOf { towel -> towel.length }

    for (i in this.indices) {
        if (canStartAt[i] == 0L) continue
        for (j in i + 1..i + 1 + maxTowelSize) {
            if (j <= this.length) {
                if (this.substring(i, j) in towels) {
                    canStartAt[j] += canStartAt[i]
                }
            }
        }
    }
    return canStartAt.last()
}


