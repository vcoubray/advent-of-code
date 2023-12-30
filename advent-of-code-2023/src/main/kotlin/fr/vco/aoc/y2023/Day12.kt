package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day12")
    val springs = input.map { it.toSpringRow() }

    println("Part 1: ${springs.sumOf { it.countValidCombination() }} ")
    println("Part 2: ${springs.map { it.unfold() }.sumOf { it.countValidCombination() }} ")
}

data class SpringRow(
    val springs: String,
    val groups: List<Int>,
) {
    fun unfold() = SpringRow(List(5) { springs }.joinToString("?"), List(5) { groups }.flatten())

    fun countValidCombination(): Long {
        val groupMask = groups.joinToString("") { ".".padEnd(it + 1, '#') } + '.'
        val operationalMatrices = Array(springs.length + 1) { LongArray(groupMask.length) { 0 } }
        val damagedMatrices = Array(springs.length + 1) { LongArray(groupMask.length) { 0 } }

        operationalMatrices[0][0] = 1
        damagedMatrices[0][0] = 1

        for (i in springs.indices) {
            val c = springs[i]
            if (c == '.' || c == '?') {
                operationalMatrices[i + 1][0] = operationalMatrices[i][0]
                damagedMatrices[i + 1][0] = damagedMatrices[i][0]
            }

            for (j in 1..<groupMask.length) {
                fun operational() {
                    if (groupMask[j] == '.') {
                        operationalMatrices[i + 1][j] += operationalMatrices[i][j]
                        damagedMatrices[i + 1][j] += operationalMatrices[i][j]
                    }
                }

                fun damaged() {
                    if (groupMask[j] == '#') {
                        operationalMatrices[i + 1][j] += damagedMatrices[i][j - 1]
                        damagedMatrices[i + 1][j] += damagedMatrices[i][j - 1]
                    } else {
                        operationalMatrices[i + 1][j] += damagedMatrices[i + 1][j - 1]
                    }
                }

                when (c) {
                    '.' -> operational()
                    '#' -> damaged()
                    '?' -> {
                        operational()
                        damaged()
                    }
                }
            }
        }
        return operationalMatrices.last().last()
    }
}

fun String.toSpringRow(): SpringRow {
    val (springs, groups) = this.split(" ")
    return SpringRow(springs, groups.split(",").map { it.toInt() })
}